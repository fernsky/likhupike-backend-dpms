package np.sthaniya.dpis.citizen.dto.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import np.sthaniya.dpis.citizen.domain.entity.CitizenState

/**
 * DTO for citizen login requests
 *
 * @property email The citizen's email address (username)
 * @property password The citizen's password
 */
data class CitizenLoginRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String
)

/**
 * DTO for the response from successful authentication
 *
 * @property token Access token for API authentication
 * @property refreshToken Token used to obtain a new access token
 * @property citizenId ID of the authenticated citizen
 * @property email Email of the authenticated citizen
 * @property expiresIn Access token expiration time in seconds
 * @property state Current state of the citizen's profile
 */
data class CitizenAuthResponse(
    val token: String,
    val refreshToken: String,
    val citizenId: String,
    val email: String,
    val expiresIn: Long,
    val state: CitizenState
)

/**
 * DTO for token refresh requests
 *
 * @property refreshToken The refresh token to use for generating a new token pair
 */
data class CitizenRefreshTokenRequest(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String
)

/**
 * DTO for password reset requests
 *
 * @property email Email address for which to reset the password
 */
data class CitizenRequestPasswordResetRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String
)

/**
 * DTO for completing a password reset
 *
 * @property email Email address of the citizen
 * @property otp One-time password sent via email
 * @property newPassword New password to set
 * @property confirmPassword Confirmation of the new password
 */
data class CitizenResetPasswordRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "OTP is required")
    @field:Size(min = 6, max = 6, message = "OTP must be 6 digits")
    val otp: String,

    @field:NotBlank(message = "New password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val newPassword: String,

    @field:NotBlank(message = "Password confirmation is required")
    val confirmPassword: String
) {
    @JsonIgnore
    fun passwordsMatch() = newPassword == confirmPassword
}

/**
 * DTO for changing an existing password
 *
 * @property currentPassword The citizen's current password
 * @property newPassword The new password to set
 * @property confirmPassword Confirmation of the new password
 */
data class CitizenChangePasswordRequest(
    @JsonProperty("currentPassword")
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,

    @JsonProperty("newPassword")
    @field:NotBlank(message = "New password is required")
    @field:Size(min = 8, message = "New password must be at least 8 characters")
    val newPassword: String,

    @JsonProperty("confirmPassword")
    @field:NotBlank(message = "Confirm password is required")
    val confirmPassword: String
) {
    @JsonIgnore
    fun isValid(): Boolean = newPassword == confirmPassword
}
