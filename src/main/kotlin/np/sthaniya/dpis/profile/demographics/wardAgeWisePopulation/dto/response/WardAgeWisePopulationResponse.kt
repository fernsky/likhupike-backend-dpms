package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * Response DTO for ward-age-wise population data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property ageGroup The age group
 * @property gender The gender
 * @property population The population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardAgeWisePopulationResponse(
        val id: UUID,
        val wardNumber: Int,
        val ageGroup: AgeGroup,
        val gender: Gender,
        val population: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
