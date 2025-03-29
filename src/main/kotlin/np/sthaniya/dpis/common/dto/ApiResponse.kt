package np.sthaniya.dpis.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

/**
 * Standard response wrapper for all API endpoints.
 *
 * Provides a consistent response structure with support for:
 * - Success/failure status
 * - Optional payload data
 * - Success messages
 * - Pagination metadata
 * - Error details
 *
 * @param T The type of the response payload data
 * @property success Indicates if the request was successful
 * @property data Optional response payload
 * @property message Optional success message
 * @property meta Optional pagination metadata
 * @property error Optional error details when success is false
 */
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
        /**
         * Creates a successful response with optional data, message, and pagination metadata.
         *
         * @param T The type of the response payload
         * @param data Optional response payload
         * @param message Optional success message
         * @param meta Optional pagination metadata
         * @return [ApiResponse] instance with success status
         */
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

        /**
         * Creates an error response with error details.
         *
         * @param T The type of the response payload
         * @param error Error details for the failed request
         * @return [ApiResponse] instance with failure status
         */
        fun <T> error(error: ErrorDetails) = ApiResponse<T>(
            success = false,
            error = error,
        )

        /**
         * Creates a paginated response from a Spring [Page] instance.
         *
         * @param T The type of the page content
         * @param page Spring Page instance containing the data
         * @param message Optional success message
         * @return [ApiResponse] instance with paginated data
         */
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

/**
 * Represents detailed error information for failed requests.
 *
 * @property code Unique identifier for the error type
 * @property message Human-readable error description
 * @property details Additional contextual information about the error
 * @property status HTTP status code associated with the error
 */
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

/**
 * Contains metadata for paginated responses.
 *
 * @property page Current page number (1-based)
 * @property size Number of items per page
 * @property totalElements Total number of items across all pages
 * @property totalPages Total number of available pages
 * @property isFirst Whether this is the first page
 * @property isLast Whether this is the last page
 */
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
        /**
         * Creates pagination metadata from a Spring [Page] instance.
         *
         * @param T The type of the page content
         * @param page Spring Page instance to extract metadata from
         * @return [PageMeta] instance with pagination details
         */
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

/**
 * Converts a Spring [Page] instance to an [ApiResponse] with pagination.
 *
 * @param T The type of the page content
 * @param message Optional success message
 * @return [ApiResponse] instance containing the page content and metadata
 */
fun <T> Page<T>.toApiResponse(message: String? = null) = ApiResponse.withPage(this, message)
