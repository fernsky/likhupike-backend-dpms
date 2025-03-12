package np.likhupikemun.dpms.auth.service

import np.likhupikemun.dpms.auth.config.AdminConfig
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.auth.dto.UserPermissionsDto
import np.likhupikemun.dpms.auth.domain.entity.User
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

    private fun createAdminUser() {
        val createUserDto = CreateUserDto(
            email = adminConfig.email,
            password = adminConfig.password,
            permissions = getAllPermissions(),
            isWardLevelUser = false
        )
        
        userService.createUser(createUserDto).apply {
            isApproved = true
        }
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
