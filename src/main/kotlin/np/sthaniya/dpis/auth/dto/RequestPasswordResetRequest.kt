package np.sthaniya.dpis.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Data Transfer Object (DTO) for initiating a password reset request.
 *
 * This class handles the validation and transport of password reset requests,
 * triggering the OTP-based password reset flow through [AuthService].
 *
 * Features:
 * - Email validation
 * - Integration with password reset flow
 * - Rate limiting support
 * - Security validation
 *
 * Password Reset Flow:
 * 1. User submits email
 * 2. System validates email existence
 * 3. OTP generated and sent via email
 * 4. User receives OTP for reset process
 *
 * Usage with [AuthController]:
 * ```kotlin
 * val request = RequestPasswordResetRequest(
 *     email = "user@example.com"
 * )
 * authService.requestPasswordReset(request)
 * ```
 *
 * Security:
 * - Rate limited to prevent abuse
 * - Email existence not disclosed
 * - OTP expiration enforced
 *
 * @property email Email address of the account requiring password reset
 */
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
