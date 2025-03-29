package np.sthaniya.dpis.auth.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.sthaniya.dpis.auth.exception.AuthException
import np.sthaniya.dpis.auth.security.JwtService
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT-based authentication filter for Spring Security.
 *
 * This filter intercepts incoming HTTP requests and processes JWT authentication tokens.
 * It's responsible for:
 * - Extracting JWT tokens from the Authorization header
 * - Validating tokens using [JwtService]
 * - Loading user details using [UserDetailsService]
 * - Setting up the Spring Security context
 *
 * Filter Chain Process:
 * 1. Extract Bearer token from Authorization header
 * 2. Validate JWT token
 * 3. Load user details
 * 4. Set up authentication context
 *
 * Usage:
 * This filter is automatically configured in [SecurityConfig] and added to the Spring Security filter chain
 * before [UsernamePasswordAuthenticationFilter].
 *
 * @property jwtService Service for JWT token operations
 * @property userDetailsService Service for loading user details
 */
@Configuration
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {

    /**
     * Main filter method that processes each HTTP request exactly once.
     *
     * This method:
     * 1. Extracts the JWT token from the Authorization header
     * 2. Validates the token and sets up the security context
     * 3. Handles authentication exceptions
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain for continuing request processing
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val authHeader = request.getHeader("Authorization")
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response)
                return
            }

            val jwt = authHeader.substring(7)

            try {
                processJwtAuthentication(jwt, request)
            } catch (e: Exception) {
                logger.error("JWT Authentication failed", e)
                throw AuthException.JwtAuthenticationException(
                    details =
                        mapOf(
                            "token" to jwt,
                            "error" to (e.message ?: "Unknown error"),
                        ),
                )
            }
        } catch (e: AuthException.UnauthenticatedException) {
            SecurityContextHolder.clearContext()
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
            return
        }

        filterChain.doFilter(request, response)
    }

    /**
     * Processes JWT authentication and sets up the security context.
     *
     * This method:
     * 1. Extracts user email from JWT token
     * 2. Loads user details if not already authenticated
     * 3. Validates token against user details
     * 4. Sets up authentication context
     *
     * @param jwt The JWT token string
     * @param request The HTTP request for building authentication details
     * @throws AuthException.JwtAuthenticationException if authentication fails
     */
    private fun processJwtAuthentication(
        jwt: String,
        request: HttpServletRequest,
    ) {
        val userEmail = jwtService.extractUsername(jwt)

        if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(userEmail)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                val authToken =
                    UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities,
                    )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            } else {
                throw AuthException.JwtAuthenticationException("Invalid JWT token")
            }
        }
    }
}
