package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request

/**
 * DTO for filtering ward-wise demographic summary data.
 *
 * @property wardNumber Optional ward number filter
 */
data class WardWiseDemographicSummaryFilterDto(
        val wardNumber: Int? = null
)
