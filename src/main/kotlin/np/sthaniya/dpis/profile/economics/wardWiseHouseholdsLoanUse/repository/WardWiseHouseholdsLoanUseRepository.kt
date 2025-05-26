package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.repository

import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.LoanUseType
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.model.WardWiseHouseholdsLoanUse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseHouseholdsLoanUse] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseHouseholdsLoanUseRepository :
        JpaRepository<WardWiseHouseholdsLoanUse, UUID>,
        JpaSpecificationExecutor<WardWiseHouseholdsLoanUse> {

    /**
     * Find all loan use data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseHouseholdsLoanUse entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseHouseholdsLoanUse>

    /**
     * Find all loan use data for a specific loan use type.
     *
     * @param loanUse The loan use type to find data for
     * @return List of WardWiseHouseholdsLoanUse entries for the specified loan use type
     */
    fun findByLoanUse(loanUse: LoanUseType): List<WardWiseHouseholdsLoanUse>

    /**
     * Find loan use data for a specific ward and loan use type.
     *
     * @param wardNumber The ward number to find data for
     * @param loanUse The loan use type to find data for
     * @return WardWiseHouseholdsLoanUse entry for the specified ward and loan use type
     */
    fun findByWardNumberAndLoanUse(
            wardNumber: Int,
            loanUse: LoanUseType
    ): WardWiseHouseholdsLoanUse?

    /**
     * Check if a record exists for the given ward and loan use type.
     *
     * @param wardNumber The ward number to check
     * @param loanUse The loan use type to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndLoanUse(wardNumber: Int, loanUse: LoanUseType): Boolean

    /**
     * Get the total households for each loan use type across all wards.
     *
     * @return List of LoanUseSummary containing loan use type and total households
     */
    @Query(
            "SELECT r.loanUse as loanUse, SUM(r.households) as totalHouseholds " +
                    "FROM WardWiseHouseholdsLoanUse r GROUP BY r.loanUse ORDER BY r.loanUse"
    )
    fun getLoanUseSummary(): List<LoanUseSummary>

    /**
     * Get the total households for each ward across all loan uses.
     *
     * @return List of WardHouseholdsSummary containing ward number and total households with loans
     */
    @Query(
            "SELECT r.wardNumber as wardNumber, SUM(r.households) as totalHouseholds " +
                    "FROM WardWiseHouseholdsLoanUse r GROUP BY r.wardNumber ORDER BY r.wardNumber"
    )
    fun getWardHouseholdsSummary(): List<WardHouseholdsSummary>
}

/** Summary of households by loan use type. */
interface LoanUseSummary {
    val loanUse: LoanUseType
    val totalHouseholds: Long
}

/** Summary of households by ward. */
interface WardHouseholdsSummary {
    val wardNumber: Int
    val totalHouseholds: Long
}
