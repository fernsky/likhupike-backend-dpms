package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.CreateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.UpdateWardWiseCastePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.request.WardWiseCastePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.CastePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.dto.response.WardWiseCastePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType

/** Service interface for managing ward-wise caste population data. */
interface WardWiseCastePopulationService {

    /**
     * Create new ward-wise caste population data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseCastePopulation(
            createDto: CreateWardWiseCastePopulationDto
    ): WardWiseCastePopulationResponse

    /**
     * Update existing ward-wise caste population data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseCastePopulation(
            id: UUID,
            updateDto: UpdateWardWiseCastePopulationDto
    ): WardWiseCastePopulationResponse

    /**
     * Delete ward-wise caste population data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseCastePopulation(id: UUID)

    /**
     * Get ward-wise caste population data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseCastePopulationById(id: UUID): WardWiseCastePopulationResponse

    /**
     * Get all ward-wise caste population data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseCastePopulation(
            filter: WardWiseCastePopulationFilterDto?
    ): List<WardWiseCastePopulationResponse>

    /**
     * Get all ward-wise caste population data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseCastePopulationByWard(
            wardNumber: Int
    ): List<WardWiseCastePopulationResponse>

    /**
     * Get all ward-wise caste population data for a specific caste type.
     *
     * @param casteType The caste type to filter by
     * @return List of data responses
     */
    fun getWardWiseCastePopulationByCaste(
            casteType: CasteType
    ): List<WardWiseCastePopulationResponse>

    /**
     * Get summary of caste population across all wards.
     *
     * @return List of summary responses by caste
     */
    fun getCastePopulationSummary(): List<CastePopulationSummaryResponse>

    /**
     * Get summary of total population by ward across all castes.
     *
     * @return List of summary responses by ward
     */
    fun getWardPopulationSummary(): List<WardPopulationSummaryResponse>
}
