package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response

/**
 * Response DTO for summarized absentee population data by ward.
 *
 * @property wardNumber The ward number
 * @property totalPopulation The total absentee population for this ward
 */
data class WardAbsenteePopulationSummaryResponse(
        val wardNumber: Int,
        val totalPopulation: Long
)
