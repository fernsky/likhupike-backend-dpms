package np.sthaniya.dpis.auth.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Enumeration of system-wide permissions used for role-based access control (RBAC).
 *
 * This enum defines all available permissions in the Digital Profile Information System.
 * Each permission represents a specific operation that can be granted to users.
 * The permissions are used in conjunction with Spring Security for authorization.
 *
 * Features:
 * - OpenAPI documentation integration via Schema annotations
 * - Spring Security authority string generation
 * - Hierarchical permission structure for user management
 *
 * Usage:
 * ```kotlin
 * if (user.hasPermission(PermissionType.CREATE_USER)) {
 *     // Perform user creation
 * }
 * ```
 */
@Schema(
    description = "System permission types for role-based access control",
    title = "Permission Types"
)
enum class PermissionType {
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
     * Permission to view user information.
     * Basic permission for accessing user details.
     */
    @Schema(description = "Permission to view user information")
    VIEW_USER,

    /**
     * Permission to reset user passwords.
     * Required for password management and recovery operations.
     */
    @Schema(description = "Permission to reset user passwords")
    RESET_USER_PASSWORD,
    ;

    /**
     * Generates the Spring Security authority string for this permission.
     * 
     * The authority string is prefixed with "PERMISSION_" followed by the enum name.
     * This format is used by Spring Security for permission-based authorization.
     *
     * Example: PermissionType.CREATE_USER.getAuthority() returns "PERMISSION_CREATE_USER"
     *
     * @return The Spring Security authority string representation of this permission
     */
    fun getAuthority() = "PERMISSION_$name"
}
