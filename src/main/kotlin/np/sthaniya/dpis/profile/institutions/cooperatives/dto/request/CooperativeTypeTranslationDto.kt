package np.sthaniya.dpis.profile.institutions.cooperatives.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType

/**
 * DTO for creating or updating a cooperative type translation.
 */
@Schema(description = "Cooperative type translation data")
data class CooperativeTypeTranslationDto(
    @field:NotNull(message = "Cooperative type is required")
    @Schema(description = "Type of cooperative")
    val cooperativeType: CooperativeType,

    @field:NotBlank(message = "Locale is required")
    @field:Size(min = 2, max = 10, message = "Locale must be between 2 and 10 characters")
    @Schema(description = "Locale for this translation", example = "ne")
    val locale: String,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    @Schema(description = "Localized name of the cooperative type", example = "कृषि सहकारी")
    val name: String,

    @Schema(description = "Localized description of this cooperative type")
    val description: String?
)
