package np.sthaniya.dpis.auth.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Enumeration of system roles used for role-based access control (RBAC).
 *
 * This enum defines all available roles in the Digital Profile Information System.
 * Each role represents a specific position or designation with associated permissions.
 * The roles are derived from user stories and use cases in the land management system.
 *
 * Features:
 * - OpenAPI documentation integration via Schema annotations
 * - Spring Security role string generation
 * - Role-based permission grouping
 *
 * Usage:
 * ```kotlin
 * if (user.hasRole(RoleType.SYSTEM_ADMINISTRATOR)) {
 *     // Perform administrative operation
 * }
 * ```
 */
@Schema(
    description = "System roles for role-based access control",
    title = "Role Types"
)
enum class RoleType {
    /**
     * System Administrator role with highest level of access.
     * Responsible for system configuration, user management, and overall system maintenance.
     */
    @Schema(description = "System Administrator with highest level of access")
    SYSTEM_ADMINISTRATOR,

    /**
     * Land Records Officer role.
     * Government official responsible for processing land registration and transactions.
     */
    @Schema(description = "Government official for land registration and transactions")
    LAND_RECORDS_OFFICER,

    /**
     * Land Surveyor role.
     * Technical staff who conducts field surveys and updates land geometry.
     */
    @Schema(description = "Technical staff for field surveys and geometry updates")
    LAND_SURVEYOR,

    /**
     * Landowner role.
     * Individual or entity that owns registered land.
     */
    @Schema(description = "Individual or entity that owns land")
    LAND_OWNER,

    /**
     * Public User role.
     * General public seeking land information with limited access.
     */
    @Schema(description = "General public user with limited access")
    PUBLIC_USER;

    /**
     * Generates the Spring Security role string for this role.
     * 
     * The role string is prefixed with "ROLE_" followed by the enum name.
     * This format is used by Spring Security for role-based authorization.
     *
     * Example: RoleType.SYSTEM_ADMINISTRATOR.getAuthority() returns "ROLE_SYSTEM_ADMINISTRATOR"
     *
     * @return The Spring Security role string representation
     */
    fun getAuthority() = "ROLE_$name"
}
