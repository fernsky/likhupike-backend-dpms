package np.likhupikemun.dpms.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import java.time.LocalDateTime
import java.util.UUID

@Schema(
    description = "Response payload containing user information",
    title = "User Response"
)
data class UserResponse(
    @Schema(
        description = "Unique identifier of the user",
        example = "550e8400-e29b-41d4-a716-446655440000",
        required = true
    )
    val id: UUID,

    @Schema(
        description = "Email address of the user",
        example = "user@example.com",
        required = true
    )
    val email: String,

    @Schema(
        description = "Set of permissions granted to the user",
        example = "[\"CREATE_USER\", \"VIEW_USER\"]",
        required = true
    )
    val permissions: Set<PermissionType>,

    @Schema(
        description = "Indicates if the user has ward-level access",
        example = "false",
        required = true
    )
    val isWardLevelUser: Boolean,

    @Schema(
        description = "Ward number (1-33) if user has ward-level access",
        example = "5",
        nullable = true
    )
    val wardNumber: Int?,

    @Schema(
        description = "Indicates if the user account is approved",
        example = "true",
        required = true
    )
    val isApproved: Boolean,

    @Schema(
        description = "UUID of the user who approved this account",
        example = "550e8400-e29b-41d4-a716-446655440000",
        nullable = true
    )
    val approvedBy: UUID?,

    @Schema(
        description = "Timestamp when the account was approved",
        example = "2024-03-15T10:30:00",
        nullable = true
    )
    val approvedAt: LocalDateTime?,

    @Schema(
        description = "Timestamp when the account was created",
        example = "2024-03-15T10:30:00",
        required = true
    )
    val createdAt: LocalDateTime,

    @Schema(
        description = "Timestamp when the account was last updated",
        example = "2024-03-15T10:30:00",
        nullable = true
    )
    val updatedAt: LocalDateTime?,
)
