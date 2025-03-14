package np.likhupikemun.dpms.auth.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import np.likhupikemun.dpms.common.dto.ErrorDetails
import np.likhupikemun.dpms.common.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse = ApiResponse.error<Nothing>(
            ErrorDetails(
                code = "AUTHENTICATION_REQUIRED",
                message = "Authentication required",
                details = mapOf("error" to "Full authentication is required to access this resource"),
                status = HttpStatus.UNAUTHORIZED.value()
            )
        )

        val mapper = ObjectMapper()
        mapper.writeValue(response.outputStream, errorResponse)
    }
}
