package np.sthaniya.dpis.common.exception

import org.springframework.http.HttpStatus


enum class RateLimitErrorCode(
    override val code: String,
    override val defaultMessage: String
) : ErrorCode {
    RATE_LIMIT_EXCEEDED("RATE_001", "Rate limit exceeded. Please try again later.")
}

class RateLimitExceededException(
    message: String? = null,
    val remainingSeconds: Long? = null
) : DpisException(
    message = message,
    errorCode = RateLimitErrorCode.RATE_LIMIT_EXCEEDED,
    status = HttpStatus.TOO_MANY_REQUESTS,
    metadata = remainingSeconds?.let { 
        mapOf(
            "retryAfterSeconds" to it,
            "retryAfter" to java.time.Instant.now().plusSeconds(it).toString()
        )
    }
)