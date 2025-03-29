package np.likhupikemun.dpis.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import np.likhupikemun.dpis.common.validation.PasswordMatch
import io.swagger.v3.oas.annotations.media.Schema

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
