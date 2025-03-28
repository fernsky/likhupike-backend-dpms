package np.likhupikemun.dpms.auth.domain.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "System permission types for role-based access control",
    title = "Permission Types"
)
enum class PermissionType {
    @Schema(description = "Permission to create new users in the system")
    CREATE_USER,

    @Schema(description = "Permission to approve user registrations or account changes")
    APPROVE_USER,

    @Schema(description = "Permission to modify existing user information")
    EDIT_USER,

    @Schema(description = "Permission to remove users from the system")
    DELETE_USER,

    @Schema(description = "Permission to view user information")
    VIEW_USER,

    @Schema(description = "Permission to reset user passwords")
    RESET_USER_PASSWORD,
    ;

    fun getAuthority() = "PERMISSION_$name"
}