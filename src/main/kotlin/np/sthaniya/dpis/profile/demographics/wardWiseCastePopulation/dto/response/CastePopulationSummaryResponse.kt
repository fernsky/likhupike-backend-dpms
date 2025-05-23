package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response

import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType

/**
 * Response DTO for summarized caste population data.
 *
 * @property casteType The type of caste
 * @property totalPopulation The total population for this caste type
 */
data class CastePopulationSummaryResponse(
        val casteType: CasteType,
        val totalPopulation: Long
)
