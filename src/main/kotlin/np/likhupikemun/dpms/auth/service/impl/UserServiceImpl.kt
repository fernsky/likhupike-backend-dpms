package np.likhupikemun.dpms.auth.service.impl

import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.repository.UserRepository
import np.likhupikemun.dpms.auth.service.PermissionService
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
    private val passwordEncoder: PasswordEncoder
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
}
