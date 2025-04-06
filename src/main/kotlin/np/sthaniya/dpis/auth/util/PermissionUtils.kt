package np.sthaniya.dpis.auth.util

import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.domain.entity.User

/**
 * Utility class for working with permissions in the application.
 *
 * Provides helper methods for checking permission relationships,
 * handling permission hierarchies, and permission evaluation.
 */
object PermissionUtils {

    /**
     * Checks if a user has at least one of the specified permissions,
     * taking into account permission inheritance.
     * 
     * @param user The user to check permissions for
     * @param permissions One or more permissions to check
     * @return true if the user has any of the permissions or a higher-level permission
     */
    fun hasAnyPermission(user: User, vararg permissions: PermissionType): Boolean {
        // If the user has any of the direct permissions
        if (permissions.any { permission -> user.hasEffectivePermission(permission) }) {
            return true
        }
        
        // Check if any of the user's permissions imply the required ones
        val userPermissions = user.getEffectivePermissions()
        return permissions.any { required ->
            userPermissions.any { held -> PermissionType.implies(held, required) }
        }
    }
    
    /**
     * Checks if a user has all of the specified permissions,
     * taking into account permission inheritance.
     * 
     * @param user The user to check permissions for
     * @param permissions One or more permissions to check
     * @return true if the user has all of the permissions or higher-level permissions
     */
    fun hasAllPermissions(user: User, vararg permissions: PermissionType): Boolean {
        val userPermissions = user.getEffectivePermissions()
        return permissions.all { required ->
            user.hasEffectivePermission(required) || 
            userPermissions.any { held -> PermissionType.implies(held, required) }
        }
    }
    
    /**
     * Gets the "view" equivalent for a given permission.
     * Useful for implementing read-only versions of functionality.
     * 
     * @param permission The permission to find a view-only equivalent for
     * @return The view permission or null if there's no direct equivalent
     */
    fun getViewPermissionFor(permission: PermissionType): PermissionType? {
        return when (permission) {
            PermissionType.MANAGE_CITIZENS,
            PermissionType.CREATE_CITIZEN, 
            PermissionType.EDIT_CITIZEN, 
            PermissionType.DELETE_CITIZEN,
            PermissionType.APPROVE_CITIZEN -> PermissionType.VIEW_CITIZEN
            
            PermissionType.MANAGE_USERS,
            PermissionType.CREATE_USER, 
            PermissionType.EDIT_USER, 
            PermissionType.DELETE_USER,
            PermissionType.APPROVE_USER,
            PermissionType.RESET_USER_PASSWORD,
            PermissionType.MANAGE_USER_ROLES -> PermissionType.VIEW_USER
                        
            else -> null
        }
    }
    
    /**
     * Determines whether a given permission is a master/aggregate permission
     * that encompasses other permissions.
     * 
     * @param permission The permission to check
     * @return true if the permission is a master/aggregate permission
     */
    fun isMasterPermission(permission: PermissionType): Boolean {
        return when (permission) {
            PermissionType.MANAGE_USERS,
            PermissionType.MANAGE_CITIZENS,
            PermissionType.SYSTEM_ADMIN -> true
            else -> false
        }
    }
}
