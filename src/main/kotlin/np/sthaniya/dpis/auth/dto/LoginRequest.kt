package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Data transfer object for authentication requests.
 * 
 * Used by [AuthService.login] to process user authentication attempts. 
 * Implements validation using Jakarta Validation constraints.
 *
 * @property email User's email address, must be in valid email format
 * @property password User's plaintext password for authentication
 * 
 * @throws AuthException.InvalidCredentialsException if credentials are incorrect
 * @throws AuthException.AccountNotApprovedException if account pending approval
 * @throws AuthException.AccountDeletedException if account is deleted
 * @see AuthResponse for authentication response data
 */
@Schema(
    description = "Request payload for user authentication",
    requiredProperties = ["email", "password"]
)
data class LoginRequest(
    @Schema(
        description = "User's email address",
        example = "user@example.com",
        required = true
    )
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Please provide a valid email address")
    val email: String,

    @Schema(
        description = "User's password",
        example = "StrongP@ss123",
        required = true,
        format = "password"
    )
    @field:NotBlank(message = "Password is required")
    val password: String
)
