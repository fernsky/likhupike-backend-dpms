package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response

import java.time.Instant
import java.util.UUID

/**
 * Response DTO for ward-wise trained population data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property trainedPopulation The trained population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseTrainedPopulationResponse(
        val id: UUID,
        val wardNumber: Int,
        val trainedPopulation: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
