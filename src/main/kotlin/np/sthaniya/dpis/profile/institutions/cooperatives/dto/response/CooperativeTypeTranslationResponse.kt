package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import java.util.UUID

/**
 * Response DTO for cooperative type translation.
 */
@Schema(description = "Cooperative type translation data")
data class CooperativeTypeTranslationResponse(
    @Schema(description = "Unique identifier for the translation")
    val id: UUID,

    @Schema(description = "Type of cooperative")
    val cooperativeType: CooperativeType,

    @Schema(description = "Locale for this translation", example = "ne")
    val locale: String,

    @Schema(description = "Localized name of the cooperative type", example = "कृषि सहकारी")
    val name: String,

    @Schema(description = "Localized description of this cooperative type")
    val description: String?
)
