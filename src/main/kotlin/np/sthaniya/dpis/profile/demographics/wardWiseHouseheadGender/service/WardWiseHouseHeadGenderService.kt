package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.CreateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.UpdateWardWiseHouseHeadGenderDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.request.WardWiseHouseHeadGenderFilterDto
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.GenderPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardPopulationSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.dto.response.WardWiseHouseHeadGenderResponse

/** Service interface for managing ward-wise house head gender data. */
interface WardWiseHouseHeadGenderService {

    /**
     * Create new ward-wise house head gender data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseHouseHeadGender(
            createDto: CreateWardWiseHouseHeadGenderDto
    ): WardWiseHouseHeadGenderResponse

    /**
     * Update existing ward-wise house head gender data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseHouseHeadGender(
            id: UUID,
            updateDto: UpdateWardWiseHouseHeadGenderDto
    ): WardWiseHouseHeadGenderResponse

    /**
     * Delete ward-wise house head gender data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseHouseHeadGender(id: UUID)

    /**
     * Get ward-wise house head gender data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseHouseHeadGenderById(id: UUID): WardWiseHouseHeadGenderResponse

    /**
     * Get all ward-wise house head gender data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseHouseHeadGender(
            filter: WardWiseHouseHeadGenderFilterDto?
    ): List<WardWiseHouseHeadGenderResponse>

    /**
     * Get all ward-wise house head gender data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseHouseHeadGenderByWard(
            wardNumber: Int
    ): List<WardWiseHouseHeadGenderResponse>

    /**
     * Get all ward-wise house head gender data for a specific gender.
     *
     * @param gender The gender to filter by
     * @return List of data responses
     */
    fun getWardWiseHouseHeadGenderByGender(
            gender: Gender
    ): List<WardWiseHouseHeadGenderResponse>

    /**
     * Get summary of gender distribution across all wards.
     *
     * @return List of summary responses by gender
     */
    fun getGenderPopulationSummary(): List<GenderPopulationSummaryResponse>

    /**
     * Get summary of total population by ward across all genders.
     *
     * @return List of summary responses by ward
     */
    fun getWardPopulationSummary(): List<WardPopulationSummaryResponse>
}
