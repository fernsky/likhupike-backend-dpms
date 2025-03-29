package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Response payload for successful user registration",
    title = "Register Response"
)
data class RegisterResponse(
    @Schema(
        description = "Email address of the registered user",
        example = "user@example.com",
        required = true
    )
    val email: String,

    @Schema(
        description = "Registration confirmation message",
        example = "Your registration is pending approval. You will be able to login once an administrator approves your account.",
        defaultValue = "Your registration is pending approval. You will be able to login once an administrator approves your account."
    )
    val message: String = "Your registration is pending approval. You will be able to login once an administrator approves your account."
)
