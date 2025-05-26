package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.economics.common.model.LoanUseType

/**
 * DTO for creating ward-wise households loan use data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property loanUse The type of loan use
 * @property households The number of households (must be non-negative)
 */
data class CreateWardWiseHouseholdsLoanUseDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Loan use type is required")
        val loanUse: LoanUseType,
        @field:NotNull(message = "Number of households is required")
        @field:Min(value = 0, message = "Number of households must be non-negative")
        val households: Int
)
