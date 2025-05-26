package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request

import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType

/**
 * DTO for filtering ward-wise remittance expenses data.
 *
 * @property wardNumber Optional ward number filter
 * @property remittanceExpense Optional remittance expense type filter
 */
data class WardWiseRemittanceExpensesFilterDto(
        val wardNumber: Int? = null,
        val remittanceExpense: RemittanceExpenseType? = null
)
