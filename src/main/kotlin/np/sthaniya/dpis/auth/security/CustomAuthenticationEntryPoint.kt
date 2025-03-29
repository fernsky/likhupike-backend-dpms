package np.sthaniya.dpis.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.sthaniya.dpis.common.config.RouteRegistry
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.dto.ErrorDetails
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
    private val routeRegistry: RouteRegistry,
) : AuthenticationEntryPoint {
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
                    message = "Method not allowed for this resource",
                    status = HttpStatus.METHOD_NOT_ALLOWED.value(),
                    details = mapOf("allowedMethods" to allowedMethods.map { it.toString() }),
                ),
            )

        objectMapper.writeValue(response.outputStream, errorResponse)
    }

    private fun handleUnauthorized(response: HttpServletResponse) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse =
            ApiResponse.error<Nothing>(
                ErrorDetails(
                    code = "UNAUTHORIZED",
                    message = "Please authenticate to access this resource",
                    status = HttpStatus.UNAUTHORIZED.value(),
                ),
            )

        objectMapper.writeValue(response.outputStream, errorResponse)
    }

    private fun handleNotFound(response: HttpServletResponse) {
        response.status = HttpStatus.NOT_FOUND.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse =
            ApiResponse.error<Nothing>(
                ErrorDetails(
                    code = "NOT_FOUND",
                    message = "Resource not found",
                    status = HttpStatus.NOT_FOUND.value(),
                ),
            )

        objectMapper.writeValue(response.outputStream, errorResponse)
    }
}
