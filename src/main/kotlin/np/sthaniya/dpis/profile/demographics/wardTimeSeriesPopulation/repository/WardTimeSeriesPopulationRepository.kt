package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.model.WardTimeSeriesPopulation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardTimeSeriesPopulation] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardTimeSeriesPopulationRepository :
        JpaRepository<WardTimeSeriesPopulation, UUID>,
        JpaSpecificationExecutor<WardTimeSeriesPopulation> {

    /**
     * Find all time series data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return List of WardTimeSeriesPopulation entries for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): List<WardTimeSeriesPopulation>

    /**
     * Find all time series data for a specific year.
     *
     * @param year The year to find data for
     * @return List of WardTimeSeriesPopulation entries for the specified year
     */
    fun findByYear(year: Int): List<WardTimeSeriesPopulation>

    /**
     * Find time series data for a specific ward and year.
     *
     * @param wardNumber The ward number to find data for
     * @param year The year to find data for
     * @return WardTimeSeriesPopulation entry for the specified ward and year
     */
    fun findByWardNumberAndYear(wardNumber: Int, year: Int): WardTimeSeriesPopulation?

    /**
     * Check if a record exists for the given ward and year.
     *
     * @param wardNumber The ward number to check
     * @param year The year to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumberAndYear(wardNumber: Int, year: Int): Boolean

    /**
     * Get the latest data for each ward (based on the most recent year).
     *
     * @return List of latest WardTimeSeriesPopulation entries for each ward
     */
    @Query(
            "SELECT w1 FROM WardTimeSeriesPopulation w1 " +
                    "WHERE w1.year = (SELECT MAX(w2.year) FROM WardTimeSeriesPopulation w2 " +
                    "WHERE w2.wardNumber = w1.wardNumber) " +
                    "ORDER BY w1.wardNumber"
    )
    fun findLatestForEachWard(): List<WardTimeSeriesPopulation>

    /**
     * Get the total population for each year across all wards.
     *
     * @return List of YearPopulationSummary containing year and total population
     */
    @Query(
            "SELECT w.year as year, SUM(w.totalPopulation) as totalPopulation " +
                    "FROM WardTimeSeriesPopulation w " +
                    "WHERE w.totalPopulation IS NOT NULL " +
                    "GROUP BY w.year ORDER BY w.year"
    )
    fun getYearPopulationSummary(): List<YearPopulationSummary>
}

/** Summary of population by year. */
interface YearPopulationSummary {
    val year: Int
    val totalPopulation: Long
}
