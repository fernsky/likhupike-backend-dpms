package np.sthaniya.dpis.citizen.config

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import jakarta.servlet.FilterChain
import np.sthaniya.dpis.citizen.service.CitizenUserDetailsService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.sthaniya.dpis.citizen.exception.CitizenAuthException
import np.sthaniya.dpis.citizen.security.CitizenJwtService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT-based authentication filter for citizen access to Spring Security.
 *
 * This filter intercepts incoming HTTP requests and processes citizen JWT authentication tokens.
 * It's responsible for:
 * - Extracting JWT tokens from the Authorization header
 * - Validating tokens using [CitizenJwtService]
 * - Loading citizen details using [CitizenUserDetailsService]
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
    private val citizenUserDetailsService: CitizenUserDetailsService,
    private val citizenRouteRegistry: CitizenRouteRegistry
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(javaClass)

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
            // info request info
            logger.debug("Processing request: " + request.servletPath)

            val authHeader = request.getHeader("Authorization")
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.debug("No Bearer token found in request")
                filterChain.doFilter(request, response)
                return
            }

            val jwt = authHeader.substring(7)
            logger.debug("JWT token extracted from request")

            // Skip processing if this is not a citizen API path
            if (!isCitizenApiPath(request.servletPath)) {
                logger.debug("Not a citizen API path: " + request.servletPath)
                filterChain.doFilter(request, response)
                return
            }

            // For citizen paths, clear any existing authentication to ensure proper processing
            // This is the critical fix
            SecurityContextHolder.clearContext()

            logger.debug("Identified as citizen API path: " + request.servletPath)

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
            logger.warn("Unauthorized access attempt", e)
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
        // Use direct string concatenation for logger messages
        logger.debug("Processing citizen JWT authentication for request: " + request.servletPath)

        val citizenEmail = citizenJwtService.extractUsername(jwt)

        // For null values, use appropriate handling
        if (citizenEmail != null) {
            logger.debug("Extracted email from JWT: " + citizenEmail)
        } else {
            logger.debug("No email extracted from JWT")
        }

        if (citizenEmail != null && SecurityContextHolder.getContext().authentication == null) {
            logger.debug("Loading citizen details for email: " + citizenEmail)

            try {
                val citizenDetails = citizenUserDetailsService.loadUserByUsername(citizenEmail)
                logger.debug("Citizen details loaded successfully: class=" + citizenDetails.javaClass.name)

                if (citizenJwtService.isTokenValid(jwt, citizenDetails)) {
                    logger.debug("JWT token is valid for citizen: " + citizenEmail)

                    // Log the class and interfaces of the citizenDetails object
                    logger.debug("Citizen details class: " + citizenDetails.javaClass.name)

                    val interfaces = citizenDetails.javaClass.interfaces.joinToString { it.name }
                    logger.debug("Citizen details interfaces: " + interfaces)

                    // Create authentication token
                    val authToken = UsernamePasswordAuthenticationToken(
                        (citizenDetails as Citizen).id.toString(),
                        null,
                        citizenDetails.authorities
                    )

                    val principalClass = authToken.principal.javaClass.name
                    val authorities = authToken.authorities.joinToString { it.authority }
                    logger.debug("Created authentication token: principal=" + principalClass + ", authorities=" + authorities)

                    // Set authentication details
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                    // Set security context
                    SecurityContextHolder.getContext().authentication = authToken
                    logger.debug("Citizen authentication successful for: " + citizenEmail)
                } else {
                    logger.warn("Invalid JWT token for citizen: " + citizenEmail)
                    throw CitizenAuthException.JwtAuthenticationException("Invalid citizen JWT token")
                }
            } catch (e: Exception) {
                // For exception logging, always pass the exception as the last parameter
                logger.error("Error during citizen authentication: " + e.message, e)
                throw e
            }
        } else {
            val hasAuth = SecurityContextHolder.getContext().authentication != null
            logger.debug("Skipping authentication - email present: " + (citizenEmail != null) + ", existing auth: " + hasAuth)
        }
    }
}
