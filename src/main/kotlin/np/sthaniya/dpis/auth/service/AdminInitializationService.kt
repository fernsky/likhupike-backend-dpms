package np.sthaniya.dpis.auth.service

import np.sthaniya.dpis.auth.config.AdminConfig
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.dto.CreateUserDto
import np.sthaniya.dpis.auth.dto.UserPermissionsDto
import np.sthaniya.dpis.auth.domain.entity.User
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service responsible for initializing the system administrator account.
 *
 * This service ensures that a system administrator account exists with the proper
 * permissions when the application starts. It handles both the initial creation
 * and subsequent verification of admin permissions.
 *
 * @property userService Service for user-related operations
 * @property adminConfig Configuration containing admin account details
 */
@Service
class AdminInitializationService(
    private val userService: UserService,
    private val adminConfig: AdminConfig
) {
    /**
     * Initializes the admin user when the application starts.
     *
     * This method is triggered by Spring's [ApplicationReadyEvent] and ensures that:
     * 1. An admin user exists with the configured email
     * 2. The admin user has all required permissions
     */
    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun initializeAdminUser() {
        val existingAdmin = userService.findByEmail(adminConfig.email)
        
        if (existingAdmin == null) {
            // Create new admin
            createAdminUser()
        } else if (!verifyAdminPermissions(existingAdmin)) {
            // Update admin permissions if needed
            updateAdminPermissions(existingAdmin)
        }
    }

    /**
     * Creates a new administrator user with all available permissions.
     *
     * The created user is automatically approved and granted all system permissions.
     */
    @Transactional
    private fun createAdminUser() {
        val createUserDto = CreateUserDto(
            email = adminConfig.email,
            password = adminConfig.password,
            permissions = getAllPermissions(),
            isWardLevelUser = false
        )
        
        val user = userService.createUser(createUserDto)
        userService.approveUser(user.id!!, user.id!!) // Self-approve admin
    }

    /**
     * Creates a map of all available permissions set to true.
     *
     * @return Map of [PermissionType] to Boolean, with all permissions enabled
     */
    private fun getAllPermissions(): Map<PermissionType, Boolean> =
        PermissionType.values().associateWith { true }

    /**
     * Verifies that the admin user has all required permissions.
     *
     * @param admin The admin [User] to verify
     * @return true if the admin has all permissions, false otherwise
     */
    private fun verifyAdminPermissions(admin: User): Boolean =
        PermissionType.values().all { admin.hasPermission(it) }

    /**
     * Updates the admin user's permissions to ensure they have all available permissions.
     *
     * @param admin The admin [User] whose permissions need to be updated
     */
    private fun updateAdminPermissions(admin: User) {
        userService.updatePermissions(
            admin.id!!,
            UserPermissionsDto(getAllPermissions())
        )
    }
}
