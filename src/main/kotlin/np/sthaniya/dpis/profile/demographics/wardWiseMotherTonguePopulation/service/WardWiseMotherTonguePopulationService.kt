package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.CreateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.UpdateWardWiseMotherTonguePopulationDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.request.WardWiseMotherTonguePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.LanguagePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.dto.response.WardWiseMotherTonguePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType

/** Service interface for managing ward-wise mother tongue population data. */
interface WardWiseMotherTonguePopulationService {

    /**
     * Create new ward-wise mother tongue population data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseMotherTonguePopulation(
            createDto: CreateWardWiseMotherTonguePopulationDto
    ): WardWiseMotherTonguePopulationResponse

    /**
     * Update existing ward-wise mother tongue population data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseMotherTonguePopulation(
            id: UUID,
            updateDto: UpdateWardWiseMotherTonguePopulationDto
    ): WardWiseMotherTonguePopulationResponse

    /**
     * Delete ward-wise mother tongue population data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseMotherTonguePopulation(id: UUID)

    /**
     * Get ward-wise mother tongue population data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseMotherTonguePopulationById(id: UUID): WardWiseMotherTonguePopulationResponse

    /**
     * Get all ward-wise mother tongue population data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseMotherTonguePopulation(
            filter: WardWiseMotherTonguePopulationFilterDto?
    ): List<WardWiseMotherTonguePopulationResponse>

    /**
     * Get all ward-wise mother tongue population data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseMotherTonguePopulationByWard(
            wardNumber: Int
    ): List<WardWiseMotherTonguePopulationResponse>

    /**
     * Get all ward-wise mother tongue population data for a specific language type.
     *
     * @param languageType The language type to filter by
     * @return List of data responses
     */
    fun getWardWiseMotherTonguePopulationByLanguage(
            languageType: LanguageType
    ): List<WardWiseMotherTonguePopulationResponse>

    /**
     * Get summary of language population across all wards.
     *
     * @return List of summary responses by language
     */
    fun getLanguagePopulationSummary(): List<LanguagePopulationSummaryResponse>

    /**
     * Get summary of total population by ward across all languages.
     *
     * @return List of summary responses by ward
     */
    fun getWardPopulationSummary(): List<WardPopulationSummaryResponse>
}
