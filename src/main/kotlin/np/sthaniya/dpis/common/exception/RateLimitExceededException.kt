package np.sthaniya.dpis.common.exception

import org.springframework.http.HttpStatus
import java.time.Instant

enum class RateLimitErrorCode(
    override val code: String,
    override val defaultMessage: String
) : ErrorCode {
    RATE_LIMIT_EXCEEDED("RATE_001", "Rate limit exceeded. Please try again later.")
}

class RateLimitExceededException(
    message: String? = null,
    val remainingSeconds: Long? = null
) : dpisException(
    message = message,
    errorCode = RateLimitErrorCode.RATE_LIMIT_EXCEEDED,
    status = HttpStatus.TOO_MANY_REQUESTS,
    metadata = mapOf(
        "retryAfterSeconds" to (remainingSeconds ?: 0L),
        "retryAfter" to Instant.now().plusSeconds(remainingSeconds ?: 0L).toString()
    )
)
