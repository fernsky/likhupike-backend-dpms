package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
/**
 * Data Transfer Object (DTO) for handling password change requests from authenticated users.
 *
 * This class handles the validation and transport of password change data, including:
 * - Current password verification
 * - New password validation
 * - Password confirmation matching
 *
 * Password Requirements:
 * - Minimum 8 characters
 * - At least one digit (0-9)
 * - At least one lowercase letter (a-z)
 * - At least one uppercase letter (A-Z)
 * - At least one special character (@#$%^&+=)
 *
 * Security Features:
 * - Input validation using Jakarta Validation
 * - Password strength enforcement
 * - Password confirmation check
 * - Prevention of password reuse
 *
 * Usage:
 * ```kotlin
 * val request = ChangePasswordRequest(
 *     currentPassword = "OldPass123!",
 *     newPassword = "NewPass456@",
 *     confirmPassword = "NewPass456@"
 * )
 * if (request.isValid()) {
 *     // Process password change
 * }
 * ```
 *
 * @property currentPassword The user's current password for verification
 * @property newPassword The desired new password meeting security requirements
 * @property confirmPassword Confirmation of the new password to prevent typos
 */
@Schema(
    description = "Request payload for changing user password",
    title = "Change Password Request",
    requiredProperties = ["currentPassword", "newPassword", "confirmPassword"]
)
data class ChangePasswordRequest(
    @Schema(
        description = "User's current password",
        example = "CurrentPass123!",
        required = true
    )
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,

    @Schema(
        description = """
            New password that meets the following criteria:
            - At least 8 characters long
            - Contains at least one digit
            - Contains at least one lowercase letter
            - Contains at least one uppercase letter
            - Contains at least one special character (@#$%^&+=)
        """,
        example = "NewPass123!",
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
        example = "NewPass123!",
        required = true
    )
    @field:NotBlank(message = "Password confirmation is required")
    val confirmPassword: String
) {
    /**
     * Validates the password change request.
     *
     * Performs the following checks:
     * 1. New password matches confirmation password
     * 2. New password is different from current password
     *
     * Note: Password complexity requirements are handled by field validation annotations.
     *
     * @return true if the request is valid, false otherwise
     */
    fun isValid(): Boolean = newPassword == confirmPassword && newPassword != currentPassword
}
