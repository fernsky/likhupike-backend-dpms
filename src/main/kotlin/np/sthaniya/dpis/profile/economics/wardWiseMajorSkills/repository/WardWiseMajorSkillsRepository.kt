package np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.repository

import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.SkillType
import np.sthaniya.dpis.profile.economics.wardWiseMajorSkills.model.WardWiseMajorSkills
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseMajorSkills] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseMajorSkillsRepository :
    JpaRepository<WardWiseMajorSkills, UUID>,
    JpaSpecificationExecutor<WardWiseMajorSkills> {

    /**
     * Find all major skills data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseMajorSkills entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseMajorSkills>

    /**
     * Find all ward data for a specific skill type.
     *
     * @param skill The skill type to find data for
     * @return List of WardWiseMajorSkills entries for the specified skill type
     */
    fun findBySkill(skill: SkillType): List<WardWiseMajorSkills>

    /**
     * Find skills data for a specific ward and skill type.
     *
     * @param wardNumber The ward number to find data for
     * @param skill The skill type to find data for
     * @return WardWiseMajorSkills entry for the specified ward and skill type
     */
    fun findByWardNumberAndSkill(
        wardNumber: Int,
        skill: SkillType
    ): WardWiseMajorSkills?

    /**
     * Check if a record exists for the given ward and skill type.
     *
     * @param wardNumber The ward number to check
     * @param skill The skill type to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndSkill(wardNumber: Int, skill: SkillType): Boolean

    /**
     * Get the total population for each skill type across all wards.
     *
     * @return List of SkillPopulationSummary containing skill type and total population
     */
    @Query(
        "SELECT r.skill as skill, SUM(r.population) as totalPopulation " +
            "FROM WardWiseMajorSkills r GROUP BY r.skill ORDER BY r.skill"
    )
    fun getSkillPopulationSummary(): List<SkillPopulationSummary>

    /**
     * Get the total population with skills for each ward across all skill types.
     *
     * @return List of WardSkillsSummary containing ward number and total population
     */
    @Query(
        "SELECT r.wardNumber as wardNumber, SUM(r.population) as totalPopulation " +
            "FROM WardWiseMajorSkills r GROUP BY r.wardNumber ORDER BY r.wardNumber"
    )
    fun getWardSkillsSummary(): List<WardSkillsSummary>
}

/** Summary of population by skill type. */
interface SkillPopulationSummary {
    val skill: SkillType
    val totalPopulation: Long
}

/** Summary of population with skills by ward. */
interface WardSkillsSummary {
    val wardNumber: Int
    val totalPopulation: Long
}
