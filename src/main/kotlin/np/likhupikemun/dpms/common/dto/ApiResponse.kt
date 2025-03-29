package np.likhupikemun.dpis.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(
    description = "Standard API response wrapper for all endpoints",
    title = "API Response"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    @Schema(description = "Indicates if the request was successful", example = "true")
    val success: Boolean,
    
    @Schema(description = "Response payload data", nullable = true)
    val data: T? = null,
    
    @Schema(description = "Optional success message", example = "User created successfully", nullable = true)
    val message: String? = null,
    
    @Schema(description = "Pagination metadata, present only for paginated responses", nullable = true)
    val meta: PageMeta? = null,
    
    @Schema(description = "Error details, present only when success is false", nullable = true)
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

@Schema(
    description = "Error details structure for failed requests",
    title = "Error Details"
)
data class ErrorDetails(
    @Schema(description = "Error code identifier", example = "VALIDATION_ERROR")
    val code: String,
    
    @Schema(description = "Human-readable error message", example = "Invalid input parameters")
    val message: String,
    
    @Schema(
        description = "Additional error context and details",
        example = """{"field": "email", "violation": "must be a valid email"}""",
        nullable = true
    )
    val details: Map<String, Any>? = null,
    
    @Schema(description = "HTTP status code", example = "400")
    val status: Int,
)

@Schema(
    description = "Pagination metadata for paginated responses",
    title = "Page Metadata"
)
data class PageMeta(
    @Schema(description = "Current page number (1-based)", example = "1")
    val page: Int,
    
    @Schema(description = "Number of items per page", example = "20")
    val size: Int,
    
    @Schema(description = "Total number of items across all pages", example = "100")
    val totalElements: Long,
    
    @Schema(description = "Total number of pages", example = "5")
    val totalPages: Int,
    
    @Schema(description = "Indicates if this is the first page", example = "true")
    val isFirst: Boolean,
    
    @Schema(description = "Indicates if this is the last page", example = "false")
    val isLast: Boolean
) {
    companion object {
        fun <T> from(page: Page<T>) = PageMeta(
            page = page.number + 1, // Convert 0-based to 1-based
            size = page.size,
            totalElements = page.totalElements, // Use total elements count
            totalPages = page.totalPages,
            isFirst = page.isFirst,
            isLast = page.isLast
        )
    }
}

fun <T> Page<T>.toApiResponse(message: String? = null) = ApiResponse.withPage(this, message)
