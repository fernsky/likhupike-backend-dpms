package np.sthaniya.dpis.citizen.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.sthaniya.dpis.citizen.exception.CitizenAuthException
import np.sthaniya.dpis.citizen.security.CitizenJwtService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT-based authentication filter for citizen access to Spring Security.
 *
 * This filter intercepts incoming HTTP requests and processes citizen JWT authentication tokens.
 * It's responsible for:
 * - Extracting JWT tokens from the Authorization header
 * - Validating tokens using [CitizenJwtService]
 * - Loading citizen details using [UserDetailsService]
 * - Setting up the Spring Security context for authenticated citizens
 *
 * Filter Chain Process:
 * 1. Extract Bearer token from Authorization header
 * 2. Validate citizen JWT token
 * 3. Load citizen details
 * 4. Set up authentication context
 *
 * Usage:
 * This filter is automatically configured and added to the Spring Security filter chain
 * to handle citizen-specific authentication.
 *
 * @property citizenJwtService Service for Citizen JWT token operations
 * @property userDetailsService Service for loading citizen details
 * @property citizenRouteRegistry Registry for citizen route checking
 */
@Configuration
class CitizenJwtAuthenticationFilter(
    private val citizenJwtService: CitizenJwtService,
    private val userDetailsService: UserDetailsService,
    private val citizenRouteRegistry: CitizenRouteRegistry
) : OncePerRequestFilter() {

    /**
     * Main filter method that processes each HTTP request exactly once.
     *
     * This method:
     * 1. Extracts the JWT token from the Authorization header
     * 2. Validates the token and sets up the security context for the citizen
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

            // Skip processing if this is not a citizen API path
            if (!isCitizenApiPath(request.servletPath)) {
                filterChain.doFilter(request, response)
                return
            }

            try {
                processCitizenJwtAuthentication(jwt, request)
            } catch (e: Exception) {
                logger.error("Citizen JWT Authentication failed", e)
                throw CitizenAuthException.JwtAuthenticationException(
                    message = e.message,
                    details = mapOf(
                        "token" to jwt,
                        "error" to (e.message ?: "Unknown error"),
                    ),
                )
            }
        } catch (e: CitizenAuthException.UnauthenticatedException) {
            SecurityContextHolder.clearContext()
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
            return
        }

        filterChain.doFilter(request, response)
    }

    /**
     * Determines if a request path is for citizen API.
     * 
     * This helps to ensure we only apply citizen JWT authentication
     * to citizen-specific API paths.
     * 
     * @param path The servlet path of the request
     * @return true if the path is for citizen API
     */
    private fun isCitizenApiPath(path: String): Boolean {
        // Use the registry to determine if this is a citizen route
        // If method doesn't matter for this check, we'll try common HTTP methods
        return citizenRouteRegistry.isCitizenRoute(path, HttpMethod.GET) ||
               citizenRouteRegistry.isCitizenRoute(path, HttpMethod.POST) ||
               citizenRouteRegistry.isCitizenRoute(path, HttpMethod.PUT) ||
               citizenRouteRegistry.isCitizenRoute(path, HttpMethod.DELETE)
    }

    /**
     * Processes citizen JWT authentication and sets up the security context.
     *
     * This method:
     * 1. Extracts citizen email from JWT token
     * 2. Loads citizen details if not already authenticated
     * 3. Validates token against citizen details
     * 4. Sets up authentication context
     *
     * @param jwt The JWT token string
     * @param request The HTTP request for building authentication details
     * @throws CitizenAuthException.JwtAuthenticationException if authentication fails
     */
    private fun processCitizenJwtAuthentication(
        jwt: String,
        request: HttpServletRequest,
    ) {
        val citizenEmail = citizenJwtService.extractUsername(jwt)

        if (citizenEmail != null && SecurityContextHolder.getContext().authentication == null) {
            val citizenDetails = userDetailsService.loadUserByUsername(citizenEmail)
            if (citizenJwtService.isTokenValid(jwt, citizenDetails)) {
                val authToken =
                    UsernamePasswordAuthenticationToken(
                        citizenDetails,
                        null,
                        citizenDetails.authorities,
                    )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            } else {
                throw CitizenAuthException.JwtAuthenticationException("Invalid citizen JWT token")
            }
        }
    }
}
