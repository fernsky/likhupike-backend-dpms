package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response

import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * Response DTO for summarized gender population data.
 *
 * @property gender The gender
 * @property totalPopulation The total population for this gender
 */
data class GenderPopulationSummaryResponse(
        val gender: Gender,
        val totalPopulation: Long
)
