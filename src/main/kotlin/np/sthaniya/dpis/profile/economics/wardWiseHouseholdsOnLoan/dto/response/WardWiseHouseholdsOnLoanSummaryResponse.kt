package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response

/**
 * Response DTO for total households on loan summary.
 *
 * @property totalHouseholdsOnLoan The total number of households with loans across all wards
 */
data class WardWiseHouseholdsOnLoanSummaryResponse(
        val totalHouseholdsOnLoan: Long
)
