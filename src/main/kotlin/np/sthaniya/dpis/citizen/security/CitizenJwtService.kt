package np.sthaniya.dpis.citizen.security

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import org.springframework.security.core.userdetails.UserDetails
import java.time.Duration

/**
 * Service for Citizen JWT token operations:
 * - Token generation (access and refresh)
 * - Token validation and verification
 * - Token blacklisting
 * - Email/username extraction
 * - Token pair management
 */
interface CitizenJwtService {
    /**
     * Gets the username from a JWT token.
     */
    fun extractUsername(token: String): String?

    /**
     * Gets the email from a JWT token, throws if not found.
     */
    fun extractEmail(token: String): String

    /**
     * Creates a JWT token from Spring UserDetails.
     */
    fun generateToken(userDetails: UserDetails): String

    /**
     * Creates a JWT token from Citizen entity with permissions.
     */
    fun generateToken(citizen: Citizen): String

    /**
     * Creates a refresh token for the given citizen.
     */
    fun generateRefreshToken(citizen: Citizen): String

    /**
     * Creates a custom JWT token with specified claims and expiration.
     */
    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails, expiration: Long): String

    /**
     * Checks if a token is valid for a specific user.
     */
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean

    /**
     * Checks if a token is structurally valid.
     */
    fun validateToken(token: String): Boolean

    /**
     * Gets the configured token expiration time in hours.
     */
    fun getTokenExpirationTime(): Long

    /**
     * Creates both access and refresh tokens for a citizen.
     */
    fun generateTokenPair(citizen: Citizen): CitizenTokenPair

    /**
     * Adds a token to the blacklist.
     */
    fun invalidateToken(token: String)

    /**
     * Gets the token expiration as a Duration.
     */
    fun getExpirationDuration(): Duration

    /**
     * Creates a pre-expired token for testing.
     */
    fun generateExpiredToken(citizen: Citizen): String
    
    /**
     * Extracts all claims from a token.
     * Used by the CurrentCitizenIdResolver.
     */
    fun extractAllClaims(token: String): io.jsonwebtoken.Claims?
}

/**
 * Data class representing a pair of access and refresh tokens for citizen authentication.
 */
data class CitizenTokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
