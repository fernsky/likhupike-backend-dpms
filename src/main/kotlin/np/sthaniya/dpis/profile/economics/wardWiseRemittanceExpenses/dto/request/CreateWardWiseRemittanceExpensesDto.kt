package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType

/**
 * DTO for creating ward-wise remittance expenses data.
 *
 * @property wardNumber The ward number (must be positive)
 * @property remittanceExpense The type of remittance expense
 * @property households The number of households (must be non-negative)
 */
data class CreateWardWiseRemittanceExpensesDto(
        @field:NotNull(message = "Ward number is required")
        @field:Min(value = 1, message = "Ward number must be positive")
        val wardNumber: Int,
        @field:NotNull(message = "Remittance expense type is required")
        val remittanceExpense: RemittanceExpenseType,
        @field:NotNull(message = "Number of households is required")
        @field:Min(value = 0, message = "Number of households must be non-negative")
        val households: Int
)
