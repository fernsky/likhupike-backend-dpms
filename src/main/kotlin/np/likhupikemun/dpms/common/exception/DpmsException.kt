package np.likhupikemun.dpms.common.exception

import org.springframework.http.HttpStatus

abstract class DpmsException(
    val errorCode: ErrorCode,
    message: String? = null,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
    val metadata: Map<String, Any> = emptyMap(),
    cause: Throwable? = null
) : RuntimeException(message ?: errorCode.defaultMessage, cause) {

    fun toErrorResponse() = ErrorResponse(
        code = errorCode.code,
        message = message ?: errorCode.defaultMessage,
        status = status.value(),
        metadata = metadata
    )

    data class ErrorResponse(
        val code: String,
        val message: String,
        val status: Int,
        val metadata: Map<String, Any>
    )
}
