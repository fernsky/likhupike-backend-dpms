package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.CreateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.UpdateWardAgeGenderWiseMarriedAgeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.request.WardAgeGenderWiseMarriedAgeFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.dto.response.WardAgeGenderWiseMarriedAgeResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup

/** Service interface for managing ward-age-gender-wise married age data. */
interface WardAgeGenderWiseMarriedAgeService {

    /**
     * Create new ward-age-gender-wise married age data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardAgeGenderWiseMarriedAge(
            createDto: CreateWardAgeGenderWiseMarriedAgeDto
    ): WardAgeGenderWiseMarriedAgeResponse

    /**
     * Update existing ward-age-gender-wise married age data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardAgeGenderWiseMarriedAge(
            id: UUID,
            updateDto: UpdateWardAgeGenderWiseMarriedAgeDto
    ): WardAgeGenderWiseMarriedAgeResponse

    /**
     * Delete ward-age-gender-wise married age data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardAgeGenderWiseMarriedAge(id: UUID)

    /**
     * Get ward-age-gender-wise married age data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardAgeGenderWiseMarriedAgeById(id: UUID): WardAgeGenderWiseMarriedAgeResponse

    /**
     * Get all ward-age-gender-wise married age data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardAgeGenderWiseMarriedAge(
            filter: WardAgeGenderWiseMarriedAgeFilterDto?
    ): List<WardAgeGenderWiseMarriedAgeResponse>

    /**
     * Get all ward-age-gender-wise married age data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardAgeGenderWiseMarriedAgeByWard(
            wardNumber: Int
    ): List<WardAgeGenderWiseMarriedAgeResponse>

    /**
     * Get all ward-age-gender-wise married age data for a specific age group.
     *
     * @param ageGroup The age group to filter by
     * @return List of data responses
     */
    fun getWardAgeGenderWiseMarriedAgeByAgeGroup(
            ageGroup: MarriedAgeGroup
    ): List<WardAgeGenderWiseMarriedAgeResponse>

    /**
     * Get all ward-age-gender-wise married age data for a specific gender.
     *
     * @param gender The gender to filter by
     * @return List of data responses
     */
    fun getWardAgeGenderWiseMarriedAgeByGender(
            gender: Gender
    ): List<WardAgeGenderWiseMarriedAgeResponse>

    /**
     * Get summary of age group and gender population across all wards.
     *
     * @return List of summary responses by age group and gender
     */
    fun getAgeGroupGenderPopulationSummary(): List<AgeGroupGenderPopulationSummaryResponse>

    /**
     * Get summary of total population by ward across all age groups and genders.
     *
     * @return List of summary responses by ward
     */
    fun getWardPopulationSummary(): List<WardPopulationSummaryResponse>
}
