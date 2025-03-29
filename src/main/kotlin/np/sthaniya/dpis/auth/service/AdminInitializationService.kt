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

@Service
class AdminInitializationService(
    private val userService: UserService,
    private val adminConfig: AdminConfig
) {
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

    private fun getAllPermissions(): Map<PermissionType, Boolean> =
        PermissionType.values().associateWith { true }

    private fun verifyAdminPermissions(admin: User): Boolean =
        PermissionType.values().all { admin.hasPermission(it) }

    private fun updateAdminPermissions(admin: User) {
        userService.updatePermissions(
            admin.id!!,
            UserPermissionsDto(getAllPermissions())
        )
    }
}
