package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response

/**
 * Response DTO for summarized ward households data.
 *
 * @property wardNumber The ward number
 * @property totalHouseholds The total number of households with loans in this ward
 */
data class WardHouseholdsSummaryResponse(
        val wardNumber: Int,
        val totalHouseholds: Long
)
