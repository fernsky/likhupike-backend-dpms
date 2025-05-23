package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response

import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup

/**
 * Response DTO for summarized age group population data.
 *
 * @property ageGroup The age group
 * @property totalPopulation The total population for this age group
 */
data class AgeGroupPopulationSummaryResponse(
        val ageGroup: AgeGroup,
        val totalPopulation: Long
)
