package np.sthaniya.dpis.profile.demographics.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.dto.request.CreateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.dto.request.UpdateWardWiseReligionPopulationDto
import np.sthaniya.dpis.profile.demographics.dto.request.WardWiseReligionPopulationFilterDto
import np.sthaniya.dpis.profile.demographics.dto.response.ReligionPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.dto.response.WardWiseReligionPopulationResponse
import np.sthaniya.dpis.profile.demographics.model.ReligionType

/** Service interface for managing ward-wise religion population data. */
interface WardWiseReligionPopulationService {

    /**
     * Create new ward-wise religion population data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseReligionPopulation(
            createDto: CreateWardWiseReligionPopulationDto
    ): WardWiseReligionPopulationResponse

    /**
     * Update existing ward-wise religion population data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseReligionPopulation(
            id: UUID,
            updateDto: UpdateWardWiseReligionPopulationDto
    ): WardWiseReligionPopulationResponse

    /**
     * Delete ward-wise religion population data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseReligionPopulation(id: UUID)

    /**
     * Get ward-wise religion population data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseReligionPopulationById(id: UUID): WardWiseReligionPopulationResponse

    /**
     * Get all ward-wise religion population data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseReligionPopulation(
            filter: WardWiseReligionPopulationFilterDto?
    ): List<WardWiseReligionPopulationResponse>

    /**
     * Get all ward-wise religion population data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseReligionPopulationByWard(
            wardNumber: Int
    ): List<WardWiseReligionPopulationResponse>

    /**
     * Get all ward-wise religion population data for a specific religion type.
     *
     * @param religionType The religion type to filter by
     * @return List of data responses
     */
    fun getWardWiseReligionPopulationByReligion(
            religionType: ReligionType
    ): List<WardWiseReligionPopulationResponse>

    /**
     * Get summary of religion population across all wards.
     *
     * @return List of summary responses by religion
     */
    fun getReligionPopulationSummary(): List<ReligionPopulationSummaryResponse>

    /**
     * Get summary of total population by ward across all religions.
     *
     * @return List of summary responses by ward
     */
    fun getWardPopulationSummary(): List<WardPopulationSummaryResponse>
}
