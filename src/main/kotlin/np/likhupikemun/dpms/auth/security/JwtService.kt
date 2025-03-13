package np.likhupikemun.dpms.auth.security

import np.likhupikemun.dpms.auth.domain.entity.User
import org.springframework.security.core.userdetails.UserDetails

interface JwtService {
    fun extractUsername(token: String): String?
    fun extractEmail(token: String): String
    fun generateToken(userDetails: UserDetails): String
    fun generateToken(user: User): String
    fun generateRefreshToken(user: User): String
    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails, expiration: Long): String
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
    fun validateToken(token: String): Boolean
    fun getTokenExpirationTime(): Long
    fun generateTokenPair(user: User): TokenPair
    fun invalidateToken(token: String)
}
