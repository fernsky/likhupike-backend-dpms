package np.sthaniya.dpis.common.exception

import org.springframework.http.HttpStatus

/**
 * Exception thrown when a client exceeds the rate limit for API calls.
 *
 * This exception is typically thrown by rate-limiting filters or services
 * when a client makes too many requests in a short period of time.
 *
 * @param message Optional custom error message
 * @param metadata Additional error context data
 */
class RateLimitedException(
    message: String? = null,
    metadata: Map<String, Any> = emptyMap()
) : dpisException(
    errorCode = RateLimitErrorCode.RATE_LIMIT_EXCEEDED,
    message = message,
    status = HttpStatus.TOO_MANY_REQUESTS,
    metadata = metadata
) {
    /**
     * Error code for rate limiting
     */
    enum class RateLimitErrorCode : ErrorCode {
        RATE_LIMIT_EXCEEDED {
            override val code = "RATE_001"
            override val defaultMessage = "Rate limit exceeded. Please try again later."
            override val i18nKey = "error.rate_limited"
        }
    }
}
