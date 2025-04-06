package np.sthaniya.dpis.citizen.dto.profile

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import np.sthaniya.dpis.common.validation.ValidPassword

/**
 * Data transfer object for citizen password change requests.
 */
@Schema(
    description = "Request payload for changing citizen password",
    title = "Change Password Request",
    requiredProperties = ["currentPassword", "newPassword", "confirmPassword"]
)
data class ChangePasswordDto(
    @Schema(
        description = "Current password for verification",
        example = "CurrentP@ss123",
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
        example = "NewStrongP@ss123",
        required = true
    )
    @field:NotBlank(message = "New password is required")
    @field:ValidPassword
    val newPassword: String,
    
    @Schema(
        description = "Confirmation of the new password - must match the new password field",
        example = "NewStrongP@ss123",
        required = true
    )
    @field:NotBlank(message = "Confirm password is required")
    val confirmPassword: String
) {
    /**
     * Validates that new password and confirmation match.
     * @return true if passwords match, false otherwise
     */
    @AssertTrue(message = "New password and confirmation do not match")
    fun isPasswordMatchValid(): Boolean = newPassword == confirmPassword
    
    /**
     * Validates that new password is different from the current password.
     * @return true if new password is different from current password
     */
    @AssertTrue(message = "New password must be different from current password")
    fun isNewPasswordDifferent(): Boolean = newPassword != currentPassword
}