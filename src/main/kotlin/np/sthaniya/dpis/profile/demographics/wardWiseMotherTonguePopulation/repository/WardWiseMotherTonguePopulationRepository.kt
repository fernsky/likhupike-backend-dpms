package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.WardWiseMotherTonguePopulation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseMotherTonguePopulation] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseMotherTonguePopulationRepository :
        JpaRepository<WardWiseMotherTonguePopulation, UUID>,
        JpaSpecificationExecutor<WardWiseMotherTonguePopulation> {

    /**
     * Find all mother tongue population data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseMotherTonguePopulation entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseMotherTonguePopulation>

    /**
     * Find all mother tongue population data for a specific language type.
     *
     * @param languageType The language type to find data for
     * @return List of WardWiseMotherTonguePopulation entries for the specified language type
     */
    fun findByLanguageType(
            languageType: LanguageType
    ): List<WardWiseMotherTonguePopulation>

    /**
     * Find mother tongue population data for a specific ward and language type.
     *
     * @param wardNumber The ward number to find data for
     * @param languageType The language type to find data for
     * @return WardWiseMotherTonguePopulation entry for the specified ward and language type
     */
    fun findByWardNumberAndLanguageType(
            wardNumber: Int,
            languageType: LanguageType
    ): WardWiseMotherTonguePopulation?

    /**
     * Check if a record exists for the given ward and language type.
     *
     * @param wardNumber The ward number to check
     * @param languageType The language type to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndLanguageType(wardNumber: Int, languageType: LanguageType): Boolean

    /**
     * Get the total population for each language type across all wards.
     *
     * @return List of LanguagePopulationSummary containing language type and total population
     */
    @Query(
            "SELECT r.languageType as languageType, SUM(r.population) as totalPopulation " +
                    "FROM WardWiseMotherTonguePopulation r GROUP BY r.languageType ORDER BY r.languageType"
    )
    fun getLanguagePopulationSummary(): List<LanguagePopulationSummary>

    /**
     * Get the total population for each ward across all languages.
     *
     * @return List of WardPopulationSummary containing ward number and total population
     */
    @Query(
            "SELECT r.wardNumber as wardNumber, SUM(r.population) as totalPopulation " +
                    "FROM WardWiseMotherTonguePopulation r GROUP BY r.wardNumber ORDER BY r.wardNumber"
    )
    fun getWardPopulationSummary(): List<WardPopulationSummary>
}

/** Summary of population by language type. */
interface LanguagePopulationSummary {
    val languageType: LanguageType
    val totalPopulation: Long
}

/** Summary of population by ward. */
interface WardPopulationSummary {
    val wardNumber: Int
    val totalPopulation: Long
}
