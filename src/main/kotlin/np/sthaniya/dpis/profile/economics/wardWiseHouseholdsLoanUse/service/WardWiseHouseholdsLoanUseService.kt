package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.service

import java.util.UUID
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.CreateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.UpdateWardWiseHouseholdsLoanUseDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.request.WardWiseHouseholdsLoanUseFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.LoanUseSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardHouseholdsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.dto.response.WardWiseHouseholdsLoanUseResponse
import np.sthaniya.dpis.profile.economics.common.model.LoanUseType

/** Service interface for managing ward-wise households loan use data. */
interface WardWiseHouseholdsLoanUseService {

    /**
     * Create new ward-wise households loan use data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseHouseholdsLoanUse(
            createDto: CreateWardWiseHouseholdsLoanUseDto
    ): WardWiseHouseholdsLoanUseResponse

    /**
     * Update existing ward-wise households loan use data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseHouseholdsLoanUse(
            id: UUID,
            updateDto: UpdateWardWiseHouseholdsLoanUseDto
    ): WardWiseHouseholdsLoanUseResponse

    /**
     * Delete ward-wise households loan use data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseHouseholdsLoanUse(id: UUID)

    /**
     * Get ward-wise households loan use data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseHouseholdsLoanUseById(id: UUID): WardWiseHouseholdsLoanUseResponse

    /**
     * Get all ward-wise households loan use data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseHouseholdsLoanUse(
            filter: WardWiseHouseholdsLoanUseFilterDto?
    ): List<WardWiseHouseholdsLoanUseResponse>

    /**
     * Get all ward-wise households loan use data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseHouseholdsLoanUseByWard(
            wardNumber: Int
    ): List<WardWiseHouseholdsLoanUseResponse>

    /**
     * Get all ward-wise households loan use data for a specific loan use type.
     *
     * @param loanUse The loan use type to filter by
     * @return List of data responses
     */
    fun getWardWiseHouseholdsLoanUseByLoanUse(
            loanUse: LoanUseType
    ): List<WardWiseHouseholdsLoanUseResponse>

    /**
     * Get summary of households by loan use type across all wards.
     *
     * @return List of summary responses by loan use type
     */
    fun getLoanUseSummary(): List<LoanUseSummaryResponse>

    /**
     * Get summary of total households by ward across all loan use types.
     *
     * @return List of summary responses by ward
     */
    fun getWardHouseholdsSummary(): List<WardHouseholdsSummaryResponse>
}
