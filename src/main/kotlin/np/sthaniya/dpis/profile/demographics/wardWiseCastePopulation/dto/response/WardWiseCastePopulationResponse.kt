package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType

/**
 * Response DTO for ward-wise caste population data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property casteType The type of caste
 * @property population The population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseCastePopulationResponse(
        val id: UUID,
        val wardNumber: Int,
        val casteType: CasteType,
        val population: Int?,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
