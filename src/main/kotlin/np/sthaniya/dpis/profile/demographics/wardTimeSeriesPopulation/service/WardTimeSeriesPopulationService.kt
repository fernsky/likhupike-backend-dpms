package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.CreateWardTimeSeriesPopulationDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.UpdateWardTimeSeriesPopulationDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.request.WardTimeSeriesPopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.WardTimeSeriesPopulationResponse
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.dto.response.YearPopulationSummaryResponse

/** Service interface for managing ward time series population data. */
interface WardTimeSeriesPopulationService {

    /**
     * Create new ward time series population data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardTimeSeriesPopulation(
            createDto: CreateWardTimeSeriesPopulationDto
    ): WardTimeSeriesPopulationResponse

    /**
     * Update existing ward time series population data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardTimeSeriesPopulation(
            id: UUID,
            updateDto: UpdateWardTimeSeriesPopulationDto
    ): WardTimeSeriesPopulationResponse

    /**
     * Delete ward time series population data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardTimeSeriesPopulation(id: UUID)

    /**
     * Get ward time series population data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardTimeSeriesPopulationById(id: UUID): WardTimeSeriesPopulationResponse

    /**
     * Get all ward time series population data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardTimeSeriesPopulation(
            filter: WardTimeSeriesPopulationFilterDto?
    ): List<WardTimeSeriesPopulationResponse>

    /**
     * Get all ward time series population data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardTimeSeriesPopulationByWard(
            wardNumber: Int
    ): List<WardTimeSeriesPopulationResponse>

    /**
     * Get all ward time series population data for a specific year.
     *
     * @param year The year to filter by
     * @return List of data responses
     */
    fun getWardTimeSeriesPopulationByYear(
            year: Int
    ): List<WardTimeSeriesPopulationResponse>

    /**
     * Get latest population data for each ward.
     *
     * @return List of summary responses by ward
     */
    fun getLatestWardPopulationSummary(): List<WardPopulationSummaryResponse>

    /**
     * Get summary of total population by year across all wards.
     *
     * @return List of summary responses by year
     */
    fun getYearPopulationSummary(): List<YearPopulationSummaryResponse>
}
