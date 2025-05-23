package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.CreateWardAgeGenderWiseAbsenteeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.UpdateWardAgeGenderWiseAbsenteeDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.request.WardAgeGenderWiseAbsenteeFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.AgeGroupGenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.WardAbsenteePopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.dto.response.WardAgeGenderWiseAbsenteeResponse
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup

/** Service interface for managing ward-age-gender-wise absentee data. */
interface WardAgeGenderWiseAbsenteeService {

    /**
     * Create new ward-age-gender-wise absentee data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardAgeGenderWiseAbsentee(
            createDto: CreateWardAgeGenderWiseAbsenteeDto
    ): WardAgeGenderWiseAbsenteeResponse

    /**
     * Update existing ward-age-gender-wise absentee data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardAgeGenderWiseAbsentee(
            id: UUID,
            updateDto: UpdateWardAgeGenderWiseAbsenteeDto
    ): WardAgeGenderWiseAbsenteeResponse

    /**
     * Delete ward-age-gender-wise absentee data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardAgeGenderWiseAbsentee(id: UUID)

    /**
     * Get ward-age-gender-wise absentee data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardAgeGenderWiseAbsenteeById(id: UUID): WardAgeGenderWiseAbsenteeResponse

    /**
     * Get all ward-age-gender-wise absentee data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardAgeGenderWiseAbsentee(
            filter: WardAgeGenderWiseAbsenteeFilterDto?
    ): List<WardAgeGenderWiseAbsenteeResponse>

    /**
     * Get all ward-age-gender-wise absentee data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardAgeGenderWiseAbsenteeByWard(
            wardNumber: Int
    ): List<WardAgeGenderWiseAbsenteeResponse>

    /**
     * Get all ward-age-gender-wise absentee data for a specific age group.
     *
     * @param ageGroup The age group to filter by
     * @return List of data responses
     */
    fun getWardAgeGenderWiseAbsenteeByAgeGroup(
            ageGroup: AbsenteeAgeGroup
    ): List<WardAgeGenderWiseAbsenteeResponse>

    /**
     * Get all ward-age-gender-wise absentee data for a specific gender.
     *
     * @param gender The gender to filter by
     * @return List of data responses
     */
    fun getWardAgeGenderWiseAbsenteeByGender(
            gender: Gender
    ): List<WardAgeGenderWiseAbsenteeResponse>

    /**
     * Get summary of absentee population across all wards grouped by age group and gender.
     *
     * @return List of summary responses by age group and gender
     */
    fun getAgeGroupGenderPopulationSummary(): List<AgeGroupGenderPopulationSummaryResponse>

    /**
     * Get summary of total absentee population by ward across all age groups and genders.
     *
     * @return List of summary responses by ward
     */
    fun getWardAbsenteePopulationSummary(): List<WardAbsenteePopulationSummaryResponse>
}
