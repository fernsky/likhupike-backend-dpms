package np.likhupikemun.dpms.auth.service

import np.likhupikemun.dpms.auth.dto.*

interface AuthService {
    fun register(request: RegisterRequest): RegisterResponse
    fun login(request: LoginRequest): AuthResponse
    fun refreshToken(refreshToken: String): AuthResponse
    fun logout(token: String)
    fun requestPasswordReset(request: RequestPasswordResetRequest)
    fun resetPassword(request: ResetPasswordRequest)
}
