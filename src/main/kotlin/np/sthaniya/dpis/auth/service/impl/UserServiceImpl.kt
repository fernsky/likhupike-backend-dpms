package np.sthaniya.dpis.auth.service.impl

import jakarta.persistence.EntityManager
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.domain.enums.RoleType
import np.sthaniya.dpis.auth.repository.specification.UserSpecifications
import np.sthaniya.dpis.auth.service.UserService
import np.sthaniya.dpis.auth.repository.UserRepository
import np.sthaniya.dpis.auth.service.PermissionService
import np.sthaniya.dpis.auth.service.RoleService
import np.sthaniya.dpis.auth.exception.AuthException
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.security.JwtService
import np.sthaniya.dpis.auth.dto.*
import np.sthaniya.dpis.common.service.EmailService
import org.springframework.data.domain.Page
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import org.slf4j.LoggerFactory

/**
 * Implementation of [UserService] that manages user-related operations.
 *
 * This service handles user creation, updates, permissions management, and account status changes.
 * It ensures proper user management with features like:
 * - User creation and reactivation
 * - Permission management
 * - Password management
 * - Account approval and deletion
 * - User search functionality
 *
 * @property userRepository Repository for user-related database operations
 * @property permissionService Service for managing user permissions
 * @property passwordEncoder Encoder for secure password handling
 * @property entityManager JPA entity manager for advanced database operations
 * @property emailService Service for sending email notifications
 * @property jwtService Service for JWT token operations
 */
@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val permissionService: PermissionService,
    private val passwordEncoder: PasswordEncoder,
    private val entityManager: EntityManager,
    private val emailService: EmailService,
    private val jwtService: JwtService,
) : UserService {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Creates a new user or reactivates a deleted user.
     *
     * @param createUserDto Data transfer object containing user creation details
     * @return The created or reactivated [User]
     * @throws AuthException.UserAlreadyExistsException if an active user with the email already exists
     */
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
                    // Clear existing roles
                    clearRoles()
                }
                
                // Add new permissions
                val permissions = permissionService.findByTypes(createUserDto.permissions.filterValues { it }.keys)
                permissions.forEach { user.addPermission(it) }

                // Add new roles
                val roles = roleService.findByTypes(createUserDto.roles.filterValues { it }.keys)
                roles.forEach { user.addRole(it) }
                
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

        // Add roles
        val roles = roleService.findByTypes(createUserDto.roles.filterValues { it }.keys)
        roles.forEach { user.addRole(it) }

        val createdUser = userRepository.save(user)

        try {
            val resetToken = jwtService.generateToken(createdUser)
            emailService.sendAccountCreatedEmailAsync(createdUser.email!!, resetToken)
        } catch (e: Exception) {
            logger.error("Failed to send account created email to: {}", createdUser.email, e)
        }

        return createdUser
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user
     * @return The [User] if found, or null if not found
     */
    @Transactional(readOnly = true)
    override fun findByEmail(email: String): User? =
        userRepository.findByEmailWithPermissions(email).orElse(null)

    /**
     * Updates the permissions of a user.
     *
     * @param userId The ID of the user
     * @param permissions The new permissions to be assigned to the user
     * @return The updated [User]
     * @throws AuthException.UserNotFoundException if the user is not found
     * @throws AuthException.UserAlreadyDeletedException if the user is already deleted
     */
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

    /**
     * Updates the roles of a user.
     *
     * @param userId The ID of the user
     * @param roles The new roles to be assigned to the user
     * @return The updated [User]
     * @throws AuthException.UserNotFoundException if the user is not found
     * @throws AuthException.UserAlreadyDeletedException if the user is already deleted
     */
    override fun updateRoles(userId: UUID, roles: UserRolesDto): User {
        val user = userRepository.findByIdWithRoles(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }

        if (user.isDeleted) {
            throw AuthException.UserAlreadyDeletedException(userId.toString())
        }

        // Get current roles
        val existingRoles = user.getAuthorities()
            .mapNotNull { 
                runCatching { 
                    RoleType.valueOf(it.authority.removePrefix("ROLE_"))
                }.getOrNull()
            }
            .toSet()

        // Get roles that need to be modified
        val rolesToModify = roles.getRolesToModify(existingRoles)

        // Apply changes only for roles that need modification
        rolesToModify.forEach { roleType ->
            val role = roleService.findByType(roleType)
            if (roles.shouldHaveRole(roleType)) {
                // Will handle removing old role internally
                user.addRole(role)
            } else {
                user.removeRole(role)
            }
        }

        entityManager.flush()
        return userRepository.save(user)
    }

    /**
     * Resets the password of a user.
     *
     * @param userId The ID of the user
     * @param newPassword The new password to be set
     * @return The updated [User]
     * @throws AuthException.UserNotFoundException if the user is not found
     * @throws AuthException.UserAlreadyDeletedException if the user is already deleted
     */
    override fun resetPassword(userId: UUID, newPassword: String): User {
        val user = userRepository.findById(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }

        if (user.isDeleted) {
            throw AuthException.UserAlreadyDeletedException(userId.toString())
        }
        
        user.setPassword(passwordEncoder.encode(newPassword))
        return userRepository.save(user)
    }

    /**
     * Approves a user account.
     *
     * @param userId The ID of the user to be approved
     * @param approvedBy The ID of the approver
     * @return The approved [User]
     * @throws AuthException.UserNotFoundException if the user is not found
     * @throws AuthException.UserAlreadyDeletedException if the user is already deleted
     * @throws AuthException.UserAlreadyApprovedException if the user is already approved
     */
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

    /**
     * Deletes a user account.
     *
     * @param userId The ID of the user to be deleted
     * @param deletedBy The ID of the deleter
     * @return The deleted [User]
     * @throws AuthException.UserNotFoundException if the user is not found
     * @throws AuthException.UserAlreadyDeletedException if the user is already deleted
     */
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

    /**
     * Searches for users based on the given criteria.
     *
     * @param criteria The search criteria
     * @return A paginated list of user projections
     * @throws AuthException.PageDoesNotExistException if the requested page does not exist
     */
    override fun searchUsers(criteria: UserSearchCriteria): Page<UserProjection> {
        val specification = UserSpecifications.fromCriteria(criteria)
        
        // Use distinct count when permissions are involved
        val totalElements = if (criteria.permissions?.isNotEmpty() == true || criteria.roles?.isNotEmpty() == true) {
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

    /**
     * Updates a user's details.
     *
     * @param userId The ID of the user to be updated
     * @param request The update request containing new user details
     * @return The updated [User]
     * @throws AuthException.UserNotFoundException if the user is not found
     * @throws AuthException.UserAlreadyDeletedException if the user is already deleted
     * @throws AuthException.UserAlreadyExistsException if the new email is already in use
     * @throws AuthException.InvalidUserStateException if the ward number is invalid for ward level users
     */
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

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user
     * @return The [User] if found
     * @throws AuthException.UserNotFoundException if the user is not found
     */
    @Transactional(readOnly = true)
    override fun getUserById(userId: UUID): User {
        return userRepository.findByIdWithPermissions(userId)
            .orElseThrow { AuthException.UserNotFoundException(userId.toString()) }
    }
}
