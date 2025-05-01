package np.sthaniya.dpis.profile.institutions.cooperatives.repository

import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeProjection
import np.sthaniya.dpis.profile.institutions.cooperatives.model.Cooperative
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Custom repository interface for [Cooperative] entity that provides additional query methods
 * beyond the standard Spring Data JPA operations.
 */
interface CooperativeRepositoryCustom {

    /**
     * Finds cooperatives within a specified distance of a geographic point.
     *
     * @param longitude The longitude of the reference point
     * @param latitude The latitude of the reference point
     * @param distanceInMeters The maximum distance in meters
     * @param pageable Pagination information
     * @return A page of cooperatives within the specified distance
     */
    fun findWithinDistance(
            longitude: Double,
            latitude: Double,
            distanceInMeters: Double,
            pageable: Pageable
    ): Page<Cooperative>

    /**
     * Retrieves a paginated list of cooperatives as projections with selected fields.
     *
     * @param spec The specification to filter cooperatives
     * @param pageable The pagination information
     * @param columns Set of column names to include in the projection
     * @param includeTranslations Whether to include translation data
     * @param includePrimaryMedia Whether to include primary media items
     * @return A [Page] of [CooperativeProjection] containing the filtered and projected
     * cooperatives
     */
    fun findAllWithProjection(
            spec: org.springframework.data.jpa.domain.Specification<Cooperative>,
            pageable: Pageable,
            columns: Set<String>,
            includeTranslations: Boolean = false,
            includePrimaryMedia: Boolean = false
    ): Page<CooperativeProjection>
}
