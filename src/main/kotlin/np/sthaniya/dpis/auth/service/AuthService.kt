package np.sthaniya.dpis.auth.service

import np.sthaniya.dpis.auth.dto.*
import java.util.UUID
/**
 * Service interface for handling authentication and user management operations.
 *
 * This interface provides methods for user registration, authentication, password management,
 * and token handling operations.
 */
interface AuthService {
    /**
     * Registers a new user in the system.
     *
     * @param request The registration details containing user information
     * @return [RegisterResponse] containing the registration result
     */
    fun register(request: RegisterRequest): RegisterResponse

    /**
     * Authenticates a user and generates access tokens.
     *
     * @param request The login credentials
     * @return [AuthResponse] containing access and refresh tokens
     */
    fun login(request: LoginRequest): AuthResponse

    /**
     * Generates new access tokens using a refresh token.
     *
     * @param refreshToken The refresh token to use for generating new access tokens
     * @return [AuthResponse] containing new access and refresh tokens
     */
    fun refreshToken(refreshToken: String): AuthResponse

    /**
     * Invalidates the given authentication token.
     *
     * @param token The token to invalidate
     */
    fun logout(token: String)

    /**
     * Initiates the password reset process for a user.
     *
     * @param request The password reset request containing user identification
     */
    fun requestPasswordReset(request: RequestPasswordResetRequest)

    /**
     * Completes the password reset process.
     *
     * @param request The reset password request containing the new password and reset token
     */
    fun resetPassword(request: ResetPasswordRequest)

    /**
     * Changes the password for the currently authenticated user.
     *
     * @param currentUserId The ID of the user changing their password
     * @param request The change password request containing the old and new passwords
     */
    fun changePassword(currentUserId: UUID, request: ChangePasswordRequest)
}
