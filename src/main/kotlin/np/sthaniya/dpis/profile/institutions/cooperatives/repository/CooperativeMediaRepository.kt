package np.sthaniya.dpis.profile.institutions.cooperatives.repository

import java.util.Optional
import java.util.UUID
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMedia
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeMediaType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.MediaVisibilityStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/** Repository interface for managing [CooperativeMedia] entities. */
@Repository
interface CooperativeMediaRepository : JpaRepository<CooperativeMedia, UUID> {

    /**
     * Find all media for a cooperative.
     *
     * @param cooperativeId The ID of the cooperative
     * @param pageable Pagination information
     * @return A page of media for the cooperative
     */
    fun findByCooperativeId(cooperativeId: UUID, pageable: Pageable): Page<CooperativeMedia>

    /**
     * Find all media for a cooperative with a specific type.
     *
     * @param cooperativeId The ID of the cooperative
     * @param type The media type
     * @param pageable Pagination information
     * @return A page of media matching the criteria
     */
    fun findByCooperativeIdAndType(
            cooperativeId: UUID,
            type: CooperativeMediaType,
            pageable: Pageable
    ): Page<CooperativeMedia>

    /**
     * Find all media for a cooperative with a specific locale.
     *
     * @param cooperativeId The ID of the cooperative
     * @param locale The locale code
     * @param pageable Pagination information
     * @return A page of media matching the criteria
     */
    fun findByCooperativeIdAndLocale(
            cooperativeId: UUID,
            locale: String,
            pageable: Pageable
    ): Page<CooperativeMedia>

    /**
     * Find all media with a specific visibility status.
     *
     * @param visibilityStatus The visibility status
     * @param pageable Pagination information
     * @return A page of media with the given visibility status
     */
    fun findByVisibilityStatus(
            visibilityStatus: MediaVisibilityStatus,
            pageable: Pageable
    ): Page<CooperativeMedia>

    /**
     * Find the primary media item for a cooperative of a specific type.
     *
     * @param cooperativeId The ID of the cooperative
     * @param type The media type
     * @return Optional containing the primary media item if found
     */
    fun findByCooperativeIdAndTypeAndIsPrimaryTrue(
            cooperativeId: UUID,
            type: CooperativeMediaType
    ): Optional<CooperativeMedia>

    /**
     * Find all featured media items.
     *
     * @param pageable Pagination information
     * @return A page of featured media items
     */
    fun findByIsFeaturedTrue(pageable: Pageable): Page<CooperativeMedia>

    /**
     * Find media by tags.
     *
     * @param tag The tag to search for
     * @param pageable Pagination information
     * @return A page of media with matching tags
     */
    @Query(
            """
        SELECT m FROM CooperativeMedia m 
        WHERE LOWER(m.tags) LIKE LOWER(CONCAT('%', :tag, '%'))
    """
    )
    fun findByTag(@Param("tag") tag: String, pageable: Pageable): Page<CooperativeMedia>

    /**
     * Set a media item as the primary one of its type for its cooperative, ensuring no other media
     * of the same type for that cooperative is primary.
     *
     * @param mediaId The ID of the media to set as primary
     * @return Number of rows affected
     */
    @Modifying
    @Query(
            """
        UPDATE CooperativeMedia m 
        SET m.isPrimary = CASE WHEN m.id = :mediaId THEN true ELSE false END 
        WHERE m.cooperative.id = (
            SELECT sub.cooperative.id FROM CooperativeMedia sub WHERE sub.id = :mediaId
        ) AND m.type = (
            SELECT sub.type FROM CooperativeMedia sub WHERE sub.id = :mediaId
        )
    """
    )
    fun setAsPrimary(@Param("mediaId") mediaId: UUID): Int

    /**
     * Increment the view count for a media item.
     *
     * @param mediaId The ID of the media
     * @return Number of rows affected
     */
    @Modifying
    @Query("UPDATE CooperativeMedia m SET m.viewCount = m.viewCount + 1 WHERE m.id = :mediaId")
    fun incrementViewCount(@Param("mediaId") mediaId: UUID): Int

    /**
     * Delete all media for a cooperative.
     *
     * @param cooperativeId The ID of the cooperative
     * @return Number of rows affected
     */
    fun deleteByCooperativeId(cooperativeId: UUID): Int
}
