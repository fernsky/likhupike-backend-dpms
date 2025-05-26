package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request

import np.sthaniya.dpis.profile.economics.common.model.LoanUseType

/**
 * DTO for filtering ward-wise households loan use data.
 *
 * @property wardNumber Optional ward number filter
 * @property loanUse Optional loan use type filter
 */
data class WardWiseHouseholdsLoanUseFilterDto(
        val wardNumber: Int? = null,
        val loanUse: LoanUseType? = null
)
