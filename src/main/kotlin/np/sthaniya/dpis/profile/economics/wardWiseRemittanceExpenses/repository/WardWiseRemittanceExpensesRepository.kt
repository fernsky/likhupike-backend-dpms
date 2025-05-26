package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.repository

import java.util.UUID
import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType
import np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.model.WardWiseRemittanceExpenses
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseRemittanceExpenses] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseRemittanceExpensesRepository :
        JpaRepository<WardWiseRemittanceExpenses, UUID>,
        JpaSpecificationExecutor<WardWiseRemittanceExpenses> {

    /**
     * Find all remittance expenses data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardWiseRemittanceExpenses entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardWiseRemittanceExpenses>

    /**
     * Find all remittance expenses data for a specific remittance expense type.
     *
     * @param remittanceExpense The remittance expense type to find data for
     * @return List of WardWiseRemittanceExpenses entries for the specified remittance expense type
     */
    fun findByRemittanceExpense(
            remittanceExpense: RemittanceExpenseType
    ): List<WardWiseRemittanceExpenses>

    /**
     * Find remittance expenses data for a specific ward and remittance expense type.
     *
     * @param wardNumber The ward number to find data for
     * @param remittanceExpense The remittance expense type to find data for
     * @return WardWiseRemittanceExpenses entry for the specified ward and remittance expense type
     */
    fun findByWardNumberAndRemittanceExpense(
            wardNumber: Int,
            remittanceExpense: RemittanceExpenseType
    ): WardWiseRemittanceExpenses?

    /**
     * Check if a record exists for the given ward and remittance expense type.
     *
     * @param wardNumber The ward number to check
     * @param remittanceExpense The remittance expense type to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndRemittanceExpense(wardNumber: Int, remittanceExpense: RemittanceExpenseType): Boolean

    /**
     * Get the total households for each remittance expense type across all wards.
     *
     * @return List of RemittanceExpenseSummary containing remittance expense type and total households
     */
    @Query(
            "SELECT r.remittanceExpense as remittanceExpense, SUM(r.households) as totalHouseholds " +
                    "FROM WardWiseRemittanceExpenses r GROUP BY r.remittanceExpense ORDER BY r.remittanceExpense"
    )
    fun getRemittanceExpenseSummary(): List<RemittanceExpenseSummary>

    /**
     * Get the total households for each ward across all remittance expense types.
     *
     * @return List of WardRemittanceSummary containing ward number and total households
     */
    @Query(
            "SELECT r.wardNumber as wardNumber, SUM(r.households) as totalHouseholds " +
                    "FROM WardWiseRemittanceExpenses r GROUP BY r.wardNumber ORDER BY r.wardNumber"
    )
    fun getWardRemittanceSummary(): List<WardRemittanceSummary>
}

/** Summary of households by remittance expense type. */
interface RemittanceExpenseSummary {
    val remittanceExpense: RemittanceExpenseType
    val totalHouseholds: Long
}

/** Summary of households by ward. */
interface WardRemittanceSummary {
    val wardNumber: Int
    val totalHouseholds: Long
}
