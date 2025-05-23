package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response

import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType

/**
 * Response DTO for summarized educational level population data.
 *
 * @property educationalLevel The educational level
 * @property totalPopulation The total population for this educational level
 */
data class EducationalLevelPopulationSummaryResponse(
        val educationalLevel: EducationalLevelType,
        val totalPopulation: Long
)
