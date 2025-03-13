package np.likhupikemun.dpms.auth.service

import np.likhupikemun.dpms.auth.dto.AuthResponse
import np.likhupikemun.dpms.auth.dto.LoginRequest
import np.likhupikemun.dpms.auth.dto.RegisterRequest
import np.likhupikemun.dpms.auth.dto.ResetPasswordRequest

interface AuthService {
    fun register(request: RegisterRequest): AuthResponse
    fun login(request: LoginRequest): AuthResponse
    fun refreshToken(refreshToken: String): AuthResponse
    fun logout(token: String)
    fun resetPassword(request: ResetPasswordRequest)
}
