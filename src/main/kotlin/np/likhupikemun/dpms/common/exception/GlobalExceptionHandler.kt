package np.likhupikemun.dpms.common.exception

import jakarta.validation.ConstraintViolationException
import np.likhupikemun.dpms.common.dto.ApiResponse
import np.likhupikemun.dpms.common.dto.ErrorDetails
import np.likhupikemun.dpms.auth.exception.AuthException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.http.converter.HttpMessageNotReadableException

@RestControllerAdvice
class GlobalExceptionHandler {
    
    @ExceptionHandler(DpmsException::class)
    fun handleDpmsException(ex: DpmsException): ResponseEntity<ApiResponse<Nothing>> =
        ResponseEntity
            .status(ex.status)
            .body(
                ApiResponse.error(
                    ErrorDetails(
                        code = ex.errorCode.code,
                        message = ex.message ?: ex.errorCode.defaultMessage,
                        details = ex.metadata,
                        status = ex.status.value()
                    )
                )
            )

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<ApiResponse<Nothing>> {
        val details = ex.constraintViolations.associate {
            it.propertyPath.toString() to it.message
        }
        
        return ResponseEntity
            .badRequest()
            .body(
                ApiResponse.error(
                    ErrorDetails(
                        code = "VALIDATION_ERROR",
                        message = "Validation failed",
                        details = details,
                        status = HttpStatus.BAD_REQUEST.value()
                    )
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val details = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }
        
        return ResponseEntity
            .badRequest()
            .body(
                ApiResponse.error(
                    ErrorDetails(
                        code = "VALIDATION_ERROR",
                        message = "Validation failed",
                        details = details,
                        status = HttpStatus.BAD_REQUEST.value()
                    )
                )
            )
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<ApiResponse<Nothing>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val isAuthenticated = authentication != null && authentication.isAuthenticated

        val exception = if (!isAuthenticated) {
            AuthException.UnauthenticatedException()
        } else {
            AuthException.InsufficientPermissionsException(
                message = "Access denied: insufficient permissions",
                requiredPermissions = getRequiredPermissions(ex)
            )
        }

        return handleDpmsException(exception)
    }

    private fun getRequiredPermissions(ex: AccessDeniedException): Set<String>? {
        // Extract required permissions from Spring Security context if available
        return try {
            SecurityContextHolder.getContext()
                .authentication
                ?.authorities
                ?.map { it.authority }
                ?.toSet()
        } catch (e: Exception) {
            null
        }
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<ApiResponse<Nothing>> =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ApiResponse.error(
                    ErrorDetails(
                        code = "DATA_INTEGRITY_ERROR",
                        message = "Data integrity violation",
                        details = mapOf("error" to (ex.message ?: "Data integrity constraint violated")),
                        status = HttpStatus.CONFLICT.value()
                    )
                )
            )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Nothing>> {
        val message = when {
            ex.cause is InvalidFormatException -> {
                val cause = ex.cause as InvalidFormatException
                when {
                    cause.targetType.isEnum -> {
                        val enumClass = cause.targetType
                        val validValues = (enumClass.enumConstants as Array<Enum<*>>).joinToString(", ") { it.name }
                        "Invalid value '${cause.value}'. Allowed values are: [$validValues]"
                    }
                    else -> "Invalid format: ${cause.originalMessage}"
                }
            }
            else -> "Invalid request format"
        }

        return ResponseEntity
            .badRequest()
            .body(
                ApiResponse.error(
                    ErrorDetails(
                        code = "INVALID_FORMAT",
                        message = "Invalid request format",
                        details = mapOf("error" to message),
                        status = HttpStatus.BAD_REQUEST.value()
                    )
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<ApiResponse<Nothing>> =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse.error(
                    ErrorDetails(
                        code = "INTERNAL_SERVER_ERROR",
                        message = "An unexpected error occurred",
                        details = mapOf("error" to (ex.message ?: "Unknown error")),
                        status = HttpStatus.INTERNAL_SERVER_ERROR.value()
                    )
                )
            )
}
