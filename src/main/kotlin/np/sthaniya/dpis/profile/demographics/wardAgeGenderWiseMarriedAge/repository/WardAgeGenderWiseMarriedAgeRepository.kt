package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.WardAgeGenderWiseMarriedAge
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardAgeGenderWiseMarriedAge] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardAgeGenderWiseMarriedAgeRepository :
        JpaRepository<WardAgeGenderWiseMarriedAge, UUID>,
        JpaSpecificationExecutor<WardAgeGenderWiseMarriedAge> {

    /**
     * Find all married age data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardAgeGenderWiseMarriedAge entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardAgeGenderWiseMarriedAge>

    /**
     * Find all married age data for a specific age group.
     *
     * @param ageGroup The age group to find data for
     * @return List of WardAgeGenderWiseMarriedAge entries for the specified age group
     */
    fun findByAgeGroup(ageGroup: MarriedAgeGroup): List<WardAgeGenderWiseMarriedAge>

    /**
     * Find all married age data for a specific gender.
     *
     * @param gender The gender to find data for
     * @return List of WardAgeGenderWiseMarriedAge entries for the specified gender
     */
    fun findByGender(gender: Gender): List<WardAgeGenderWiseMarriedAge>

    /**
     * Find married age data for a specific ward, age group, and gender.
     *
     * @param wardNumber The ward number to find data for
     * @param ageGroup The age group to find data for
     * @param gender The gender to find data for
     * @return WardAgeGenderWiseMarriedAge entry for the specified ward, age group, and gender
     */
    fun findByWardNumberAndAgeGroupAndGender(
            wardNumber: Int,
            ageGroup: MarriedAgeGroup,
            gender: Gender
    ): WardAgeGenderWiseMarriedAge?

    /**
     * Check if a record exists for the given ward, age group, and gender.
     *
     * @param wardNumber The ward number to check
     * @param ageGroup The age group to check
     * @param gender The gender to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndAgeGroupAndGender(
            wardNumber: Int,
            ageGroup: MarriedAgeGroup,
            gender: Gender
    ): Boolean

    /**
     * Get the total population for each age group and gender across all wards.
     *
     * @return List of AgeGroupGenderPopulationSummary containing age group, gender, and total population
     */
    @Query(
            "SELECT m.ageGroup as ageGroup, m.gender as gender, SUM(m.population) as totalPopulation " +
                    "FROM WardAgeGenderWiseMarriedAge m GROUP BY m.ageGroup, m.gender ORDER BY m.ageGroup, m.gender"
    )
    fun getAgeGroupGenderPopulationSummary(): List<AgeGroupGenderPopulationSummary>

    /**
     * Get the total population for each ward across all age groups and genders.
     *
     * @return List of WardPopulationSummary containing ward number and total population
     */
    @Query(
            "SELECT m.wardNumber as wardNumber, SUM(m.population) as totalPopulation " +
                    "FROM WardAgeGenderWiseMarriedAge m GROUP BY m.wardNumber ORDER BY m.wardNumber"
    )
    fun getWardPopulationSummary(): List<WardPopulationSummary>
}

/** Summary of population by age group and gender. */
interface AgeGroupGenderPopulationSummary {
    val ageGroup: MarriedAgeGroup
    val gender: Gender
    val totalPopulation: Long
}

/** Summary of population by ward. */
interface WardPopulationSummary {
    val wardNumber: Int
    val totalPopulation: Long
}
