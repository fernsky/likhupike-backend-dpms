package np.sthaniya.dpis.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Email
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data Transfer Object (DTO) for completing a password reset using OTP.
 *
 * This class handles the validation and transport of password reset data including:
 * - Email verification
 * - OTP validation
 * - New password requirements
 * - Password confirmation
 *
 * Password Reset Flow:
 * 1. User receives OTP via email
 * 2. User submits OTP with new password
 * 3. System validates OTP and password requirements
 * 4. Password is updated if validation succeeds
 *
 * Security Features:
 * - OTP format validation (6 digits)
 * - Strong password requirements
 * - Password confirmation check
 * - Limited OTP validity period
 * - Rate limiting on attempts
 *
 * Usage with [AuthService]:
 * ```kotlin
 * val request = ResetPasswordRequest(
 *     email = "user@example.com",
 *     otp = "123456",
 *     newPassword = "NewPass123!",
 *     confirmPassword = "NewPass123!"
 * )
 * authService.resetPassword(request)
 * ```
 *
 * @property email Email address for account identification
 * @property otp One-time password received via email
 * @property newPassword New password meeting security requirements
 * @property confirmPassword Confirmation of new password
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
