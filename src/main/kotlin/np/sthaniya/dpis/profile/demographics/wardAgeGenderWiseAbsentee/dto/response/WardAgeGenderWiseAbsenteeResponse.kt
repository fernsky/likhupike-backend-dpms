package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup

/**
 * Response DTO for ward-age-gender-wise absentee data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property ageGroup The age group for absentee population
 * @property gender The gender for absentee population
 * @property population The absentee population count
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardAgeGenderWiseAbsenteeResponse(
        val id: UUID,
        val wardNumber: Int,
        val ageGroup: AbsenteeAgeGroup,
        val gender: Gender,
        val population: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
