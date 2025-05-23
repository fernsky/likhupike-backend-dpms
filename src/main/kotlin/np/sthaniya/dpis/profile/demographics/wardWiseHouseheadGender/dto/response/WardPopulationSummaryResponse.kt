package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response

/**
 * Response DTO for summarized ward population data.
 *
 * @property wardNumber The ward number
 * @property wardName Optional ward name for reference
 * @property totalPopulation The total population for this ward
 */
data class WardPopulationSummaryResponse(
        val wardNumber: Int,
        val wardName: String?,
        val totalPopulation: Long
)
