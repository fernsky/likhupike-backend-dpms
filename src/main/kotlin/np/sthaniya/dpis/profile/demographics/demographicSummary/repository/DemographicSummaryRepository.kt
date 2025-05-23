package np.sthaniya.dpis.profile.demographics.demographicSummary.repository

import java.util.UUID
import np.sthaniya.dpis.profile.demographics.demographicSummary.model.DemographicSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository interface for accessing and manipulating [DemographicSummary] data.
 *
 * This repository extends JpaRepository for standard CRUD operations.
 * Since DemographicSummary is a singleton entity, there should only be one record in the database.
 */
@Repository
interface DemographicSummaryRepository : JpaRepository<DemographicSummary, UUID> {

    /**
     * Gets the first demographic summary record.
     *
     * Since this is a singleton entity, we expect at most one record.
     *
     * @return The demographic summary or null if none exists
     */
    fun findFirstBy(): DemographicSummary?
}
