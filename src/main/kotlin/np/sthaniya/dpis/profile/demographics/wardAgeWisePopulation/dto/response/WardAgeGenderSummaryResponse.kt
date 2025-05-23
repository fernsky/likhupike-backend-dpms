package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response

import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * Response DTO for summarized ward population data by age group and gender.
 *
 * @property wardNumber The ward number
 * @property ageGroup The age group
 * @property gender The gender
 * @property totalPopulation The total population
 */
data class WardAgeGenderSummaryResponse(
        val wardNumber: Int,
        val ageGroup: AgeGroup,
        val gender: Gender,
        val totalPopulation: Long
)
