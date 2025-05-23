package np.sthaniya.dpis.profile.demographics.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.model.ReligionType

/**
 * Response DTO for ward-wise religion population data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property religionType The type of religion
 * @property population The population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseReligionPopulationResponse(
        val id: UUID,
        val wardNumber: Int,
        val religionType: ReligionType,
        val population: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
