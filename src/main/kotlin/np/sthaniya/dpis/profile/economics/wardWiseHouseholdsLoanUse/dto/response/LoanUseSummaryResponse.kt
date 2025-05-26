package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response

import np.sthaniya.dpis.profile.economics.common.model.LoanUseType

/**
 * Response DTO for summarized loan use data.
 *
 * @property loanUse The type of loan use
 * @property totalHouseholds The total number of households for this loan use type
 */
data class LoanUseSummaryResponse(
        val loanUse: LoanUseType,
        val totalHouseholds: Long
)
