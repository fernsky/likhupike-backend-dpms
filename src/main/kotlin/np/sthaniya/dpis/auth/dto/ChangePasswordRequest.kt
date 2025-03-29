package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

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
    fun isValid(): Boolean = newPassword == confirmPassword && newPassword != currentPassword
}
