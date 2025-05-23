package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response

/**
 * Response DTO for summarized ward population data.
 *
 * @property wardNumber The ward number
 * @property totalPopulation The total population for this ward
 */
data class WardPopulationSummaryResponse(val wardNumber: Int, val totalPopulation: Long)
