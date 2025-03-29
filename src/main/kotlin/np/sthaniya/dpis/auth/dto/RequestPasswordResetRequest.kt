package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Data transfer object for initiating the password reset workflow.
 * 
 * Used with [AuthService.requestPasswordReset] to trigger OTP generation
 * and delivery for password reset operations.
 *
 * @property email User's registered email address
 * @throws AuthException.UserNotFoundException if email is not found
 * @throws AuthException.TooManyRequestsException if rate limit exceeded
 * @see PasswordResetOtp for OTP entity
 * @see ResetPasswordRequest for OTP verification and password reset
 */
@Schema(
    description = "Request payload for initiating a password reset",
    title = "Password Reset Request",
    requiredProperties = ["email"]
)
data class RequestPasswordResetRequest(
    @Schema(
        description = "Email address of the account that needs password reset",
        example = "user@example.com",
        required = true
    )
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Please provide a valid email address")
    val email: String
)
