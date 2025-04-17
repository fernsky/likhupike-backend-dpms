package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

/**
 * Response DTO for media upload operation.
 */
@Schema(description = "Result of media upload operation")
data class MediaUploadResponse(
    @Schema(description = "ID of the created media entry")
    val id: UUID,

    @Schema(description = "Storage key for the uploaded file")
    val storageKey: String,

    @Schema(description = "Original filename of the uploaded file")
    val originalFilename: String,

    @Schema(description = "Content type of the file")
    val contentType: String?,

    @Schema(description = "Size of the file in bytes")
    val size: Long,

    @Schema(description = "Public URL to access the media")
    val url: String?,

    @Schema(description = "URL to thumbnail version (for images/videos)")
    val thumbnailUrl: String?
)
