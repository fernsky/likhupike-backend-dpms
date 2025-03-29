package np.sthaniya.dpis.common.exception

import org.springframework.http.HttpStatus

/**
 * Base exception class for DPIS application-specific exceptions.
 *
 * This abstract class provides a consistent structure for handling application errors with:
 * - Error code categorization
 * - Custom error messages
 * - HTTP status codes
 * - Additional error metadata
 * - Conversion to API response format
 *
 * @property errorCode The categorized error code enum
 * @property message Optional custom error message (defaults to error code's message)
 * @property status HTTP status code for the error (defaults to BAD_REQUEST)
 * @property metadata Additional context data for the error
 * @property cause Optional underlying cause of the error
 */
abstract class dpisException(
    val errorCode: ErrorCode,
    message: String? = null,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
    val metadata: Map<String, Any> = emptyMap(),
    cause: Throwable? = null
) : RuntimeException(message ?: errorCode.defaultMessage, cause) {

    /**
     * Converts this exception to a standardized error response structure.
     *
     * @return [ErrorResponse] containing the error details
     */
    fun toErrorResponse() = ErrorResponse(
        code = errorCode.code,
        message = message ?: errorCode.defaultMessage,
        status = status.value(),
        metadata = metadata
    )

    /**
     * Data structure for representing error details in API responses.
     *
     * @property code String identifier for the error type
     * @property message Human-readable error description
     * @property status HTTP status code
     * @property metadata Additional error context information
     */
    data class ErrorResponse(
        val code: String,
        val message: String,
        val status: Int,
        val metadata: Map<String, Any>
    )
}
