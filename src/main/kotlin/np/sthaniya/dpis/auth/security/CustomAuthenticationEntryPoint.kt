package np.sthaniya.dpis.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.sthaniya.dpis.common.config.RouteRegistry
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.dto.ErrorDetails
import np.sthaniya.dpis.common.service.I18nMessageService
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * Custom implementation of [AuthenticationEntryPoint] that handles unauthorized access attempts
 * and various HTTP error scenarios.
 *
 * This entry point provides detailed JSON responses for different authentication and
 * authorization scenarios, including:
 * - Unauthorized access attempts
 * - Method not allowed errors
 * - Resource not found errors
 *
 * @property objectMapper The Jackson object mapper used for JSON serialization
 * @property routeRegistry The registry containing route patterns and their configurations
 * @property i18nMessageService The service for retrieving internationalized messages
 */
@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
    private val routeRegistry: RouteRegistry,
    private val i18nMessageService: I18nMessageService,
) : AuthenticationEntryPoint {
    /**
     * Handles the authentication entry point by determining the appropriate error response
     * based on the request details.
     *
     * @param request The incoming HTTP request
     * @param response The HTTP response to be modified
     * @param authException The authentication exception that triggered this handler
     */
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        val method = HttpMethod.valueOf(request.method)
        val path = request.requestURI

        if (!routeRegistry.hasMatchingPattern(path)) {
            handleNotFound(response)
            return
        }

        val validMethods = routeRegistry.getValidMethodsForPath(path)
        if (!validMethods.contains(method)) {
            handleMethodNotAllowed(response, validMethods)
            return
        }

        if (routeRegistry.isPublicRoute(path, method)) {
            handleUnauthorized(response)
            return
        }

        handleUnauthorized(response)
    }

    /**
     * Handles HTTP 405 Method Not Allowed responses.
     *
     * @param response The HTTP response to be modified
     * @param allowedMethods Set of HTTP methods that are allowed for the requested resource
     */
    fun handleMethodNotAllowed(
        response: HttpServletResponse,
        allowedMethods: Set<HttpMethod>,
    ) {
        response.status = HttpStatus.METHOD_NOT_ALLOWED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.setHeader("Allow", allowedMethods.joinToString(", ") { it.toString() })

        val errorResponse =
            ApiResponse.error<Nothing>(
                ErrorDetails(
                    code = "METHOD_NOT_ALLOWED",
                    message = i18nMessageService.getMessage("common.error.METHOD_NOT_ALLOWED"),
                    status = HttpStatus.METHOD_NOT_ALLOWED.value(),
                    details = mapOf("allowedMethods" to allowedMethods.map { it.toString() }),
                ),
            )

        objectMapper.writeValue(response.outputStream, errorResponse)
    }

    /**
     * Handles HTTP 401 Unauthorized responses.
     *
     * @param response The HTTP response to be modified
     */
    private fun handleUnauthorized(response: HttpServletResponse) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse =
            ApiResponse.error<Nothing>(
                ErrorDetails(
                    code = "UNAUTHORIZED",
                    message = i18nMessageService.getMessage("common.error.UNAUTHORIZED"),
                    status = HttpStatus.UNAUTHORIZED.value(),
                ),
            )

        objectMapper.writeValue(response.outputStream, errorResponse)
    }

    /**
     * Handles HTTP 404 Not Found responses.
     *
     * @param response The HTTP response to be modified
     */
    private fun handleNotFound(response: HttpServletResponse) {
        response.status = HttpStatus.NOT_FOUND.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse =
            ApiResponse.error<Nothing>(
                ErrorDetails(
                    code = "NOT_FOUND",
                    message = i18nMessageService.getMessage("common.error.NOT_FOUND"),
                    status = HttpStatus.NOT_FOUND.value(),
                ),
            )

        objectMapper.writeValue(response.outputStream, errorResponse)
    }
}

