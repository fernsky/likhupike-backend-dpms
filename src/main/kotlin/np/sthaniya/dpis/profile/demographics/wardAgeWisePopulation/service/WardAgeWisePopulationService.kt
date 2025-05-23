package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.CreateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.UpdateWardAgeWisePopulationDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.request.WardAgeWisePopulationFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.AgeGroupPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeGenderSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.dto.response.WardAgeWisePopulationResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/** Service interface for managing ward-age-wise population data. */
interface WardAgeWisePopulationService {

    /**
     * Create new ward-age-wise population data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardAgeWisePopulation(
            createDto: CreateWardAgeWisePopulationDto
    ): WardAgeWisePopulationResponse

    /**
     * Update existing ward-age-wise population data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardAgeWisePopulation(
            id: UUID,
            updateDto: UpdateWardAgeWisePopulationDto
    ): WardAgeWisePopulationResponse

    /**
     * Delete ward-age-wise population data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardAgeWisePopulation(id: UUID)

    /**
     * Get ward-age-wise population data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardAgeWisePopulationById(id: UUID): WardAgeWisePopulationResponse

    /**
     * Get all ward-age-wise population data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardAgeWisePopulation(
            filter: WardAgeWisePopulationFilterDto?
    ): List<WardAgeWisePopulationResponse>

    /**
     * Get all ward-age-wise population data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardAgeWisePopulationByWard(
            wardNumber: Int
    ): List<WardAgeWisePopulationResponse>

    /**
     * Get all ward-age-wise population data for a specific age group.
     *
     * @param ageGroup The age group to filter by
     * @return List of data responses
     */
    fun getWardAgeWisePopulationByAgeGroup(
            ageGroup: AgeGroup
    ): List<WardAgeWisePopulationResponse>

    /**
     * Get all ward-age-wise population data for a specific gender.
     *
     * @param gender The gender to filter by
     * @return List of data responses
     */
    fun getWardAgeWisePopulationByGender(
            gender: Gender
    ): List<WardAgeWisePopulationResponse>

    /**
     * Get summary of population by age group across all wards.
     *
     * @return List of summary responses by age group
     */
    fun getAgeGroupPopulationSummary(): List<AgeGroupPopulationSummaryResponse>

    /**
     * Get summary of population by gender across all wards.
     *
     * @return List of summary responses by gender
     */
    fun getGenderPopulationSummary(): List<GenderPopulationSummaryResponse>

    /**
     * Get summary of population by ward, age group, and gender.
     *
     * @return List of detailed summary responses
     */
    fun getWardAgeGenderSummary(): List<WardAgeGenderSummaryResponse>
}
