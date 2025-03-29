package np.sthaniya.dpis.auth.dto

import jakarta.validation.constraints.NotEmpty
import np.sthaniya.dpis.auth.domain.enums.PermissionType

/**
 * Data transfer object for managing user permission modifications.
 * 
 * This class provides an immutable representation of permission changes to be applied
 * to a user, along with utility methods for permission set manipulation.
 * Implements validation through Jakarta Validation annotations.
 *
 * Example usage:
 * ```kotlin
 * val dto = UserPermissionsDto(mapOf(
 *     PermissionType.VIEW_USER to true,    // Grant VIEW_USER
 *     PermissionType.EDIT_USER to false    // Revoke EDIT_USER
 * ))
 * 
 * // Get permissions to add
 * val toGrant = dto.getPermissionsToGrant()
 * 
 * // Get permissions to remove
 * val toRevoke = dto.getPermissionsToRevoke()
 * 
 * // Update existing permission set
 * val updatedPermissions = dto.updateExistingPermissions(currentPermissions)
 * ```
 *
 * @param permissions Map of [PermissionType] to boolean indicating grant (true) or revoke (false)
 * @throws jakarta.validation.ConstraintViolationException if permissions map is empty
 */
data class UserPermissionsDto(
    @field:NotEmpty(message = "At least one permission must be specified")
    val permissions: Map<PermissionType, Boolean>
) {

    /**
     * Returns a set of permissions that should be granted to the user.
     * Filters the permissions map for entries with value = true.
     */
    fun getPermissionsToGrant(): Set<PermissionType> = 
        permissions.filterValues { it }.keys

    /**
     * Returns a set of permissions that should be revoked from the user.
     * Filters the permissions map for entries with value = false.
     */
    fun getPermissionsToRevoke(): Set<PermissionType> = 
        permissions.filterValues { !it }.keys

    /**
     * Applies the permission changes to an existing set of permissions.
     * This operation is non-destructive to the input set.
     *
     * @param currentPermissions The current set of permissions
     * @return A new set containing the updated permissions
     */
    fun updateExistingPermissions(currentPermissions: Set<PermissionType>): Set<PermissionType> {
        val updatedPermissions = currentPermissions.toMutableSet()
        getPermissionsToGrant().forEach { updatedPermissions.add(it) }
        getPermissionsToRevoke().forEach { updatedPermissions.remove(it) }
        return updatedPermissions
    }

    /**
     * Returns the set of permissions that need to be modified based on their current state.
     * A permission needs modification if its desired state differs from its current state.
     *
     * @param existingPermissions Current set of permissions
     * @return Set of [PermissionType] that need to be added or removed
     */
    fun getPermissionsToModify(existingPermissions: Set<PermissionType>): Set<PermissionType> {
        val permissionsToModify = mutableSetOf<PermissionType>()
        
        permissions.forEach { (permission, shouldHave) ->
            val hasPermission = permission in existingPermissions
            // Only add to modification set if there's a change needed
            if (shouldHave != hasPermission) {
                permissionsToModify.add(permission)
            }
        }
        
        return permissionsToModify
    }

    /**
     * Checks if a specific permission should be granted.
     *
     * @param permission The permission to check
     * @return true if the permission should be granted, false if it should be revoked
     *         or if it's not present in the permissions map
     */
    fun shouldHavePermission(permission: PermissionType): Boolean =
        permissions[permission] ?: false
}
