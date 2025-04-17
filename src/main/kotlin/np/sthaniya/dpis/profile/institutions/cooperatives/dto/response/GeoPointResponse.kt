package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Response DTO for geographic point coordinates.
 */
@Schema(description = "Geographic point coordinates")
data class GeoPointResponse(
    @Schema(description = "Longitude coordinate", example = "85.3240")
    val longitude: Double,

    @Schema(description = "Latitude coordinate", example = "27.7172")
    val latitude: Double
)
