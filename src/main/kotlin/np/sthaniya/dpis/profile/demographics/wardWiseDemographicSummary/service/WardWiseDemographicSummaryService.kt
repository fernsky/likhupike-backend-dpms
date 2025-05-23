package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.CreateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.UpdateWardWiseDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.request.WardWiseDemographicSummaryFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.dto.response.WardWiseDemographicSummaryResponse

/** Service interface for managing ward-wise demographic summary data. */
interface WardWiseDemographicSummaryService {

    /**
     * Create new ward-wise demographic summary data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseDemographicSummary(
            createDto: CreateWardWiseDemographicSummaryDto
    ): WardWiseDemographicSummaryResponse

    /**
     * Update existing ward-wise demographic summary data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseDemographicSummary(
            id: UUID,
            updateDto: UpdateWardWiseDemographicSummaryDto
    ): WardWiseDemographicSummaryResponse

    /**
     * Delete ward-wise demographic summary data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseDemographicSummary(id: UUID)

    /**
     * Get ward-wise demographic summary data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseDemographicSummaryById(id: UUID): WardWiseDemographicSummaryResponse

    /**
     * Get all ward-wise demographic summary data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseDemographicSummary(
            filter: WardWiseDemographicSummaryFilterDto?
    ): List<WardWiseDemographicSummaryResponse>

    /**
     * Get ward-wise demographic summary data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return The data response or null if not found
     */
    fun getWardWiseDemographicSummaryByWard(
            wardNumber: Int
    ): WardWiseDemographicSummaryResponse?
}
