package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType

/**
 * Response DTO for ward-wise mother tongue population data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property languageType The type of language
 * @property population The population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseMotherTonguePopulationResponse(
        val id: UUID,
        val wardNumber: Int,
        val languageType: LanguageType,
        val population: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
