package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.service

import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.CreateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.UpdateWardWiseRemittanceExpensesDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.request.WardWiseRemittanceExpensesFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.RemittanceExpenseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardRemittanceSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.dto.response.WardWiseRemittanceExpensesResponse

/** Service interface for managing ward-wise remittance expenses data. */
interface WardWiseRemittanceExpensesService {

    /**
     * Create new ward-wise remittance expenses data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseRemittanceExpenses(
            createDto: CreateWardWiseRemittanceExpensesDto
    ): WardWiseRemittanceExpensesResponse

    /**
     * Update existing ward-wise remittance expenses data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseRemittanceExpenses(
            id: UUID,
            updateDto: UpdateWardWiseRemittanceExpensesDto
    ): WardWiseRemittanceExpensesResponse

    /**
     * Delete ward-wise remittance expenses data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseRemittanceExpenses(id: UUID)

    /**
     * Get ward-wise remittance expenses data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseRemittanceExpensesById(id: UUID): WardWiseRemittanceExpensesResponse

    /**
     * Get all ward-wise remittance expenses data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseRemittanceExpenses(
            filter: WardWiseRemittanceExpensesFilterDto?
    ): List<WardWiseRemittanceExpensesResponse>

    /**
     * Get all ward-wise remittance expenses data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseRemittanceExpensesByWard(
            wardNumber: Int
    ): List<WardWiseRemittanceExpensesResponse>

    /**
     * Get all ward-wise remittance expenses data for a specific remittance expense type.
     *
     * @param remittanceExpense The remittance expense type to filter by
     * @return List of data responses
     */
    fun getWardWiseRemittanceExpensesByRemittanceExpense(
            remittanceExpense: RemittanceExpenseType
    ): List<WardWiseRemittanceExpensesResponse>

    /**
     * Get summary of remittance expenses across all wards.
     *
     * @return List of summary responses by remittance expense type
     */
    fun getRemittanceExpenseSummary(): List<RemittanceExpenseSummaryResponse>

    /**
     * Get summary of total households by ward across all remittance expense types.
     *
     * @return List of summary responses by ward
     */
    fun getWardRemittanceSummary(): List<WardRemittanceSummaryResponse>
}
