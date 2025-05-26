package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.service

import java.util.UUID
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.CreateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.UpdateWardWiseMajorSkillsDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.request.WardWiseMajorSkillsFilterDto
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.SkillPopulationSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardSkillsSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.dto.response.WardWiseMajorSkillsResponse
import np.sthaniya.dpis.profile.economics.common.model.SkillType

/**
 * Service interface for managing ward-wise major skills data.
 */
interface WardWiseMajorSkillsService {

    /**
     * Create new ward-wise major skills data.
     *
     * @param createDto DTO containing the data to create
     * @return The created data response
     */
    fun createWardWiseMajorSkills(
        createDto: CreateWardWiseMajorSkillsDto
    ): WardWiseMajorSkillsResponse

    /**
     * Update existing ward-wise major skills data.
     *
     * @param id The ID of the data to update
     * @param updateDto DTO containing the update data
     * @return The updated data response
     */
    fun updateWardWiseMajorSkills(
        id: UUID,
        updateDto: UpdateWardWiseMajorSkillsDto
    ): WardWiseMajorSkillsResponse

    /**
     * Delete ward-wise major skills data.
     *
     * @param id The ID of the data to delete
     */
    fun deleteWardWiseMajorSkills(id: UUID)

    /**
     * Get ward-wise major skills data by ID.
     *
     * @param id The ID of the data to retrieve
     * @return The data response
     */
    fun getWardWiseMajorSkillsById(id: UUID): WardWiseMajorSkillsResponse

    /**
     * Get all ward-wise major skills data with optional filtering.
     *
     * @param filter Optional filter criteria
     * @return List of data responses
     */
    fun getAllWardWiseMajorSkills(
        filter: WardWiseMajorSkillsFilterDto?
    ): List<WardWiseMajorSkillsResponse>

    /**
     * Get all ward-wise major skills data for a specific ward.
     *
     * @param wardNumber The ward number to filter by
     * @return List of data responses
     */
    fun getWardWiseMajorSkillsByWard(
        wardNumber: Int
    ): List<WardWiseMajorSkillsResponse>

    /**
     * Get all ward-wise major skills data for a specific skill type.
     *
     * @param skill The skill type to filter by
     * @return List of data responses
     */
    fun getWardWiseMajorSkillsBySkill(
        skill: SkillType
    ): List<WardWiseMajorSkillsResponse>

    /**
     * Get summary of skill population across all wards.
     *
     * @return List of summary responses by skill
     */
    fun getSkillPopulationSummary(): List<SkillPopulationSummaryResponse>

    /**
     * Get summary of total population with skills by ward.
     *
     * @return List of summary responses by ward
     */
    fun getWardSkillsSummary(): List<WardSkillsSummaryResponse>
}
