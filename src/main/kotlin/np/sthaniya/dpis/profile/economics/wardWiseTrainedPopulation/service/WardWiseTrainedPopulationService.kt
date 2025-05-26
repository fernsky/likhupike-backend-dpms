package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.service

import java.util.UUID
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.CreateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.UpdateWardWiseTrainedPopulationDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.request.WardWiseTrainedPopulationFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.TrainedPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardTrainedPopulationResponse
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.dto.response.WardWiseTrainedPopulationResponse

/** Service interface for managing ward-wise trained population data. */
interface WardWiseTrainedPopulationService {

    /**
     * Create new ward-wise trained population data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseTrainedPopulation(
            createDto: CreateWardWiseTrainedPopulationDto
    ): WardWiseTrainedPopulationResponse

    /**
     * Update existing ward-wise trained population data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseTrainedPopulation(
            id: UUID,
            updateDto: UpdateWardWiseTrainedPopulationDto
    ): WardWiseTrainedPopulationResponse

    /**
     * Delete ward-wise trained population data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseTrainedPopulation(id: UUID)

    /**
     * Get ward-wise trained population data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseTrainedPopulationById(id: UUID): WardWiseTrainedPopulationResponse

    /**
     * Get all ward-wise trained population data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseTrainedPopulation(
            filter: WardWiseTrainedPopulationFilterDto?
    ): List<WardWiseTrainedPopulationResponse>

    /**
     * Get ward-wise trained population data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return The data response or null if not found
     */
    fun getWardWiseTrainedPopulationByWard(
            wardNumber: Int
    ): WardWiseTrainedPopulationResponse?

    /**
     * Get summary of total trained population across all wards.
     *
     * @return Summary response with total trained population
     */
    fun getTrainedPopulationSummary(): TrainedPopulationSummaryResponse

    /**
     * Get summary of trained population by ward.
     *
     * @return List of ward summary responses
     */
    fun getWardTrainedPopulationSummary(): List<WardTrainedPopulationResponse>
}
