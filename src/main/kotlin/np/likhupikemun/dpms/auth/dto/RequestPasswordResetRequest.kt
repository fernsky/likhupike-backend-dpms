package np.likhupikemun.dpms.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

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
