package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.service

import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.OccupationType
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.CreateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.UpdateWardWiseMajorOccupationDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.request.WardWiseMajorOccupationFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.OccupationPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardOccupationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.dto.response.WardWiseMajorOccupationResponse

/** Service interface for managing ward-wise major occupation data. */
interface WardWiseMajorOccupationService {

    /**
     * Create new ward-wise major occupation data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseMajorOccupation(
            createDto: CreateWardWiseMajorOccupationDto
    ): WardWiseMajorOccupationResponse

    /**
     * Update existing ward-wise major occupation data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseMajorOccupation(
            id: UUID,
            updateDto: UpdateWardWiseMajorOccupationDto
    ): WardWiseMajorOccupationResponse

    /**
     * Delete ward-wise major occupation data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseMajorOccupation(id: UUID)

    /**
     * Get ward-wise major occupation data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseMajorOccupationById(id: UUID): WardWiseMajorOccupationResponse

    /**
     * Get all ward-wise major occupation data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseMajorOccupation(
            filter: WardWiseMajorOccupationFilterDto?
    ): List<WardWiseMajorOccupationResponse>

    /**
     * Get all ward-wise major occupation data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseMajorOccupationByWard(
            wardNumber: Int
    ): List<WardWiseMajorOccupationResponse>

    /**
     * Get all ward-wise major occupation data for a specific occupation type.
     *
     * @param occupation The occupation type to filter by
     * @return List of data responses
     */
    fun getWardWiseMajorOccupationByOccupation(
            occupation: OccupationType
    ): List<WardWiseMajorOccupationResponse>

    /**
     * Get summary of occupation population across all wards.
     *
     * @return List of summary responses by occupation
     */
    fun getOccupationPopulationSummary(): List<OccupationPopulationSummaryResponse>

    /**
     * Get summary of total population by ward across all occupations.
     *
     * @return List of summary responses by ward
     */
    fun getWardOccupationSummary(): List<WardOccupationSummaryResponse>
}
