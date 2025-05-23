package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.WardAgeWisePopulation
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardAgeWisePopulation] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardAgeWisePopulationRepository :
        JpaRepository<WardAgeWisePopulation, UUID>,
        JpaSpecificationExecutor<WardAgeWisePopulation> {

    /**
     * Find all age-wise population data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardAgeWisePopulation entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardAgeWisePopulation>

    /**
     * Find all age-wise population data for a specific age group.
     *
     * @param ageGroup The age group to find data for
     * @return List of WardAgeWisePopulation entries for the specified age group
     */
    fun findByAgeGroup(
            ageGroup: AgeGroup
    ): List<WardAgeWisePopulation>

    /**
     * Find all age-wise population data for a specific gender.
     *
     * @param gender The gender to find data for
     * @return List of WardAgeWisePopulation entries for the specified gender
     */
    fun findByGender(
            gender: Gender
    ): List<WardAgeWisePopulation>

    /**
     * Find age-wise population data for a specific ward, age group, and gender.
     *
     * @param wardNumber The ward number to find data for
     * @param ageGroup The age group to find data for
     * @param gender The gender to find data for
     * @return WardAgeWisePopulation entry for the specified ward, age group, and gender
     */
    fun findByWardNumberAndAgeGroupAndGender(
            wardNumber: Int,
            ageGroup: AgeGroup,
            gender: Gender
    ): WardAgeWisePopulation?

    /**
     * Check if a record exists for the given ward, age group, and gender.
     *
     * @param wardNumber The ward number to check
     * @param ageGroup The age group to check
     * @param gender The gender to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndAgeGroupAndGender(wardNumber: Int, ageGroup: AgeGroup, gender: Gender): Boolean

    /**
     * Get the total population for each age group across all wards.
     *
     * @return List of AgeGroupPopulationSummary containing age group and total population
     */
    @Query(
            "SELECT w.ageGroup as ageGroup, SUM(w.population) as totalPopulation " +
                    "FROM WardAgeWisePopulation w GROUP BY w.ageGroup ORDER BY w.ageGroup"
    )
    fun getAgeGroupPopulationSummary(): List<AgeGroupPopulationSummary>

    /**
     * Get the total population for each gender across all wards.
     *
     * @return List of GenderPopulationSummary containing gender and total population
     */
    @Query(
            "SELECT w.gender as gender, SUM(w.population) as totalPopulation " +
                    "FROM WardAgeWisePopulation w GROUP BY w.gender ORDER BY w.gender"
    )
    fun getGenderPopulationSummary(): List<GenderPopulationSummary>

    /**
     * Get the total population for each ward by age group and gender.
     *
     * @return List of WardAgeGenderSummary containing ward, age group, gender, and total population
     */
    @Query(
            "SELECT w.wardNumber as wardNumber, w.ageGroup as ageGroup, w.gender as gender, " +
                    "SUM(w.population) as totalPopulation " +
                    "FROM WardAgeWisePopulation w " +
                    "GROUP BY w.wardNumber, w.ageGroup, w.gender " +
                    "ORDER BY w.wardNumber, w.ageGroup, w.gender"
    )
    fun getWardAgeGenderSummary(): List<WardAgeGenderSummary>
}

/** Summary of population by age group. */
interface AgeGroupPopulationSummary {
    val ageGroup: AgeGroup
    val totalPopulation: Long
}

/** Summary of population by gender. */
interface GenderPopulationSummary {
    val gender: Gender
    val totalPopulation: Long
}

/** Summary of population by ward, age group, and gender. */
interface WardAgeGenderSummary {
    val wardNumber: Int
    val ageGroup: AgeGroup
    val gender: Gender
    val totalPopulation: Long
}
