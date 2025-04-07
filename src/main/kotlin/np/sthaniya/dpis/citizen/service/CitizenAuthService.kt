package np.sthaniya.dpis.citizen.service

import np.sthaniya.dpis.citizen.dto.auth.*
import java.util.UUID

/**
 * Service interface for handling citizen authentication operations.
 *
 * This interface provides methods for citizen authentication, password management,
 * and token handling operations.
 */
interface CitizenAuthService {
    
    /**
     * Authenticates a citizen and generates access tokens.
     *
     * @param request The login credentials
     * @return [CitizenAuthResponse] containing access and refresh tokens
     */
    fun login(request: CitizenLoginRequest): CitizenAuthResponse

    /**
     * Generates new access tokens using a refresh token.
     *
     * @param refreshToken The refresh token to use for generating new access tokens
     * @return [CitizenAuthResponse] containing new access and refresh tokens
     */
    fun refreshToken(refreshToken: String): CitizenAuthResponse

    /**
     * Invalidates the given authentication token.
     *
     * @param token The token to invalidate
     */
    fun logout(token: String)

    /**
     * Initiates the password reset process for a citizen.
     *
     * @param request The password reset request containing user identification
     */
    fun requestPasswordReset(request: CitizenRequestPasswordResetRequest)

    /**
     * Completes the password reset process.
     *
     * @param request The reset password request containing the new password and reset token
     */
    fun resetPassword(request: CitizenResetPasswordRequest)

    /**
     * Changes the password for the currently authenticated citizen.
     *
     * @param citizenId The ID of the citizen changing their password
     * @param request The change password request containing the old and new passwords
     */
    fun changePassword(citizenId: UUID, request: CitizenChangePasswordRequest)
}
