package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.repository

import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.OccupationType
import np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.model.WardWiseMajorOccupation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseMajorOccupation] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseMajorOccupationRepository :
        JpaRepository<WardWiseMajorOccupation, UUID>,
        JpaSpecificationExecutor<WardWiseMajorOccupation> {

    /**
     * Find all major occupation data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseMajorOccupation entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseMajorOccupation>

    /**
     * Find all major occupation data for a specific occupation type.
     *
     * @param occupation The occupation type to find data for
     * @return List of WardWiseMajorOccupation entries for the specified occupation type
     */
    fun findByOccupation(
            occupation: OccupationType
    ): List<WardWiseMajorOccupation>

    /**
     * Find major occupation data for a specific ward and occupation type.
     *
     * @param wardNumber The ward number to find data for
     * @param occupation The occupation type to find data for
     * @return WardWiseMajorOccupation entry for the specified ward and occupation type
     */
    fun findByWardNumberAndOccupation(
            wardNumber: Int,
            occupation: OccupationType
    ): WardWiseMajorOccupation?

    /**
     * Check if a record exists for the given ward and occupation type.
     *
     * @param wardNumber The ward number to check
     * @param occupation The occupation type to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndOccupation(wardNumber: Int, occupation: OccupationType): Boolean

    /**
     * Get the total population for each occupation type across all wards.
     *
     * @return List of OccupationPopulationSummary containing occupation type and total population
     */
    @Query(
            "SELECT r.occupation as occupation, SUM(r.population) as totalPopulation " +
                    "FROM WardWiseMajorOccupation r GROUP BY r.occupation ORDER BY r.occupation"
    )
    fun getOccupationPopulationSummary(): List<OccupationPopulationSummary>

    /**
     * Get the total population for each ward across all occupations.
     *
     * @return List of WardOccupationSummary containing ward number and total population
     */
    @Query(
            "SELECT r.wardNumber as wardNumber, SUM(r.population) as totalPopulation " +
                    "FROM WardWiseMajorOccupation r GROUP BY r.wardNumber ORDER BY r.wardNumber"
    )
    fun getWardOccupationSummary(): List<WardOccupationSummary>
}

/** Summary of population by occupation type. */
interface OccupationPopulationSummary {
    val occupation: OccupationType
    val totalPopulation: Long
}

/** Summary of population by ward. */
interface WardOccupationSummary {
    val wardNumber: Int
    val totalPopulation: Long
}
