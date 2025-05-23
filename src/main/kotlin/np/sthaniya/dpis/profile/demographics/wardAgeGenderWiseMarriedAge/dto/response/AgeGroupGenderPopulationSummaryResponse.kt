package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response

import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup

/**
 * Response DTO for summarized age group and gender population data.
 *
 * @property ageGroup The age group at marriage
 * @property gender The gender
 * @property totalPopulation The total population for this age group and gender
 */
data class AgeGroupGenderPopulationSummaryResponse(
        val ageGroup: MarriedAgeGroup,
        val gender: Gender,
        val totalPopulation: Long
)
