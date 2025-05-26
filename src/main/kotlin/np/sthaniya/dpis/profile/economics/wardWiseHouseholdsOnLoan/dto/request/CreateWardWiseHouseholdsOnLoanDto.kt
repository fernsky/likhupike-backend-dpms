package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

/**
 * DTO for creating ward-wise households on loan data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property households The number of households with loans (must be non-negative)
 */
data class CreateWardWiseHouseholdsOnLoanDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Households is required")
        @field:Min(value = 0, message = "Households must be non-negative")
        val households: Int
)
