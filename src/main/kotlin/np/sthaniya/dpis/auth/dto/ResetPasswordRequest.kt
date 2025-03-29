package np.sthaniya.dpis.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Email
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data transfer object for OTP-based password reset operations.
 *
 * Encapsulates the data required for password reset verification and execution,
 * with validation constraints enforced through Jakarta Validation annotations.
 * Used in conjunction with [AuthService.resetPassword].
 *
 * @property email Email address associated with the password reset request
 * @property otp One-time password received via email, must be 6 digits
 * @property newPassword New password to set, must meet complexity requirements
 * @property confirmPassword Confirmation of new password, must match newPassword
 *
 * @throws AuthException.InvalidOtpException if OTP is invalid or expired
 * @throws AuthException.TooManyAttemptsException if max OTP attempts exceeded
 * @throws AuthException.UserNotFoundException if email not found
 * @throws jakarta.validation.ConstraintViolationException if validation fails
 */
@Schema(
    description = "Request payload for resetting user password with OTP",
    title = "Reset Password Request",
    requiredProperties = ["email", "otp", "newPassword", "confirmPassword"]
)
data class ResetPasswordRequest(
    @Schema(
        description = "Email address of the account",
        example = "user@example.com",
        required = true
    )
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Please provide a valid email address")
    val email: String,

    @Schema(
        description = "One-time password (OTP) received via email",
        example = "123456",
        required = true,
        pattern = "^[0-9]{6}$"
    )
    @field:NotBlank(message = "OTP is required")
    @field:Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits")
    val otp: String,

    @Schema(
        description = """
            New password that meets the following criteria:
            - At least 8 characters long
            - Contains at least one digit
            - Contains at least one lowercase letter
            - Contains at least one uppercase letter
            - Contains at least one special character (@#$%^&+=)
        """,
        example = "NewP@ss123",
        required = true
    )
    @field:NotBlank(message = "New password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
        message = "Password must contain at least one digit, one lowercase, one uppercase letter and one special character"
    )
    val newPassword: String,

    @Schema(
        description = "Confirmation of the new password - must match newPassword",
        example = "NewP@ss123",
        required = true
    )
    @field:NotBlank(message = "Password confirmation is required")
    val confirmPassword: String
)
