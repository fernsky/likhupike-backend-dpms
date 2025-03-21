package np.likhupikemun.dpms.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.domain.Page

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val meta: PageMeta? = null,
    val error: ErrorDetails? = null,
) {
    companion object {
        fun <T> success(
            data: T? = null,
            message: String? = null,
            meta: PageMeta? = null,
        ) = ApiResponse(
            success = true,
            data = data,
            message = message,
            meta = meta,
        )

        fun <T> error(error: ErrorDetails) = ApiResponse<T>(
            success = false,
            error = error,
        )

        fun <T> withPage(
            page: Page<T>,
            message: String? = null
        ) = ApiResponse(
            success = true,
            data = page.content,
            message = message,
            meta = PageMeta.from(page)
        )
    }
}

data class ErrorDetails(
    val code: String,
    val message: String,
    val details: Map<String, Any>? = null,
    val status: Int,
)

data class PageMeta(
    val page: Int,            // Current page number (1-based)
    val size: Int,            // Page size
    val totalElements: Long,  // Total number of elements
    val totalPages: Int,      // Total number of pages
    val isFirst: Boolean,     // If this is the first page
    val isLast: Boolean      // If this is the last page
) {
    companion object {
        fun <T> from(page: Page<T>) = PageMeta(
            page = page.number + 1, // Convert 0-based to 1-based
            size = page.size,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            isFirst = page.isFirst,
            isLast = page.isLast
        )
    }
}

fun <T> Page<T>.toApiResponse(message: String? = null) = ApiResponse.withPage(this, message)
