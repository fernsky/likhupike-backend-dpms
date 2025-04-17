package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.MediaVisibilityStatus
import java.time.Instant
import java.util.UUID

/**
 * Response DTO for cooperative media.
 */
@Schema(description = "Cooperative media data")
data class CooperativeMediaResponse(
    @Schema(description = "Unique identifier for the media")
    val id: UUID,

    @Schema(description = "Locale for this media item (null if not language-specific)", example = "ne")
    val locale: String?,

    @Schema(description = "Type of media")
    val type: CooperativeMediaType,

    @Schema(description = "Title/caption of the media", example = "Members of the cooperative during harvest")
    val title: String,

    @Schema(description = "Detailed description of the media content")
    val description: String?,

    @Schema(description = "Alternative text for accessibility and SEO", example = "Cooperative members harvesting rice in the fields")
    val altText: String?,

    @Schema(description = "Path to the stored file in the system")
    val filePath: String,

    @Schema(description = "Public URL to access the media")
    val fileUrl: String?,

    @Schema(description = "URL to thumbnail version (for images/videos)")
    val thumbnailUrl: String?,

    @Schema(description = "MIME type of the media file", example = "image/jpeg")
    val mimeType: String?,

    @Schema(description = "Size of the file in bytes", example = "2048000")
    val fileSize: Long?,

    @Schema(description = "Dimensions for image/video files", example = "1920x1080")
    val dimensions: String?,

    @Schema(description = "Duration for audio/video files in seconds", example = "125")
    val duration: Int?,

    @Schema(description = "Additional metadata in JSON format")
    val metadata: String?,

    @Schema(description = "Searchable tags associated with this media", example = "harvest,rice,members,agriculture")
    val tags: String?,

    @Schema(description = "License information for the media", example = "CC BY-SA 4.0")
    val license: String?,

    @Schema(description = "Attribution information if required", example = "Photo by Ramesh Sharma")
    val attribution: String?,

    @Schema(description = "Display order for multiple media items", example = "10")
    val sortOrder: Int,

    @Schema(description = "Whether this is the primary media item of its type")
    val isPrimary: Boolean,

    @Schema(description = "Whether this media should be featured prominently")
    val isFeatured: Boolean,

    @Schema(description = "Visibility status for the media")
    val visibilityStatus: MediaVisibilityStatus,

    @Schema(description = "When this media was uploaded")
    val uploadedAt: Instant,

    @Schema(description = "When this media was last accessed/viewed")
    val lastAccessed: Instant?,

    @Schema(description = "Number of times this media has been viewed", example = "1532")
    val viewCount: Long
)
