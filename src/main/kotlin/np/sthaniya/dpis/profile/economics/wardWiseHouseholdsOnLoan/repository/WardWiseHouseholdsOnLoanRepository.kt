package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.repository

import java.util.UUID
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.model.WardWiseHouseholdsOnLoan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseHouseholdsOnLoan] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseHouseholdsOnLoanRepository :
        JpaRepository<WardWiseHouseholdsOnLoan, UUID>,
        JpaSpecificationExecutor<WardWiseHouseholdsOnLoan> {

    /**
     * Find all households on loan data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseHouseholdsOnLoan entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): WardWiseHouseholdsOnLoan?

    /**
     * Check if a record exists for the given ward.
     *
     * @param wardNumber The ward number to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumber(wardNumber: Int): Boolean

    /**
     * Get the total number of households on loan across all wards.
     *
     * @return HouseholdsOnLoanSummary containing the total number of households on loan
     */
    @Query(
            "SELECT SUM(r.households) as totalHouseholdsOnLoan " +
                    "FROM WardWiseHouseholdsOnLoan r"
    )
    fun getHouseholdsOnLoanSummary(): HouseholdsOnLoanSummary
}

/** Summary of households on loan. */
interface HouseholdsOnLoanSummary {
    val totalHouseholdsOnLoan: Long
}
