package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.dto.UserProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

/**
 * Custom repository interface defining optimized and specialized User entity operations.
 *
 * This interface provides:
 * - Optimized queries for fetching users with permissions
 * - Projection-based queries for performance
 * - Custom counting operations
 *
 * Implementation Details:
 * - Uses Criteria API for dynamic query building
 * - Supports eager loading of permissions
 * - Handles custom projections for optimized data transfer
 *
 * Performance Considerations:
 * - Optimized for minimal database round trips
 * - Uses joins for efficient permission loading
 * - Supports pagination and specifications
 */
interface UserRepositoryCustom {
    /**
     * Finds a user by email with their permissions eagerly loaded.
     *
     * This query optimizes permission loading by:
     * - Using LEFT JOIN FETCH for permissions
     * - Loading all data in a single query
     * - Filtering out deleted users
     *
     * @param email The email address to search for
     * @return Optional containing the user with permissions if found
     */
    fun findByEmailWithPermissions(email: String): Optional<User>

    /**
     * Finds a user by ID with their permissions eagerly loaded.
     *
     * Similar to [findByEmailWithPermissions] but searches by ID instead.
     *
     * @param id The UUID of the user to find
     * @return Optional containing the user with permissions if found
     */
    fun findByIdWithPermissions(id: UUID): Optional<User>

    /**
     * Finds users matching given specifications with custom column projections.
     *
     * Features:
     * - Supports dynamic specifications
     * - Allows column selection for optimization
     * - Handles pagination
     * - Properly handles distinct counts when needed
     *
     * @param spec The specification to filter users
     * @param pageable Pagination information
     * @param columns Set of column names to include in projection
     * @return Page of user projections matching criteria
     */
    fun findAllWithProjection(spec: Specification<User>, pageable: Pageable, columns: Set<String>): Page<UserProjection>

    /**
     * Counts distinct users matching the given specification.
     *
     * Useful for:
     * - Getting accurate counts with joined tables
     * - Avoiding duplicate counts in complex queries
     * - Performance optimization for count queries
     *
     * @param spec The specification to filter users
     * @return Count of distinct users matching the specification
     */
    fun countDistinct(spec: Specification<User>): Long
}
