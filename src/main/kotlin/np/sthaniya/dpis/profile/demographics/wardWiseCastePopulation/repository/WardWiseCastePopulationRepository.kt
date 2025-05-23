package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.WardWiseCastePopulation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseCastePopulation] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseCastePopulationRepository :
        JpaRepository<WardWiseCastePopulation, UUID>,
        JpaSpecificationExecutor<WardWiseCastePopulation> {

    /**
     * Find all caste population data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseCastePopulation entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseCastePopulation>

    /**
     * Find all caste population data for a specific caste type.
     *
     * @param casteType The caste type to find data for
     * @return List of WardWiseCastePopulation entries for the specified caste type
     */
    fun findByCasteType(
            casteType: CasteType
    ): List<WardWiseCastePopulation>

    /**
     * Find caste population data for a specific ward and caste type.
     *
     * @param wardNumber The ward number to find data for
     * @param casteType The caste type to find data for
     * @return WardWiseCastePopulation entry for the specified ward and caste type
     */
    fun findByWardNumberAndCasteType(
            wardNumber: Int,
            casteType: CasteType
    ): WardWiseCastePopulation?

    /**
     * Check if a record exists for the given ward and caste type.
     *
     * @param wardNumber The ward number to check
     * @param casteType The caste type to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndCasteType(wardNumber: Int, casteType: CasteType): Boolean

    /**
     * Get the total population for each caste type across all wards.
     *
     * @return List of CastePopulationSummary containing caste type and total population
     */
    @Query(
            "SELECT r.casteType as casteType, SUM(r.population) as totalPopulation " +
                    "FROM WardWiseCastePopulation r GROUP BY r.casteType ORDER BY r.casteType"
    )
    fun getCastePopulationSummary(): List<CastePopulationSummary>

    /**
     * Get the total population for each ward across all castes.
     *
     * @return List of WardPopulationSummary containing ward number and total population
     */
    @Query(
            "SELECT r.wardNumber as wardNumber, SUM(r.population) as totalPopulation " +
                    "FROM WardWiseCastePopulation r GROUP BY r.wardNumber ORDER BY r.wardNumber"
    )
    fun getWardPopulationSummary(): List<WardPopulationSummary>
}

/** Summary of population by caste type. */
interface CastePopulationSummary {
    val casteType: CasteType
    val totalPopulation: Long
}

/** Summary of population by ward. */
interface WardPopulationSummary {
    val wardNumber: Int
    val totalPopulation: Long
}
