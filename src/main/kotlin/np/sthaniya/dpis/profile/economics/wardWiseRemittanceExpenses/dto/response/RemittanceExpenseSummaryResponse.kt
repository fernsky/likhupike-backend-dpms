package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response

import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType

/**
 * Response DTO for summarized remittance expense data.
 *
 * @property remittanceExpense The type of remittance expense
 * @property totalHouseholds The total number of households for this remittance expense type
 */
data class RemittanceExpenseSummaryResponse(
        val remittanceExpense: RemittanceExpenseType,
        val totalHouseholds: Long
)
