package np.likhupikemun.dpis.auth.security

import np.likhupikemun.dpis.auth.domain.entity.User
import org.springframework.security.core.userdetails.UserDetails
import java.time.Duration

class TestJwtService : JwtService {
    private val validTokens = mutableMapOf<String, String>() // Map token to email
    private val blacklistedTokens = mutableSetOf<String>()
    private val resetTokens = mutableMapOf<String, Pair<String, Long>>() // Map for reset tokens

    override fun extractUsername(token: String): String? = 
        if (isValidTestToken(token)) validTokens[token] else null

    override fun extractEmail(token: String): String =
        validTokens[token] ?: resetTokens[token]?.first
        ?: throw IllegalStateException("Invalid token")

    override fun generateToken(userDetails: UserDetails): String =
        createAndStoreTestToken(userDetails.username)

    override fun generateToken(user: User): String {
        val token = createAndStoreTestToken(user.email!!)
        resetTokens[token] = Pair(user.email!!, System.currentTimeMillis() + 15 * 60 * 1000)
        validTokens[token] = user.email!!
        return token
    }

    override fun generateRefreshToken(user: User): String =
        createAndStoreTestToken(user.email!!)

    override fun generateExpiredToken(user: User): String = "expired-token"

    override fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long
    ): String = createAndStoreTestToken(userDetails.username)

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
        isValidTestToken(token)

    override fun validateToken(token: String): Boolean {
        val isValid = token in validTokens || resetTokens.containsKey(token)
        // Check if token is in resetTokens and not expired
        if (resetTokens.containsKey(token)) {
            val (_, expiry) = resetTokens[token]!!
            return isValid && System.currentTimeMillis() < expiry
        }
        return isValid
    }

    override fun getTokenExpirationTime(): Long = 3600L

    override fun generateTokenPair(user: User): TokenPair {
        val accessToken = createAndStoreTestToken(user.email!!)
        val refreshToken = createAndStoreTestToken(user.email!!)
        
        // Store user's permissions in validTokens map
        validTokens[accessToken] = user.email!!
        validTokens[refreshToken] = user.email!!

        return TokenPair(accessToken, refreshToken, 3600L)
    }

    override fun invalidateToken(token: String) {
        validTokens.remove(token)
        blacklistedTokens.add(token)
    }

    override fun getExpirationDuration(): Duration = Duration.ofHours(1)

    private fun createAndStoreTestToken(email: String): String {
        val token = "test-token-${System.currentTimeMillis()}"
        validTokens[token] = email
        return token
    }

    private fun isValidTestToken(token: String): Boolean {
        val inValidTokens = token in validTokens && token !in blacklistedTokens
        val inResetTokens = resetTokens[token]?.let { (_, expiry) ->
            System.currentTimeMillis() < expiry
        } ?: false
        return inValidTokens || inResetTokens
    }

    // New method to help with testing
    fun getResetTokenForEmail(email: String): String {
        // Remove any existing reset tokens for this email
        resetTokens.entries.removeIf { it.value.first == email }
        // Generate new token
        val token = "test-token-${System.currentTimeMillis()}"
        resetTokens[token] = Pair(email, System.currentTimeMillis() + 15 * 60 * 1000)
        validTokens[token] = email  // Also store in validTokens
        return token
    }
}
