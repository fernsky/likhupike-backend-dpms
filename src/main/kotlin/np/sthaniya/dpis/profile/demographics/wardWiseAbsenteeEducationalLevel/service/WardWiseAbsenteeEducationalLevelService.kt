package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.CreateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.UpdateWardWiseAbsenteeEducationalLevelDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.request.WardWiseAbsenteeEducationalLevelFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.EducationalLevelPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.dto.response.WardWiseAbsenteeEducationalLevelResponse
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType

/** Service interface for managing ward-wise absentee educational level data. */
interface WardWiseAbsenteeEducationalLevelService {

    /**
     * Create new ward-wise absentee educational level data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseAbsenteeEducationalLevel(
            createDto: CreateWardWiseAbsenteeEducationalLevelDto
    ): WardWiseAbsenteeEducationalLevelResponse

    /**
     * Update existing ward-wise absentee educational level data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseAbsenteeEducationalLevel(
            id: UUID,
            updateDto: UpdateWardWiseAbsenteeEducationalLevelDto
    ): WardWiseAbsenteeEducationalLevelResponse

    /**
     * Delete ward-wise absentee educational level data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseAbsenteeEducationalLevel(id: UUID)

    /**
     * Get ward-wise absentee educational level data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseAbsenteeEducationalLevelById(id: UUID): WardWiseAbsenteeEducationalLevelResponse

    /**
     * Get all ward-wise absentee educational level data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseAbsenteeEducationalLevel(
            filter: WardWiseAbsenteeEducationalLevelFilterDto?
    ): List<WardWiseAbsenteeEducationalLevelResponse>

    /**
     * Get all ward-wise absentee educational level data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseAbsenteeEducationalLevelByWard(
            wardNumber: Int
    ): List<WardWiseAbsenteeEducationalLevelResponse>

    /**
     * Get all ward-wise absentee educational level data for a specific educational level.
     *
     * @param educationalLevel The educational level to filter by
     * @return List of data responses
     */
    fun getWardWiseAbsenteeEducationalLevelByEducationalLevel(
            educationalLevel: EducationalLevelType
    ): List<WardWiseAbsenteeEducationalLevelResponse>

    /**
     * Get summary of educational level population across all wards.
     *
     * @return List of summary responses by educational level
     */
    fun getEducationalLevelPopulationSummary(): List<EducationalLevelPopulationSummaryResponse>

    /**
     * Get summary of total population by ward across all educational levels.
     *
     * @return List of summary responses by ward
     */
    fun getWardPopulationSummary(): List<WardPopulationSummaryResponse>
}
