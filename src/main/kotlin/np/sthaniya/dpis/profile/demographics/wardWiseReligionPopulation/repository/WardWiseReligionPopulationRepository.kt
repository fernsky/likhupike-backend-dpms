package np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.ReligionType
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.WardWiseReligionPopulation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseReligionPopulation] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseReligionPopulationRepository :
        JpaRepository<WardWiseReligionPopulation, UUID>,
        JpaSpecificationExecutor<WardWiseReligionPopulation> {

    /**
     * Find all religion population data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseReligionPopulation entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseReligionPopulation>

    /**
     * Find all religion population data for a specific religion type.
     *
     * @param religionType The religion type to find data for
     * @return List of WardWiseReligionPopulation entries for the specified religion type
     */
    fun findByReligionType(
            religionType: ReligionType
    ): List<WardWiseReligionPopulation>

    /**
     * Find religion population data for a specific ward and religion type.
     *
     * @param wardNumber The ward number to find data for
     * @param religionType The religion type to find data for
     * @return WardWiseReligionPopulation entry for the specified ward and religion type
     */
    fun findByWardNumberAndReligionType(
            wardNumber: Int,
            religionType: ReligionType
    ): WardWiseReligionPopulation?

    /**
     * Check if a record exists for the given ward and religion type.
     *
     * @param wardNumber The ward number to check
     * @param religionType The religion type to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndReligionType(wardNumber: Int, religionType: ReligionType): Boolean

    /**
     * Get the total population for each religion type across all wards.
     *
     * @return List of ReligionPopulationSummary containing religion type and total population
     */
    @Query(
            "SELECT r.religionType as religionType, SUM(r.population) as totalPopulation " +
                    "FROM WardWiseReligionPopulation r GROUP BY r.religionType ORDER BY r.religionType"
    )
    fun getReligionPopulationSummary(): List<ReligionPopulationSummary>

    /**
     * Get the total population for each ward across all religions.
     *
     * @return List of WardPopulationSummary containing ward number and total population
     */
    @Query(
            "SELECT r.wardNumber as wardNumber, SUM(r.population) as totalPopulation " +
                    "FROM WardWiseReligionPopulation r GROUP BY r.wardNumber ORDER BY r.wardNumber"
    )
    fun getWardPopulationSummary(): List<WardPopulationSummary>
}

/** Summary of population by religion type. */
interface ReligionPopulationSummary {
    val religionType: ReligionType
    val totalPopulation: Long
}

/** Summary of population by ward. */
interface WardPopulationSummary {
    val wardNumber: Int
    val totalPopulation: Long
}
