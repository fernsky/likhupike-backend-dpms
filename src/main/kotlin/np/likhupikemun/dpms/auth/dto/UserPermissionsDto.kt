package np.likhupikemun.dpms.auth.dto

import jakarta.validation.constraints.NotEmpty
import np.likhupikemun.dpms.auth.domain.enum.PermissionType

data class UserPermissionsDto(
    @field:NotEmpty(message = "At least one permission must be specified")
    val permissions: Map<PermissionType, Boolean>
) {
    // Get only the permissions that should be granted
    fun getPermissionsToGrant(): Set<PermissionType> =
        permissions.filterValues { it }.keys

    // Get only the permissions that should be revoked
    fun getPermissionsToRevoke(): Set<PermissionType> =
        permissions.filterValues { !it }.keys

    // Update existing permissions - only modifies permissions that are present in the DTO
    fun updateExistingPermissions(currentPermissions: Set<PermissionType>): Set<PermissionType> {
        val updatedPermissions = currentPermissions.toMutableSet()
        getPermissionsToGrant().forEach { updatedPermissions.add(it) }
        getPermissionsToRevoke().forEach { updatedPermissions.remove(it) }
        return updatedPermissions
    }
}
