package np.likhupikemun.dpms.auth.service.impl

import jakarta.persistence.EntityManager
import np.likhupikemun.dpms.auth.repository.specification.UserSpecifications
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.repository.UserRepository
import np.likhupikemun.dpms.auth.service.PermissionService
import np.likhupikemun.dpms.auth.exception.AuthException
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.auth.dto.UserPermissionsDto
import np.likhupikemun.dpms.auth.domain.entity.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import javax.persistence.criteria.Predicate

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val permissionService: PermissionService,
    private val passwordEncoder: PasswordEncoder,
    private val entityManager: EntityManager
) : UserService {
    override fun createUser(createUserDto: CreateUserDto): User {
        if (userRepository.existsByEmail(createUserDto.email)) {
            throw AuthException.UserAlreadyExistsException(createUserDto.email)
        }
        
        val user = User().apply {
            email = createUserDto.email
            setPassword(passwordEncoder.encode(createUserDto.password))
            isWardLevelUser = createUserDto.isWardLevelUser
            wardNumber = createUserDto.wardNumber
        }

        // Add permissions
        val permissions = permissionService.findByTypes(createUserDto.permissions.filterValues { it }.keys)
        permissions.forEach { user.addPermission(it) }

        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): User? =
        userRepository.findByEmailWithPermissions(email).orElse(null)

    override fun updatePermissions(userId: UUID, permissions: UserPermissionsDto): User {
        val user = userRepository.findByIdWithPermissions(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }

        if (user.isDeleted) {
            throw AuthException.UserAlreadyDeletedException(userId.toString())
        }

        val permissionsToGrant = permissionService.findByTypes(permissions.getPermissionsToGrant())
        val permissionsToRevoke = permissionService.findByTypes(permissions.getPermissionsToRevoke())

        permissionsToGrant.forEach { user.addPermission(it) }
        permissionsToRevoke.forEach { user.removePermission(it) }

        return userRepository.save(user)
    }

    override fun resetPassword(userId: UUID, newPassword: String): User {
        val user = userRepository.findById(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }

        if (user.isDeleted) {
            throw AuthException.UserAlreadyDeletedException(userId.toString())
        }
        
        user.setPassword(passwordEncoder.encode(newPassword))
        return userRepository.save(user)
    }

    override fun approveUser(userId: UUID, approvedBy: UUID): User {
        val user = userRepository.findById(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }

        if (user.isDeleted) {
            throw AuthException.UserAlreadyDeletedException(userId.toString())
        }
        
        if (user.isApproved) {
            throw AuthException.UserAlreadyApprovedException(userId.toString())
        }

        user.apply {
            isApproved = true
            this.approvedBy = approvedBy
            approvedAt = LocalDateTime.now()
        }

        return userRepository.save(user)
    }

    override fun deleteUser(userId: UUID, deletedBy: String): User {
        val user = userRepository.findById(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }

        if (user.isDeleted) {
            throw AuthException.UserAlreadyDeletedException(userId.toString())
        }

        user.apply {
            isDeleted = true
            this.deletedBy = deletedBy
            deletedAt = LocalDateTime.now()
        }

        return userRepository.save(user)
    }

    override fun searchUsers(criteria: UserSearchCriteria): Page<UserProjection> {
        val specification = UserSpecifications.fromCriteria(criteria)
        return userRepository.findAllWithProjection(
            spec = specification,
            pageable = criteria.toPageable(),
            columns = criteria.getValidColumns()
        )
    }

    override fun updateUser(userId: UUID, request: UpdateUserRequest): User {
        val user = userRepository.findById(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }

        if (user.isDeleted) {
            throw AuthException.UserAlreadyDeletedException(userId.toString())
        }

        request.email?.let { email ->
            if (email != user.email && userRepository.existsByEmail(email)) {
                throw AuthException.UserAlreadyExistsException(email)
            }
            user.email = email
        }

        request.isWardLevelUser?.let { isWardLevel ->
            user.isWardLevelUser = isWardLevel
        }

        request.wardNumber?.let { wardNum ->
            if (user.isWardLevelUser && wardNum == null) {
                throw AuthException.InvalidUserStateException("Ward number is required for ward level users")
            }
            user.wardNumber = wardNum
        }

        return userRepository.save(user)
    }
}
