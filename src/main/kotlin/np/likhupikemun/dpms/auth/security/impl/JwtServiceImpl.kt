package np.likhupikemun.dpms.auth.security.impl

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.security.JwtService
import np.likhupikemun.dpms.auth.security.TokenPair
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class JwtServiceImpl(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
    @Value("\${jwt.expiration}")
    private val jwtExpiration: Long,
    @Value("\${jwt.refresh-expiration}")
    private val refreshExpiration: Long
) : JwtService {

    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    private val blacklistedTokens = Collections.synchronizedSet(HashSet<String>())

    override fun extractUsername(token: String): String? = 
        extractClaim(token) { it.subject }

    override fun extractEmail(token: String): String = 
        extractUsername(token) ?: throw IllegalStateException("Token has no subject claim")

    override fun generateToken(userDetails: UserDetails): String = 
        generateToken(mapOf(), userDetails, jwtExpiration)

    override fun generateToken(user: User): String =
        generateToken(
            mapOf(
                "email" to (user.email ?: ""),
                "permissions" to user.getAuthorities().map { it.authority }
            ),
            user,
            jwtExpiration
        )

    override fun generateRefreshToken(user: User): String =
        generateToken(
            mapOf(
                "email" to (user.email ?: ""),
                "type" to "refresh"
            ),
            user,
            refreshExpiration
        )

    override fun generateExpiredToken(user: User): String =
        generateToken(
            mapOf(
                "email" to (user.email ?: ""),
                "type" to "refresh"
            ),
            user,
            -1L  // Negative expiration means token is already expired
        )

    override fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long
    ): String =
        Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(Duration.ofHours(expiration))))
            .signWith(key)
            .compact()

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token) && !isTokenBlacklisted(token)
    }

    override fun validateToken(token: String): Boolean =
        try {
            !isTokenExpired(token) && !isTokenBlacklisted(token)
        } catch (e: Exception) {
            false
        }

    override fun getTokenExpirationTime(): Long = jwtExpiration

    override fun generateTokenPair(user: User): TokenPair {
        val accessToken = generateToken(user)
        val refreshToken = generateRefreshToken(user)
        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtExpiration
        )
    }

    override fun invalidateToken(token: String) {
        blacklistedTokens.add(token)
        // Clean up expired tokens from blacklist
        val now = Date()
        blacklistedTokens.removeIf { tok ->
            extractExpiration(tok)?.before(now) ?: true
        }
    }

    override fun getExpirationDuration(): Duration = Duration.ofHours(jwtExpiration)

    private fun isTokenExpired(token: String): Boolean = 
        extractExpiration(token)?.before(Date()) ?: true

    private fun isTokenBlacklisted(token: String): Boolean =
        blacklistedTokens.contains(token)

    private fun extractExpiration(token: String): Date? = 
        extractClaim(token) { it.expiration }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T? =
        try {
            extractAllClaims(token)?.let(claimsResolver)
        } catch (e: Exception) {
            null
        }

    private fun extractAllClaims(token: String): Claims? =
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            null
        }
}
