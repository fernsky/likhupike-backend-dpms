package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response

/**
 * Response DTO for summarized ward population data.
 *
 * @property wardNumber The ward number
 * @property wardName The ward name (if available)
 * @property totalPopulation The latest total population for this ward
 * @property year The year of the data
 */
data class WardPopulationSummaryResponse(
        val wardNumber: Int,
        val wardName: String?,
        val totalPopulation: Int?,
        val year: Int
)
