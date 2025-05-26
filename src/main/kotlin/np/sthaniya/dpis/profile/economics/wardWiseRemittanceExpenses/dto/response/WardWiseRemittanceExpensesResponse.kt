package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response

import java.time.Instant
import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType

/**
 * Response DTO for ward-wise remittance expenses data.
 *
 * @property id Unique identifier
 * @property wardNumber The ward number
 * @property remittanceExpense The type of remittance expense
 * @property households The number of households
 * @property createdAt The creation timestamp
 * @property updatedAt The last update timestamp
 */
data class WardWiseRemittanceExpensesResponse(
        val id: UUID,
        val wardNumber: Int,
        val remittanceExpense: RemittanceExpenseType,
        val households: Int,
        val createdAt: Instant?,
        val updatedAt: Instant?
)
