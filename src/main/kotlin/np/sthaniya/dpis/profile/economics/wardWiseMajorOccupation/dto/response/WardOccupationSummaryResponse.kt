package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response

/**
 * Response DTO for summarized ward occupation data.
 *
 * @property wardNumber The ward number
 * @property totalPopulation The total population for this ward
 */
data class WardOccupationSummaryResponse(
        val wardNumber: Int,
        val totalPopulation: Long
)
