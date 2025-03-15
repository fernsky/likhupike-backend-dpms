package np.likhupikemun.dpms.auth.security

import np.likhupikemun.dpms.auth.domain.entity.User
import org.springframework.security.core.userdetails.UserDetails
import java.time.Duration

class TestJwtService : JwtService {
    private val validTokens = mutableSetOf<String>()
    private val blacklistedTokens = mutableSetOf<String>()

    override fun extractUsername(token: String): String? = 
        if (isValidTestToken(token)) "test@test.com" else null

    override fun extractEmail(token: String): String =
        extractUsername(token) ?: throw IllegalStateException("Invalid token")

    override fun generateToken(userDetails: UserDetails): String =
        createAndStoreTestToken()

    override fun generateToken(user: User): String =
        createAndStoreTestToken()

    override fun generateRefreshToken(user: User): String =
        createAndStoreTestToken()

    override fun generateExpiredToken(user: User): String = "expired-token"

    override fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long
    ): String = createAndStoreTestToken()

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
        isValidTestToken(token)

    override fun validateToken(token: String): Boolean =
        isValidTestToken(token)

    override fun getTokenExpirationTime(): Long = 3600L

    override fun generateTokenPair(user: User): TokenPair {
        val accessToken = createAndStoreTestToken()
        val refreshToken = createAndStoreTestToken()
        return TokenPair(accessToken, refreshToken, 3600L)
    }

    override fun invalidateToken(token: String) {
        validTokens.remove(token)
        blacklistedTokens.add(token)
    }

    override fun getExpirationDuration(): Duration = Duration.ofHours(1)

    private fun createAndStoreTestToken(): String {
        val token = "test-token-${System.currentTimeMillis()}"
        validTokens.add(token)
        return token
    }

    private fun isValidTestToken(token: String): Boolean =
        token in validTokens && token !in blacklistedTokens
}
