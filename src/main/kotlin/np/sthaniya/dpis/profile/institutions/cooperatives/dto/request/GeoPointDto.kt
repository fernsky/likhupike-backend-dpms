package np.sthaniya.dpis.profile.institutions.cooperatives.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*

/**
 * DTO for geographic point coordinates.
 */
@Schema(description = "Geographic point coordinates")
data class GeoPointDto(
    @field:NotNull(message = "Longitude is required")
    @field:Min(-180, message = "Longitude must be between -180 and 180")
    @field:Max(180, message = "Longitude must be between -180 and 180")
    @Schema(description = "Longitude coordinate", example = "85.3240")
    val longitude: Double,

    @field:NotNull(message = "Latitude is required")
    @field:Min(-90, message = "Latitude must be between -90 and 90")
    @field:Max(90, message = "Latitude must be between -90 and 90")
    @Schema(description = "Latitude coordinate", example = "27.7172")
    val latitude: Double
)
