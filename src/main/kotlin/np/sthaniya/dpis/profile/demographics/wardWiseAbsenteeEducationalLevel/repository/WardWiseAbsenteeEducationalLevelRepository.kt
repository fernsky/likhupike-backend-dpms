package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.model.WardWiseAbsenteeEducationalLevel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseAbsenteeEducationalLevel] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseAbsenteeEducationalLevelRepository :
        JpaRepository<WardWiseAbsenteeEducationalLevel, UUID>,
        JpaSpecificationExecutor<WardWiseAbsenteeEducationalLevel> {

    /**
     * Find all educational level data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseAbsenteeEducationalLevel entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseAbsenteeEducationalLevel>

    /**
     * Find all educational level data for a specific educational level.
     *
     * @param educationalLevel The educational level to find data for
     * @return List of WardWiseAbsenteeEducationalLevel entries for the specified educational level
     */
    fun findByEducationalLevel(
            educationalLevel: EducationalLevelType
    ): List<WardWiseAbsenteeEducationalLevel>

    /**
     * Find educational level data for a specific ward and educational level.
     *
     * @param wardNumber The ward number to find data for
     * @param educationalLevel The educational level to find data for
     * @return WardWiseAbsenteeEducationalLevel entry for the specified ward and educational level
     */
    fun findByWardNumberAndEducationalLevel(
            wardNumber: Int,
            educationalLevel: EducationalLevelType
    ): WardWiseAbsenteeEducationalLevel?

    /**
     * Check if a record exists for the given ward and educational level.
     *
     * @param wardNumber The ward number to check
     * @param educationalLevel The educational level to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndEducationalLevel(wardNumber: Int, educationalLevel: EducationalLevelType): Boolean

    /**
     * Get the total population for each educational level across all wards.
     *
     * @return List of EducationalLevelPopulationSummary containing educational level and total population
     */
    @Query(
            "SELECT e.educationalLevel as educationalLevel, SUM(e.population) as totalPopulation " +
                    "FROM WardWiseAbsenteeEducationalLevel e GROUP BY e.educationalLevel ORDER BY e.educationalLevel"
    )
    fun getEducationalLevelPopulationSummary(): List<EducationalLevelPopulationSummary>

    /**
     * Get the total population for each ward across all educational levels.
     *
     * @return List of WardPopulationSummary containing ward number and total population
     */
    @Query(
            "SELECT e.wardNumber as wardNumber, SUM(e.population) as totalPopulation " +
                    "FROM WardWiseAbsenteeEducationalLevel e GROUP BY e.wardNumber ORDER BY e.wardNumber"
    )
    fun getWardPopulationSummary(): List<WardPopulationSummary>
}

/** Summary of population by educational level. */
interface EducationalLevelPopulationSummary {
    val educationalLevel: EducationalLevelType
    val totalPopulation: Long
}

/** Summary of population by ward. */
interface WardPopulationSummary {
    val wardNumber: Int
    val totalPopulation: Long
}
