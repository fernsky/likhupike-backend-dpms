package np.sthaniya.dpis.profile.institutions.cooperatives.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.MediaVisibilityStatus

/**
 * DTO for updating existing cooperative media metadata.
 */
@Schema(description = "Request for updating cooperative media metadata")
data class UpdateCooperativeMediaDto(
    @Schema(description = "Locale for this media item (null if not language-specific)", example = "ne")
    val locale: String?,

    @Schema(description = "Type of media")
    val type: CooperativeMediaType?,

    @field:Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @Schema(description = "Title/caption of the media", example = "Members of the cooperative during harvest")
    val title: String?,

    @Schema(description = "Detailed description of the media content")
    val description: String?,

    @field:Size(max = 255, message = "Alt text must be less than 255 characters")
    @Schema(description = "Alternative text for accessibility and SEO", example = "Cooperative members harvesting rice in the fields")
    val altText: String?,

    @Schema(description = "Searchable tags associated with this media", example = "harvest,rice,members,agriculture")
    val tags: String?,

    @Schema(description = "License information for the media", example = "CC BY-SA 4.0")
    val license: String?,

    @Schema(description = "Attribution information if required", example = "Photo by Ramesh Sharma")
    val attribution: String?,

    @Schema(description = "Display order for multiple media items", example = "10")
    val sortOrder: Int?,

    @Schema(description = "Whether this is the primary media item of its type")
    val isPrimary: Boolean?,

    @Schema(description = "Whether this media should be featured prominently")
    val isFeatured: Boolean?,

    @Schema(description = "Visibility status for the media")
    val visibilityStatus: MediaVisibilityStatus?
)
