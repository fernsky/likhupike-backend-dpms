package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Data Transfer Object (DTO) for user authentication requests.
 *
 * This class handles login request validation and data transport for the authentication process.
 * It is used in conjunction with [AuthService] to process user login attempts.
 *
 * Features:
 * - Email format validation
 * - Required field validation
 * - OpenAPI/Swagger documentation
 * - Security input validation
 *
 * Usage with [AuthController]:
 * ```kotlin
 * val loginRequest = LoginRequest(
 *     email = "user@example.com",
 *     password = "userPassword123!"
 * )
 * authService.login(loginRequest)
 * ```
 *
 * Security:
 * - Password field is marked as password format in OpenAPI
 * - Input validation prevents empty/malformed data
 * - Rate limiting applied at service level
 *
 * @property email User's email address for authentication
 * @property password User's password (plain text, will be verified against hashed version)
 */
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
