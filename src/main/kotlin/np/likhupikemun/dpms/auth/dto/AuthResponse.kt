package np.likhupikemun.dpms.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Authentication response containing tokens and user information",
    title = "Authentication Response"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthResponse(
    @Schema(
        description = "JWT access token for API authentication",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        required = true
    )
    val token: String,

    @Schema(
        description = "JWT refresh token for obtaining new access tokens",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        required = true
    )
    val refreshToken: String,

    @Schema(
        description = "Unique identifier of the authenticated user",
        example = "550e8400-e29b-41d4-a716-446655440000",
        required = true
    )
    val userId: String,

    @Schema(
        description = "Email address of the authenticated user",
        example = "user@example.com",
        required = true
    )
    val email: String,

    @Schema(
        description = "List of permissions granted to the user",
        example = "[\"USER_READ\", \"USER_WRITE\"]",
        required = true
    )
    val permissions: Set<PermissionType>,

    @Schema(
        description = "Token expiration time in seconds",
        example = "3600",
        required = true
    )
    val expiresIn: Long,

    @Schema(
        description = "Indicates if the user has ward-level access",
        example = "false",
        defaultValue = "false"
    )
    val isWardLevelUser: Boolean = false,

    @Schema(
        description = "Ward number if user has ward-level access",
        example = "5",
        nullable = true
    )
    val wardNumber: Int? = null
)
