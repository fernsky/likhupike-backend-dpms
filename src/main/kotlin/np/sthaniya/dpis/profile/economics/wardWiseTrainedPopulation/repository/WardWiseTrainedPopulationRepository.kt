package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.repository

import java.util.UUID
import np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.model.WardWiseTrainedPopulation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseTrainedPopulation] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseTrainedPopulationRepository :
        JpaRepository<WardWiseTrainedPopulation, UUID>,
        JpaSpecificationExecutor<WardWiseTrainedPopulation> {

    /**
     * Find trained population data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return WardWiseTrainedPopulation entry for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): WardWiseTrainedPopulation?

    /**
     * Check if a record exists for the given ward.
     *
     * @param wardNumber The ward number to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumber(wardNumber: Int): Boolean

    /**
     * Get the total trained population across all wards.
     *
     * @return Total trained population summary
     */
    @Query(
            "SELECT SUM(r.trainedPopulation) as totalTrainedPopulation FROM WardWiseTrainedPopulation r"
    )
    fun getTotalTrainedPopulation(): Long

    /**
     * Get the ward-wise trained population summary.
     *
     * @return List of ward summaries with trained population data
     */
    @Query(
            "SELECT r.wardNumber as wardNumber, r.trainedPopulation as trainedPopulation " +
                    "FROM WardWiseTrainedPopulation r ORDER BY r.wardNumber"
    )
    fun getWardWiseTrainedPopulationSummary(): List<WardTrainedPopulationSummary>
}

/** Summary of trained population by ward. */
interface WardTrainedPopulationSummary {
    val wardNumber: Int
    val trainedPopulation: Int
}
