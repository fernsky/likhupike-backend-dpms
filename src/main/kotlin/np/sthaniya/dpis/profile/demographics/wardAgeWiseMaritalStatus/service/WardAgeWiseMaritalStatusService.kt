package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.service

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.CreateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.UpdateWardAgeWiseMaritalStatusDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.request.WardAgeWiseMaritalStatusFilterDto
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.AgeGroupSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.MaritalStatusSummaryResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.dto.response.WardAgeWiseMaritalStatusResponse
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup

/** Service interface for managing ward age-wise marital status data. */
interface WardAgeWiseMaritalStatusService {

    /**
     * Create new ward age-wise marital status data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardAgeWiseMaritalStatus(
        createDto: CreateWardAgeWiseMaritalStatusDto
    ): WardAgeWiseMaritalStatusResponse

    /**
     * Update existing ward age-wise marital status data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardAgeWiseMaritalStatus(
        id: UUID,
        updateDto: UpdateWardAgeWiseMaritalStatusDto
    ): WardAgeWiseMaritalStatusResponse

    /**
     * Delete ward age-wise marital status data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardAgeWiseMaritalStatus(id: UUID)

    /**
     * Get ward age-wise marital status data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardAgeWiseMaritalStatusById(id: UUID): WardAgeWiseMaritalStatusResponse

    /**
     * Get all ward age-wise marital status data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardAgeWiseMaritalStatus(
        filter: WardAgeWiseMaritalStatusFilterDto?
    ): List<WardAgeWiseMaritalStatusResponse>

    /**
     * Get all ward age-wise marital status data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardAgeWiseMaritalStatusByWard(
        wardNumber: Int
    ): List<WardAgeWiseMaritalStatusResponse>

    /**
     * Get all ward age-wise marital status data for a specific age group.
     *
     * @param ageGroup The age group to filter by
     * @return List of data responses
     */
    fun getWardAgeWiseMaritalStatusByAgeGroup(
        ageGroup: MaritalAgeGroup
    ): List<WardAgeWiseMaritalStatusResponse>

    /**
     * Get all ward age-wise marital status data for a specific marital status.
     *
     * @param maritalStatus The marital status to filter by
     * @return List of data responses
     */
    fun getWardAgeWiseMaritalStatusByMaritalStatus(
        maritalStatus: MaritalStatusGroup
    ): List<WardAgeWiseMaritalStatusResponse>

    /**
     * Get summary of marital status population across all wards and age groups.
     *
     * @return List of summary responses by marital status
     */
    fun getMaritalStatusSummary(): List<MaritalStatusSummaryResponse>

    /**
     * Get summary of age group population across all wards and marital statuses.
     *
     * @return List of summary responses by age group
     */
    fun getAgeGroupSummary(): List<AgeGroupSummaryResponse>
}
