package np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.dto.response

import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.ReligionType

/**
 * Response DTO for summarized religion population data.
 *
 * @property religionType The type of religion
 * @property totalPopulation The total population for this religion type
 */
data class ReligionPopulationSummaryResponse(
        val religionType: ReligionType,
        val totalPopulation: Long
)
