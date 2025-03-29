package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

/**
 * Primary repository interface for [User] entity operations.
 * 
 * This repository combines Spring Data JPA's standard repository features,
 * specification execution capabilities, and custom query methods defined in [UserRepositoryCustom].
 */
interface UserRepository : 
    JpaRepository<User, UUID>, 
    JpaSpecificationExecutor<User>, 
    UserRepositoryCustom {
    
    /**
     * Checks if a user exists with the given email address.
     *
     * @param email The email address to check
     * @return true if a user exists with the given email, false otherwise
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return An [Optional] containing the user if found, or empty if not found
     */
    fun findByEmail(email: String): Optional<User>
}
