package np.likhupikemun.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(
    description = "Request payload for user authentication",
    title = "Login Request",
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
