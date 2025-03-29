package np.sthaniya.dpis.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import np.sthaniya.dpis.common.validation.PasswordMatch
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data Transfer Object (DTO) for administrative password reset requests.
 *
 * This class handles validation and transport of administrative password reset data,
 * used by administrators to reset user passwords without requiring the current password
 * or OTP verification.
 *
 * Features:
 * - Password strength validation
 * - Password confirmation
 * - Custom validation using @PasswordMatch
 * - OpenAPI/Swagger documentation
 *
 * Usage with [UserService]:
 * ```kotlin
 * val request = ResetUserPasswordRequest(
 *     newPassword = "NewSecurePass123!",
 *     confirmPassword = "NewSecurePass123!"
 * )
 * userService.resetPassword(userId, request)
 * ```
 *
 * Security:
 * - Requires RESET_USER_PASSWORD permission
 * - Strong password requirements
 * - Password confirmation validation
 * - Audit logging of password resets
 *
 * @property newPassword The new password to set for the user
 * @property confirmPassword Confirmation of the new password
 */
@Schema(
    description = "Request payload for resetting user password by admin",
    title = "Reset User Password Request",
    requiredProperties = ["newPassword", "confirmPassword"]
)
@PasswordMatch // Add custom validation annotation
data class ResetUserPasswordRequest(
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
