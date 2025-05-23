package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response

import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup

/**
 * Response DTO for summarized absentee population data by age group and gender.
 *
 * @property ageGroup The age group
 * @property gender The gender
 * @property totalPopulation The total absentee population for this age group and gender
 */
data class AgeGroupGenderPopulationSummaryResponse(
        val ageGroup: AbsenteeAgeGroup,
        val gender: Gender,
        val totalPopulation: Long
)
