package np.sthaniya.dpis.auth.repository

/**
 * Primary repository interface for User entity operations in the Digital Profile System.
 *
 * This repository combines:
 * - Standard JPA operations through [JpaRepository]
 * - Specification-based querying through [JpaSpecificationExecutor]
 * - Custom repository operations through [UserRepositoryCustom]
 *
 * Key Features:
 * - CRUD operations for User entities
 * - Specification-based dynamic querying
 * - Custom optimized queries for user lookups
 * - Email-based user operations
 *
 * Integration Points:
 * - Works with User domain entity
 * - Supports Spring Data JPA patterns
 * - Integrates with custom repository implementations
 */
interface UserRepository : 
    JpaRepository<User, UUID>, 
    JpaSpecificationExecutor<User>, 
    UserRepositoryCustom {
    
    /**
     * Checks if a user exists with the given email address.
     *
     * @param email The email address to check
     * @return true if a user exists with this email, false otherwise
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return Optional containing the user if found, empty otherwise
     */
    fun findByEmail(email: String): Optional<User>
}
