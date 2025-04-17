package np.sthaniya.dpis.profile.institutions.cooperatives.repository

import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeTranslation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional
import java.util.UUID

/**
 * Repository interface for managing [CooperativeTranslation] entities.
 */
@Repository
interface CooperativeTranslationRepository : JpaRepository<CooperativeTranslation, UUID> {

    /**
     * Find a cooperative translation by cooperative ID and locale.
     *
     * @param cooperativeId The ID of the cooperative
     * @param locale The locale code
     * @return Optional containing the translation if found
     */
    fun findByCooperativeIdAndLocale(cooperativeId: UUID, locale: String): Optional<CooperativeTranslation>

    /**
     * Find all translations for a cooperative.
     *
     * @param cooperativeId The ID of the cooperative
     * @return List of translations for the cooperative
     */
    fun findByCooperativeId(cooperativeId: UUID): List<CooperativeTranslation>

    /**
     * Find all translations for a specific locale.
     *
     * @param locale The locale code
     * @param pageable Pagination information
     * @return A page of translations for the locale
     */
    fun findByLocale(locale: String, pageable: Pageable): Page<CooperativeTranslation>
    
    /**
     * Find translations by status.
     *
     * @param status The content status
     * @param pageable Pagination information
     * @return A page of translations with the given status
     */
    fun findByStatus(status: ContentStatus, pageable: Pageable): Page<CooperativeTranslation>

    /**
     * Search translations by content.
     *
     * @param query The search query
     * @param pageable Pagination information
     * @return A page of translations matching the search query
     */
    @Query("""
        SELECT t FROM CooperativeTranslation t 
        WHERE 
            LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(t.location) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(t.services) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    fun search(@Param("query") query: String, pageable: Pageable): Page<CooperativeTranslation>

    /**
     * Find translations by URL slug.
     *
     * @param slugUrl The URL slug
     * @return Optional containing the translation if found
     */
    fun findBySlugUrl(slugUrl: String): Optional<CooperativeTranslation>
    
    /**
     * Check if a translation with the given slug URL exists.
     *
     * @param slugUrl The slug URL to check
     * @return true if a translation with this slug exists
     */
    fun existsBySlugUrl(slugUrl: String): Boolean

    /**
     * Check if a translation with the given slug URL exists for a different cooperative.
     *
     * @param slugUrl The slug URL to check
     * @param cooperativeId The cooperative ID to exclude
     * @return true if a translation with this slug exists for a different cooperative
     */
    @Query("""
        SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END 
        FROM CooperativeTranslation t 
        WHERE t.slugUrl = :slugUrl AND t.cooperative.id <> :cooperativeId
    """)
    fun existsBySlugUrlAndCooperativeIdNot(
        @Param("slugUrl") slugUrl: String,
        @Param("cooperativeId") cooperativeId: UUID
    ): Boolean
    
    /**
     * Update the content status of a cooperative translation.
     *
     * @param id The translation ID
     * @param status The new status
     * @param updatedBy User who is updating the status
     * @return Number of rows affected
     */
    @Modifying
    @Query("""
        UPDATE CooperativeTranslation t 
        SET t.status = :status, t.updatedBy = :updatedBy, t.updatedAt = :updatedAt 
        WHERE t.id = :id
    """)
    fun updateStatus(
        @Param("id") id: UUID, 
        @Param("status") status: ContentStatus,
        @Param("updatedBy") updatedBy: UUID,
        @Param("updatedAt") updatedAt: Instant = Instant.now()
    ): Int

    /**
     * Find translations that need review (content that hasn't been reviewed in a while).
     *
     * @param reviewThreshold Threshold date for content review
     * @param pageable Pagination information
     * @return A page of translations needing review
     */
    fun findByContentLastReviewedBeforeOrContentLastReviewedIsNull(
        reviewThreshold: Instant, 
        pageable: Pageable
    ): Page<CooperativeTranslation>
}
