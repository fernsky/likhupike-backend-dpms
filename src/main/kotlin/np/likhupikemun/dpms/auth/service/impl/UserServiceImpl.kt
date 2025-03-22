package np.likhupikemun.dpms.auth.service.impl

import jakarta.persistence.EntityManager
import np.likhupikemun.dpms.auth.repository.specification.UserSpecifications
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.repository.UserRepository
import np.likhupikemun.dpms.auth.service.PermissionService
import np.likhupikemun.dpms.auth.exception.AuthException
import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.dto.*
import org.springframework.data.domain.Page
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

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

        // Get current permissions
        val existingPermissions = user.getAuthorities()
            .mapNotNull { 
                runCatching { 
                    PermissionType.valueOf(it.authority.removePrefix("PERMISSION_"))
                }.getOrNull()
            }
            .toSet()

        // Get permissions that need to be modified
        val permissionsToModify = permissions.getPermissionsToModify(existingPermissions)

        // Apply changes only for permissions that need modification
        permissionsToModify.forEach { permissionType ->
            val permission = permissionService.findByType(permissionType)
            if (permissions.shouldHavePermission(permissionType)) {
                user.addPermission(permission)
            } else {
                user.removePermission(permission)
            }
        }

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

        request.email?.let { email: String ->
            if (email != user.email && userRepository.existsByEmail(email)) {
                throw AuthException.UserAlreadyExistsException(email)
            }
            user.email = email
        }

        request.isWardLevelUser?.let { isWardLevel: Boolean ->
            user.isWardLevelUser = isWardLevel
        }

        request.wardNumber?.let { wardNum: Int ->
            if (user.isWardLevelUser && wardNum == null) {
                throw AuthException.InvalidUserStateException("Ward number is required for ward level users")
            }
            user.wardNumber = wardNum
        }

        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun getUserById(userId: UUID): User {
        return userRepository.findByIdWithPermissions(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }
    }
}
