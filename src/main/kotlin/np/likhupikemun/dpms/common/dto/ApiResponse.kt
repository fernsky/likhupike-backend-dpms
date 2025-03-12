package np.likhupikemun.dpms.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.domain.Page
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val meta: PaginationMeta? = null,
    val error: ErrorDetails? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun <T> success(
            data: T? = null,
            message: String? = null,
            meta: PaginationMeta? = null
        ) = ApiResponse(
            success = true,
            data = data,
            message = message,
            meta = meta
        )

        fun <T> error(error: ErrorDetails) = ApiResponse<T>(
            success = false,
            error = error
        )
    }
}

data class ErrorDetails(
    val code: String,
    val message: String,
    val details: Map<String, Any>? = null,
    val status: Int
)

data class PaginationMeta(
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean
) {
    companion object {
        fun from(page: Page<*>) = PaginationMeta(
            page = page.number,
            size = page.size,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            isFirst = page.isFirst,
            isLast = page.isLast
        )
    }
}

fun <T> Page<T>.toApiResponse(message: String? = null) = ApiResponse.success(
    data = content,
    message = message,
    meta = PaginationMeta.from(this)
)

fun errorResponse(
    code: String,
    message: String,
    details: Map<String, Any>? = null,
    status: Int
) = ApiResponse.error<Nothing>(
    ErrorDetails(
        code = code,
        message = message,
        details = details,
        status = status
    )
)
