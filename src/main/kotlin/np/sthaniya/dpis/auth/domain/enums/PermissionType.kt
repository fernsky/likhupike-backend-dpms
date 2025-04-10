package np.sthaniya.dpis.auth.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Central registry of all permissions used for role-based access control (RBAC).
 *
 * This enum defines the complete hierarchy of permissions in the Digital Profile Information System.
 * Permissions are organized by functional domain and follow a hierarchical structure where
 * higher-level permissions implicitly grant access to related lower-level permissions.
 *
 * For example, having MANAGE_CITIZENS automatically grants all citizen-related permissions.
 *
 * The permissions are used in conjunction with Spring Security for authorization.
 *
 * Usage:
 * ```kotlin
 * if (user.hasPermission(PermissionType.MANAGE_CITIZENS)) {
 *     // User can perform any citizen management operation
 * }
 * ```
 */
@Schema(
    description = "System permission types for role-based access control",
    title = "Permission Types"
)
enum class PermissionType {
    // == USER MANAGEMENT PERMISSIONS ==
    
    /**
     * Master permission for all user management operations.
     * Grants all user-related permissions.
     */
    @Schema(description = "Master permission for all user management operations")
    MANAGE_USERS,

    /**
     * Permission to view user information.
     * Allows read-only access to user details.
     */
    @Schema(description = "Permission to view user information")
    VIEW_USER,

    /**
     * Permission to create new users in the system.
     * Required for administrators and user management operations.
     */
    @Schema(description = "Permission to create new users in the system")
    CREATE_USER,

    /**
     * Permission to approve user registrations or account changes.
     * Required for administrative oversight of user management.
     */
    @Schema(description = "Permission to approve user registrations or account changes")
    APPROVE_USER,

    /**
     * Permission to modify existing user information.
     * Allows updating user details and profile information.
     */
    @Schema(description = "Permission to modify existing user information")
    EDIT_USER,

    /**
     * Permission to remove users from the system.
     * Enables soft deletion of user accounts.
     */
    @Schema(description = "Permission to remove users from the system")
    DELETE_USER,

    /**
     * Permission to reset user passwords.
     * Required for password management and recovery operations.
     */
    @Schema(description = "Permission to reset user passwords")
    RESET_USER_PASSWORD,

    /**
     * Permission to manage user roles and permissions.
     * Allows assigning and removing roles and permissions.
     */
    @Schema(description = "Permission to manage user roles and permissions")
    MANAGE_USER_ROLES,

    // == CITIZEN MANAGEMENT PERMISSIONS ==
    
    /**
     * Master permission for all citizen management operations.
     * Grants all citizen-related permissions.
     */
    @Schema(description = "Master permission for all citizen management operations")
    MANAGE_CITIZENS,

    /**
     * Permission to view citizen information.
     * Allows reading citizen profile data.
     */
    @Schema(description = "Permission to view citizen information")
    VIEW_CITIZEN,

    /**
     * Permission to create new citizen records.
     * Required for administrators to register citizens in the system.
     */
    @Schema(description = "Permission to create new citizen records")
    CREATE_CITIZEN,
    
    /**
     * Permission to update citizen records.
     * Allows modifying existing citizen information.
     */
    @Schema(description = "Permission to update citizen information")
    EDIT_CITIZEN,
    
    /**
     * Permission to delete citizen records.
     * Enables removal of citizen data from the system.
     */
    @Schema(description = "Permission to delete citizen records")
    DELETE_CITIZEN,
    
    /**
     * Permission to approve citizen registrations.
     * Required for validating and activating citizen accounts.
     */
    @Schema(description = "Permission to approve citizen registrations")
    APPROVE_CITIZEN,
    

    // == SYSTEM ADMINISTRATION PERMISSIONS ==
    
    /**
     * Master permission for all system administration operations.
     * Grants all system-level management permissions.
     */
    @Schema(description = "Master permission for all system administration operations")
    SYSTEM_ADMIN;
    

    /**
     * Generates the Spring Security authority string for this permission.
     * 
     * The authority string is prefixed with "PERMISSION_" followed by the enum name.
     * This format is used by Spring Security for permission-based authorization.
     *
     * @return The Spring Security authority string representation of this permission
     */
    fun getAuthority() = "PERMISSION_$name"

    companion object {
        /**
         * Gets all the permissions included in a high-level permission.
         * Used for permission inheritance and expansion.
         * 
         * @param permission The high-level permission to expand
         * @return List of specific permissions included in the high-level permission
         */
        fun getIncludedPermissions(permission: PermissionType): Set<PermissionType> {
            return when (permission) {
                // User management hierarchy
                MANAGE_USERS -> setOf(
                    MANAGE_USERS, VIEW_USER, CREATE_USER, EDIT_USER, DELETE_USER,
                    APPROVE_USER, RESET_USER_PASSWORD, MANAGE_USER_ROLES
                )
                MANAGE_USER_ROLES -> setOf(MANAGE_USER_ROLES, VIEW_USER)
                
                // Citizen management hierarchy  
                MANAGE_CITIZENS -> setOf(
                    MANAGE_CITIZENS, VIEW_CITIZEN, CREATE_CITIZEN, EDIT_CITIZEN,
                )
                
                // System admin hierarchy
                SYSTEM_ADMIN -> setOf(
                    SYSTEM_ADMIN, MANAGE_USERS, MANAGE_CITIZENS
                )
                
                // Single permissions only include themselves
                else -> setOf(permission)
            }
        }
        
        /**
         * Returns the domain/group a permission belongs to.
         * 
         * @param permission The permission to categorize
         * @return The permission group name
         */
        fun getDomain(permission: PermissionType): String {
            return when (permission) {
                // User permissions
                MANAGE_USERS, VIEW_USER, CREATE_USER, EDIT_USER, DELETE_USER, 
                APPROVE_USER, RESET_USER_PASSWORD, MANAGE_USER_ROLES -> "User Management"
                
                // Citizen permissions
                MANAGE_CITIZENS, VIEW_CITIZEN, CREATE_CITIZEN, EDIT_CITIZEN,
                DELETE_CITIZEN, APPROVE_CITIZEN -> "Citizen Management"
                
                // System permissions
                SYSTEM_ADMIN -> "System Administration"
            }
        }
        
        /**
         * Checks if a permission effectively grants another permission through inheritance.
         * 
         * @param held The permission that is held
         * @param required The permission that is required
         * @return true if the held permission grants the required permission
         */
        fun implies(held: PermissionType, required: PermissionType): Boolean {
            return getIncludedPermissions(held).contains(required)
        }
    }
}
