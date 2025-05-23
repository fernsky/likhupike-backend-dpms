package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.model.WardWiseDemographicSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [WardWiseDemographicSummary] data.
 *
 * This repository extends JpaRepository for standard CRUD operations, and JpaSpecificationExecutor
 * for dynamic query capabilities.
 */
@Repository
interface WardWiseDemographicSummaryRepository :
        JpaRepository<WardWiseDemographicSummary, UUID>,
        JpaSpecificationExecutor<WardWiseDemographicSummary> {

    /**
     * Find demographic summary data for a specific ward.
     *
     * @param wardNumber The ward number to find data for
     * @return WardWiseDemographicSummary entry for the specified ward
     */
    fun findByWardNumber(wardNumber: Int): WardWiseDemographicSummary?

    /**
     * Check if a record exists for the given ward number.
     *
     * @param wardNumber The ward number to check
     * @return true if a record exists, false otherwise
     */
    fun existsByWardNumber(wardNumber: Int): Boolean
}
