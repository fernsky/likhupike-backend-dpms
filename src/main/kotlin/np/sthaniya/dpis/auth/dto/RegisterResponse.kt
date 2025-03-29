package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Response object for successful user registration operations.
 * 
 * Used by [AuthService.register] to return registration confirmation details
 * to the client. Contains the registered email and a status message about
 * the approval process.
 *
 * @property email The email address that was registered
 * @property message Description of next steps in registration process
 * @see AuthController.register
 */
@Schema(description = "Response payload for successful user registration")
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
