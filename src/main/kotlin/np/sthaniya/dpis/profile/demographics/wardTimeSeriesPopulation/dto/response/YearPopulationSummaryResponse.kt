package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response

/**
 * Response DTO for summarized year-wise population data.
 *
 * @property year The census year
 * @property totalPopulation The total population for this year
 */
data class YearPopulationSummaryResponse(
        val year: Int,
        val totalPopulation: Long
)
