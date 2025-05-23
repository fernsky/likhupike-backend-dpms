package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.WardWiseMaritalStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseMaritalStatus] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardAgeWiseMaritalStatusRepository :
    JpaRepository<WardWiseMaritalStatus, UUID>,
    JpaSpecificationExecutor<WardWiseMaritalStatus> {

    /**
     * Find all marital status data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseMaritalStatus entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseMaritalStatus>

    /**
     * Find all marital status data for a specific age group.
     *
     * @param ageGroup The age group to find data for
     * @return List of WardWiseMaritalStatus entries for the specified age group
     */
    fun findByAgeGroup(ageGroup: MaritalAgeGroup): List<WardWiseMaritalStatus>

    /**
     * Find all marital status data for a specific marital status.
     *
     * @param maritalStatus The marital status to find data for
     * @return List of WardWiseMaritalStatus entries for the specified marital status
     */
    fun findByMaritalStatus(maritalStatus: MaritalStatusGroup): List<WardWiseMaritalStatus>

    /**
     * Find marital status data for a specific ward, age group, and marital status.
     *
     * @param wardNumber The ward number to find data for
     * @param ageGroup The age group to find data for
     * @param maritalStatus The marital status to find data for
     * @return WardWiseMaritalStatus entry for the specified ward, age group, and marital status
     */
    fun findByWardNumberAndAgeGroupAndMaritalStatus(
        wardNumber: Int,
        ageGroup: MaritalAgeGroup,
        maritalStatus: MaritalStatusGroup
    ): WardWiseMaritalStatus?

    /**
     * Check if a record exists for the given ward, age group, and marital status.
     *
     * @param wardNumber The ward number to check
     * @param ageGroup The age group to check
     * @param maritalStatus The marital status to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndAgeGroupAndMaritalStatus(
        wardNumber: Int,
        ageGroup: MaritalAgeGroup,
        maritalStatus: MaritalStatusGroup
    ): Boolean

    /**
     * Get the total population for each marital status across all wards and age groups.
     *
     * @return List of MaritalStatusSummary containing marital status and population statistics
     */
    @Query(
        "SELECT m.maritalStatus as maritalStatus, " +
            "SUM(m.population) as totalPopulation, " +
            "SUM(m.malePopulation) as malePopulation, " +
            "SUM(m.femalePopulation) as femalePopulation, " +
            "SUM(m.otherPopulation) as otherPopulation " +
            "FROM WardWiseMaritalStatus m GROUP BY m.maritalStatus ORDER BY m.maritalStatus"
    )
    fun getMaritalStatusSummary(): List<MaritalStatusSummary>

    /**
     * Get the total population for each age group across all wards and marital statuses.
     *
     * @return List of AgeGroupSummary containing age group and population statistics
     */
    @Query(
        "SELECT m.ageGroup as ageGroup, " +
            "SUM(m.population) as totalPopulation, " +
            "SUM(m.malePopulation) as malePopulation, " +
            "SUM(m.femalePopulation) as femalePopulation, " +
            "SUM(m.otherPopulation) as otherPopulation " +
            "FROM WardWiseMaritalStatus m GROUP BY m.ageGroup ORDER BY m.ageGroup"
    )
    fun getAgeGroupSummary(): List<AgeGroupSummary>
}

/** Summary of population by marital status. */
interface MaritalStatusSummary {
    val maritalStatus: MaritalStatusGroup
    val totalPopulation: Long
    val malePopulation: Long?
    val femalePopulation: Long?
    val otherPopulation: Long?
}

/** Summary of population by age group. */
interface AgeGroupSummary {
    val ageGroup: MaritalAgeGroup
    val totalPopulation: Long
    val malePopulation: Long?
    val femalePopulation: Long?
    val otherPopulation: Long?
}
