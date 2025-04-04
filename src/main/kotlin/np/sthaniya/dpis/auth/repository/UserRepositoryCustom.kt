package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.dto.UserProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

/**
 * Custom repository interface for [User] entity that provides additional query methods
 * beyond the standard Spring Data JPA operations.
 */
interface UserRepositoryCustom {
    /**
     * Finds a user by their email address and eagerly loads their permissions.
     *
     * @param email The email address to search for
     * @return An [Optional] containing the user with their permissions if found, or empty if not found
     */
    fun findByEmailWithPermissions(email: String): Optional<User>

    /**
     * Finds a user by their ID and eagerly loads their permissions.
     *
     * @param id The UUID of the user to search for
     * @return An [Optional] containing the user with their permissions if found, or empty if not found
     */
    fun findByIdWithPermissions(id: UUID): Optional<User>
    
    /**
     * Finds a user by their email address and eagerly loads their roles.
     *
     * @param email The email address to search for
     * @return An [Optional] containing the user with their roles if found, or empty if not found
     */
    fun findByEmailWithRoles(email: String): Optional<User>

    /**
     * Finds a user by their ID and eagerly loads their roles.
     *
     * @param id The UUID of the user to search for
     * @return An [Optional] containing the user with their roles if found, or empty if not found
     */
    fun findByIdWithRoles(id: UUID): Optional<User>
    
    /**
     * Finds a user by their email address and eagerly loads both their permissions and roles.
     *
     * @param email The email address to search for
     * @return An [Optional] containing the user with their permissions and roles if found, or empty if not found
     */
    fun findByEmailWithPermissionsAndRoles(email: String): Optional<User>

    /**
     * Finds a user by their ID and eagerly loads both their permissions and roles.
     *
     * @param id The UUID of the user to search for
     * @return An [Optional] containing the user with their permissions and roles if found, or empty if not found
     */
    fun findByIdWithPermissionsAndRoles(id: UUID): Optional<User>

    /**
     * Retrieves a paginated list of users as projections based on the given specification and selected columns.
     *
     * @param spec The specification to filter users
     * @param pageable The pagination information
     * @param columns Set of column names to include in the projection
     * @return A [Page] of [UserProjection] containing the filtered and projected users
     */
    fun findAllWithProjection(spec: Specification<User>, pageable: Pageable, columns: Set<String>): Page<UserProjection>

    /**
     * Counts the distinct number of users matching the given specification.
     *
     * @param spec The specification to filter users
     * @return The count of distinct users matching the specification
     */
    fun countDistinct(spec: Specification<User>): Long
}
