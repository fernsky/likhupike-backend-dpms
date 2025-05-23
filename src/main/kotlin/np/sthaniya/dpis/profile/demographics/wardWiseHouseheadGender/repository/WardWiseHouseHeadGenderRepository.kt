package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.model.WardWiseHouseHeadGender
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseHouseHeadGender] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseHouseHeadGenderRepository :
        JpaRepository<WardWiseHouseHeadGender, UUID>,
        JpaSpecificationExecutor<WardWiseHouseHeadGender> {

    /**
     * Find all house head gender data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseHouseHeadGender entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseHouseHeadGender>

    /**
     * Find all house head gender data for a specific gender.
     *
     * @param gender The gender to find data for
     * @return List of WardWiseHouseHeadGender entries for the specified gender
     */
    fun findByGender(gender: Gender): List<WardWiseHouseHeadGender>

    /**
     * Find house head gender data for a specific ward and gender.
     *
     * @param wardNumber The ward number to find data for
     * @param gender The gender to find data for
     * @return WardWiseHouseHeadGender entry for the specified ward and gender
     */
    fun findByWardNumberAndGender(
            wardNumber: Int,
            gender: Gender
    ): WardWiseHouseHeadGender?

    /**
     * Check if a record exists for the given ward and gender.
     *
     * @param wardNumber The ward number to check
     * @param gender The gender to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndGender(wardNumber: Int, gender: Gender): Boolean

    /**
     * Get the total population for each gender across all wards.
     *
     * @return List of GenderPopulationSummary containing gender and total population
     */
    @Query(
            "SELECT h.gender as gender, SUM(h.population) as totalPopulation " +
                    "FROM WardWiseHouseHeadGender h GROUP BY h.gender ORDER BY h.gender"
    )
    fun getGenderPopulationSummary(): List<GenderPopulationSummary>

    /**
     * Get the total population for each ward across all genders.
     *
     * @return List of WardPopulationSummary containing ward number, ward name, and total population
     */
    @Query(
            "SELECT h.wardNumber as wardNumber, h.wardName as wardName, SUM(h.population) as totalPopulation " +
                    "FROM WardWiseHouseHeadGender h GROUP BY h.wardNumber, h.wardName ORDER BY h.wardNumber"
    )
    fun getWardPopulationSummary(): List<WardPopulationSummary>
}

/** Summary of population by gender. */
interface GenderPopulationSummary {
    val gender: Gender
    val totalPopulation: Long
}

/** Summary of population by ward. */
interface WardPopulationSummary {
    val wardNumber: Int
    val wardName: String?
    val totalPopulation: Long
}
