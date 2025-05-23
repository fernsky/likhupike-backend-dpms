package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.WardAgeGenderWiseAbsentee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardAgeGenderWiseAbsentee] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardAgeGenderWiseAbsenteeRepository :
        JpaRepository<WardAgeGenderWiseAbsentee, UUID>,
        JpaSpecificationExecutor<WardAgeGenderWiseAbsentee> {

    /**
     * Find all absentee population data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardAgeGenderWiseAbsentee entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardAgeGenderWiseAbsentee>

    /**
     * Find all absentee population data for a specific age group.
     *
     * @param ageGroup The age group to find data for
     * @return List of WardAgeGenderWiseAbsentee entries for the specified age group
     */
    fun findByAgeGroup(ageGroup: AbsenteeAgeGroup): List<WardAgeGenderWiseAbsentee>

    /**
     * Find all absentee population data for a specific gender.
     *
     * @param gender The gender to find data for
     * @return List of WardAgeGenderWiseAbsentee entries for the specified gender
     */
    fun findByGender(gender: Gender): List<WardAgeGenderWiseAbsentee>

    /**
     * Find absentee population data for a specific ward, age group, and gender.
     *
     * @param wardNumber The ward number to find data for
     * @param ageGroup The age group to find data for
     * @param gender The gender to find data for
     * @return WardAgeGenderWiseAbsentee entry for the specified ward, age group, and gender
     */
    fun findByWardNumberAndAgeGroupAndGender(
            wardNumber: Int,
            ageGroup: AbsenteeAgeGroup,
            gender: Gender
    ): WardAgeGenderWiseAbsentee?

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
            ageGroup: AbsenteeAgeGroup,
            gender: Gender
    ): Boolean

    /**
     * Get the total population for each age group and gender combination across all wards.
     *
     * @return List of AgeGroupGenderPopulationSummary containing age group, gender, and total population
     */
    @Query(
            "SELECT a.ageGroup as ageGroup, a.gender as gender, SUM(a.population) as totalPopulation " +
                    "FROM WardAgeGenderWiseAbsentee a " +
                    "GROUP BY a.ageGroup, a.gender " +
                    "ORDER BY a.ageGroup, a.gender"
    )
    fun getAgeGroupGenderPopulationSummary(): List<AgeGroupGenderPopulationSummary>

    /**
     * Get the total population for each ward across all age groups and genders.
     *
     * @return List of WardAbsenteePopulationSummary containing ward number and total population
     */
    @Query(
            "SELECT a.wardNumber as wardNumber, SUM(a.population) as totalPopulation " +
                    "FROM WardAgeGenderWiseAbsentee a " +
                    "GROUP BY a.wardNumber " +
                    "ORDER BY a.wardNumber"
    )
    fun getWardAbsenteePopulationSummary(): List<WardAbsenteePopulationSummary>
}

/** Summary of population by age group and gender. */
interface AgeGroupGenderPopulationSummary {
    val ageGroup: AbsenteeAgeGroup
    val gender: Gender
    val totalPopulation: Long
}

/** Summary of population by ward. */
interface WardAbsenteePopulationSummary {
    val wardNumber: Int
    val totalPopulation: Long
}
