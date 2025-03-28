package np.likhupikemun.dpms.auth.service.impl

import jakarta.persistence.EntityManager
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import np.likhupikemun.dpms.auth.repository.specification.UserSpecifications
import np.likhupikemun.dpms.auth.service.UserService
import np.likhupikemun.dpms.auth.repository.UserRepository
import np.likhupikemun.dpms.auth.service.PermissionService
import np.likhupikemun.dpms.auth.exception.AuthException
import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.security.JwtService
import np.likhupikemun.dpms.auth.dto.*
import np.likhupikemun.dpms.common.service.EmailService
import org.springframework.data.domain.Page
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import org.slf4j.LoggerFactory

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val permissionService: PermissionService,
    private val passwordEncoder: PasswordEncoder,
    private val entityManager: EntityManager,
    private val emailService: EmailService,
    private val jwtService: JwtService,
) : UserService {
    private val logger = LoggerFactory.getLogger(javaClass)


    override fun createUser(createUserDto: CreateUserDto): User {
        // Check if user exists
        val existingUser = userRepository.findByEmail(createUserDto.email)
        
        if (existingUser.isPresent) {
            val user = existingUser.get()
            if (user.isDeleted) {
                // Reactivate deleted user
                user.apply {
                    isDeleted = false
                    deletedBy = null
                    deletedAt = null
                    setPassword(passwordEncoder.encode(createUserDto.password))
                    isWardLevelUser = createUserDto.isWardLevelUser
                    wardNumber = createUserDto.wardNumber
                    // Clear existing permissions
                    clearPermissions()
                }
                
                // Add new permissions
                val permissions = permissionService.findByTypes(createUserDto.permissions.filterValues { it }.keys)
                permissions.forEach { user.addPermission(it) }
                
                val reactivatedUser = userRepository.save(user)

                return reactivatedUser
            } else {
                throw AuthException.UserAlreadyExistsException(createUserDto.email)
            }
        }
        
        // Create new user if doesn't exist
        val user = User().apply {
            email = createUserDto.email
            setPassword(passwordEncoder.encode(createUserDto.password))
            isWardLevelUser = createUserDto.isWardLevelUser
            wardNumber = createUserDto.wardNumber
        }

        // Add permissions
        val permissions = permissionService.findByTypes(createUserDto.permissions.filterValues { it }.keys)
        permissions.forEach { user.addPermission(it) }

        val createdUser = userRepository.save(user)

        try {
            val resetToken = jwtService.generateToken(createdUser)
            emailService.sendAccountCreatedEmailAsync(createdUser.email!!, resetToken)
        } catch (e: Exception) {
            logger.error("Failed to send account created email to: {}", createdUser.email, e)
        }

        return createdUser
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
                // Will handle removing old permission internally
                user.addPermission(permission)
            } else {
                user.removePermission(permission)
            }
        }

        entityManager.flush()
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

        val approvedUser = userRepository.save(user)

        try {
            // Send account approved email using template
            emailService.sendAccountApprovedEmailAsync(approvedUser.email!!)
        } catch (e: Exception) {
            // Log but don't throw since approval was successful
            logger.error("Failed to send approval email to: {}", approvedUser.email, e)
        }

        return approvedUser
    }

    override fun deleteUser(userId: UUID, deletedBy: UUID): User {
        val user = userRepository.findById(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }

        if (user.isDeleted) {
            throw AuthException.UserAlreadyDeletedException(userId.toString())
        }

        user.apply {
            isDeleted = true
            this.deletedBy = deletedBy
            this.isApproved = false
            deletedAt = LocalDateTime.now()
        }

        return userRepository.save(user)
    }

    override fun searchUsers(criteria: UserSearchCriteria): Page<UserProjection> {
        val specification = UserSpecifications.fromCriteria(criteria)
        
        // Use distinct count when permissions are involved
        val totalElements = if (criteria.permissions?.isNotEmpty() == true) {
            userRepository.countDistinct(specification)
        } else {
            userRepository.count(specification)
        }
        
        val totalPages = (totalElements + criteria.size - 1) / criteria.size
        
        if (criteria.page > totalPages && totalPages > 0) {
            throw AuthException.PageDoesNotExistException("Page number ${criteria.page} is invalid. Total pages available: $totalPages")
        }
        
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
