package np.likhupikemun.dpis.auth.service

import np.likhupikemun.dpis.auth.dto.*
import java.util.UUID

interface AuthService {
    fun register(request: RegisterRequest): RegisterResponse
    fun login(request: LoginRequest): AuthResponse
    fun refreshToken(refreshToken: String): AuthResponse
    fun logout(token: String)
    fun requestPasswordReset(request: RequestPasswordResetRequest)
    fun resetPassword(request: ResetPasswordRequest)
    fun changePassword(currentUserId: UUID, request: ChangePasswordRequest)
}
