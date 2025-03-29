package np.sthaniya.dpis.auth.security

/**
 * Data class representing a pair of JWT tokens used for authentication.
 *
 * This class encapsulates the token pair used in the JWT authentication flow:
 * - Access token for short-term API access
 * - Refresh token for obtaining new access tokens
 * - Expiration time for token validity
 *
 * Usage Example:
 * ```kotlin
 * // Creating a token pair
 * val tokenPair = TokenPair(
 *     accessToken = "eyJhbGciOiJIUzI1...",
 *     refreshToken = "eyJhbGciOiJIUzI1...",
 *     expiresIn = 3600L // 1 hour
 * )
 *
 * // Using in authentication response
 * return AuthResponse(
 *     tokens = tokenPair,
 *     user = userDetails
 * )
 * ```
 *
 * Security Considerations:
 * - Access tokens should have short lifetime (e.g., 15-60 minutes)
 * - Refresh tokens should have longer lifetime (e.g., 7-30 days)
 * - Both tokens should be transmitted securely (HTTPS)
 * - Refresh tokens should be stored securely by clients
 * - Access tokens should never be stored in browser localStorage
 *
 * Integration Points:
 * - Used by [AuthService] for token generation
 * - Used in authentication responses
 * - Used in token refresh operations
 * - Consumed by client applications
 *
 * @property accessToken JWT token for API access
 * @property refreshToken JWT token for refreshing access tokens
 * @property expiresIn Time in seconds until the access token expires
 */
data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
