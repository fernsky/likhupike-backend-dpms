package np.sthaniya.dpis.citizen.repository

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/**
 * Repository for managing Citizen entities.
 * 
 * Provides standard JPA operations and extends with custom query methods
 * specific to citizen data.
 */
@Repository
interface CitizenRepository : 
    JpaRepository<Citizen, UUID>, 
    JpaSpecificationExecutor<Citizen>,
    CitizenRepositoryCustom {
    
    /**
     * Finds a citizen by email address.
     *
     * @param email The email address to search for
     * @return Optional containing the citizen if found
     */
    fun findByEmail(email: String): Optional<Citizen>
    
    /**
     * Finds a citizen by citizenship number.
     *
     * @param citizenshipNumber The citizenship number to search for
     * @return Optional containing the citizen if found
     */
    fun findByCitizenshipNumber(citizenshipNumber: String): Optional<Citizen>
    
    /**
     * Checks if a citizenship number already exists in the system.
     *
     * @param citizenshipNumber The citizenship number to check
     * @return true if the citizenship number exists, false otherwise
     */
    fun existsByCitizenshipNumber(citizenshipNumber: String): Boolean
}
