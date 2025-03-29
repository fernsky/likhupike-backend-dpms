package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
/**
 * Data Transfer Object (DTO) representing the response after successful user registration.
 *
 * This DTO provides confirmation details for newly registered users, including:
 * - Email address of the registered account
 * - Status message explaining next steps
 *
 * Features:
 * - Simple confirmation response
 * - Informative message about approval process
 * - OpenAPI/Swagger documentation
 *
 * Usage in [AuthController]:
 * ```kotlin
 * val response = RegisterResponse(
 *     email = request.email,
 *     message = "Your registration is pending approval..."
 * )
 * return ApiResponse.success(data = response)
 * ```
 *
 * Integration:
 * - Used by [AuthService.register] method
 * - Returned to client after successful registration
 * - Part of registration workflow
 *
 * @property email The email address of the registered user
 * @property message Informative message about pending approval
 */
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
