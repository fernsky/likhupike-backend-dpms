package np.sthaniya.dpis.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.domain.enums.RoleType
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Response object for successful authentication operations.
 * 
 * Contains JWT tokens and user profile data returned after successful authentication
 * through [AuthService.login] or token refresh through [AuthService.refreshToken].
 *
 * @property token JWT access token for API authentication
 * @property refreshToken JWT refresh token for obtaining new access tokens
 * @property userId Unique identifier of the authenticated user
 * @property email Email address of the authenticated user
 * @property permissions Set of [PermissionType] granted to the user
 * @property roles Set of [RoleType] assigned to the user
 * @property expiresIn Token validity duration in seconds
 * @property isWardLevelUser Indicates if user has ward-level access restrictions
 * @property wardNumber Ward number if user has ward-level access, null otherwise
 *
 * @see JwtService for token generation details
 * @see AuthController.login for usage in authentication flow
 */
@Schema(description = "Authentication response containing tokens and user information")
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
        example = "[\"VIEW_USER\", \"CREATE_USER\"]",
        required = true
    )
    val permissions: Set<PermissionType>,
    
    @Schema(
        description = "List of roles assigned to the user",
        example = "[\"SYSTEM_ADMINISTRATOR\", \"LAND_RECORDS_OFFICER\"]",
        required = true
    )
    val roles: Set<RoleType>,

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
