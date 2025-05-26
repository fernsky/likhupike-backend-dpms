package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.OccupationType

/**
 * Response DTO for ward-wise major occupation data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property occupation The type of occupation
 * @property population The population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseMajorOccupationResponse(
        val id: UUID,
        val wardNumber: Int,
        val occupation: OccupationType,
        val population: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
