package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.service

import java.util.UUID
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.CreateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.UpdateWardWiseHouseholdsOnLoanDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.request.WardWiseHouseholdsOnLoanFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanSummaryResponse

/** Service interface for managing ward-wise households on loan data. */
interface WardWiseHouseholdsOnLoanService {

    /**
     * Create new ward-wise households on loan data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseHouseholdsOnLoan(
            createDto: CreateWardWiseHouseholdsOnLoanDto
    ): WardWiseHouseholdsOnLoanResponse

    /**
     * Update existing ward-wise households on loan data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseHouseholdsOnLoan(
            id: UUID,
            updateDto: UpdateWardWiseHouseholdsOnLoanDto
    ): WardWiseHouseholdsOnLoanResponse

    /**
     * Delete ward-wise households on loan data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseHouseholdsOnLoan(id: UUID)

    /**
     * Get ward-wise households on loan data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseHouseholdsOnLoanById(id: UUID): WardWiseHouseholdsOnLoanResponse

    /**
     * Get all ward-wise households on loan data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseHouseholdsOnLoan(
            filter: WardWiseHouseholdsOnLoanFilterDto?
    ): List<WardWiseHouseholdsOnLoanResponse>

    /**
     * Get ward-wise households on loan data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return The data response or null if not found
     */
    fun getWardWiseHouseholdsOnLoanByWard(
            wardNumber: Int
    ): WardWiseHouseholdsOnLoanResponse?

    /**
     * Get summary of households on loan across all wards.
     *
     * @return Summary response with total households on loan
     */
    fun getHouseholdsOnLoanSummary(): WardWiseHouseholdsOnLoanSummaryResponse
}
