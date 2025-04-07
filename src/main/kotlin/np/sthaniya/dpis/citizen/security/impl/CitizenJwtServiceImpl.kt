package np.sthaniya.dpis.citizen.security.impl

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.security.CitizenJwtService
import np.sthaniya.dpis.citizen.security.CitizenTokenPair
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * JWT token management implementation for Citizens using JJWT library with HMAC-SHA256 signing.
 *
 * Threading: Thread-safe via [Collections.synchronizedSet]
 * Memory: O(n) where n = number of blacklisted tokens
 * Performance: O(1) for token operations, O(n) for blacklist cleanup
 *
 * Configuration:
 * ```yaml
 * citizen-jwt:
 *   secret-key: "base64-encoded-key"
 *   expiration: 1 # hours
 *   refresh-expiration: 168 # hours (1 week)
 * ```
 */
@Service
class CitizenJwtServiceImpl(
    @Value("\${citizen-jwt.secret-key}")
    private val secretKey: String,
    @Value("\${citizen-jwt.expiration}")
    private val jwtExpiration: Long,
    @Value("\${citizen-jwt.refresh-expiration}")
    private val refreshExpiration: Long
) : CitizenJwtService {

    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    private val blacklistedTokens = Collections.synchronizedSet(HashSet<String>())

    /**
     * Extracts subject claim from JWT payload.
     * Complexity: O(1)
     * 
     * Technical flow:
     * 1. Parse JWT using HMAC-SHA256
     * 2. Verify signature using secret key
     * 3. Extract and return subject claim
     */
    override fun extractUsername(token: String): String? = 
        extractClaim(token) { it.subject }

    /**
     * Forces username extraction with null safety.
     * Complexity: O(1)
     * 
     * Error handling:
     * - Throws if subject claim missing
     * - Propagates JWT parsing exceptions
     */
    override fun extractEmail(token: String): String = 
        extractUsername(token) ?: throw IllegalStateException("Token has no subject claim")

    /**
     * Creates JWT with default claims.
     * Complexity: O(1)
     * Memory: Creates new JWT string
     */
    override fun generateToken(userDetails: UserDetails): String = 
        generateToken(mapOf(), userDetails, jwtExpiration)

    /**
     * Creates JWT with citizen information.
     * Complexity: O(n) where n = number of authorities
     * 
     * Claims structure:
     * {
     *   "sub": "<email>",
     *   "email": "<email>",
     *   "roles": ["ROLE_CITIZEN"],
     *   "iat": <timestamp>,
     *   "exp": <timestamp>
     * }
     */
    override fun generateToken(citizen: Citizen): String =
        generateToken(
            mapOf(
                "email" to (citizen.email ?: ""),
                "roles" to citizen.getAuthorities().map { it.authority },
                "citizenId" to citizen.id.toString()
            ),
            citizen,
            jwtExpiration
        )

    /**
     * Creates refresh token with minimal claims.
     * Complexity: O(1)
     * 
     * Claims structure:
     * {
     *   "sub": "<email>",
     *   "email": "<email>",
     *   "type": "refresh",
     *   "citizenId": "<uuid>",
     *   "iat": <timestamp>,
     *   "exp": <timestamp + refresh_duration>
     * }
     */
    override fun generateRefreshToken(citizen: Citizen): String =
        generateToken(
            mapOf(
                "email" to (citizen.email ?: ""),
                "type" to "refresh",
                "citizenId" to citizen.id.toString()
            ),
            citizen,
            refreshExpiration
        )

    override fun generateExpiredToken(citizen: Citizen): String =
        generateToken(
            mapOf(
                "email" to (citizen.email ?: ""),
                "type" to "refresh",
                "citizenId" to citizen.id.toString()
            ),
            citizen,
            -1L  // Negative expiration means token is already expired
        )

    /**
     * Low-level token generation with custom claims.
     * Complexity: O(n) where n = size of extraClaims
     * 
     * Technical steps:
     * 1. Build claims set from Map
     * 2. Add standard claims (sub, iat, exp)
     * 3. Sign using HMAC-SHA256
     * 4. Compact into JWT string
     */
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

    /**
     * Validates token for specific citizen.
     * Complexity: O(1) average, O(n) worst case for blacklist check
     * 
     * Validation steps:
     * 1. Extract and compare username
     * 2. Check expiration timestamp
     * 3. Verify against blacklist
     * 4. Verify HMAC signature
     */
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

    override fun generateTokenPair(citizen: Citizen): CitizenTokenPair {
        val accessToken = generateToken(citizen)
        val refreshToken = generateRefreshToken(citizen)
        return CitizenTokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtExpiration
        )
    }

    /**
     * Thread-safe token blacklisting with cleanup.
     * Complexity: O(n) where n = blacklisted tokens
     * 
     * Implementation notes:
     * - Uses synchronized HashSet for thread safety
     * - Performs eager cleanup of expired tokens
     * - Memory grows with number of valid blacklisted tokens
     */
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

    /**
     * Extracts JWT claims with error handling.
     * Complexity: O(1)
     * 
     * Technical flow:
     * 1. Parse JWT string
     * 2. Verify HMAC signature
     * 3. Extract claims object
     * 4. Apply claims resolver function
     * 5. Handle parsing/validation exceptions
     */
    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T? =
        try {
            extractAllClaims(token)?.let(claimsResolver)
        } catch (e: Exception) {
            null
        }

    /**
     * Low-level JWT parsing and verification.
     * Complexity: O(1)
     * 
     * Error cases:
     * - Malformed JWT
     * - Invalid signature
     * - Expired token
     * - Invalid claim format
     */
    override fun extractAllClaims(token: String): Claims? =
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
