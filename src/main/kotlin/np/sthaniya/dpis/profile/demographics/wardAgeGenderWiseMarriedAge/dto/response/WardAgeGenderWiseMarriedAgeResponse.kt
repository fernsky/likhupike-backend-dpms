package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup

/**
 * Response DTO for ward-age-gender-wise married age data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property ageGroup The age group at marriage
 * @property gender The gender
 * @property population The population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardAgeGenderWiseMarriedAgeResponse(
        val id: UUID,
        val wardNumber: Int,
        val ageGroup: MarriedAgeGroup,
        val gender: Gender,
        val population: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
