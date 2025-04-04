package np.sthaniya.dpis.citizen.repository

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import java.util.Optional
import java.util.UUID

/**
 * Custom repository interface for specialized Citizen entity operations.
 */
interface CitizenRepositoryCustom {
    
    /**
     * Finds a citizen by ID and eagerly loads related document storage keys.
     *
     * @param id The UUID of the citizen to search for
     * @return An Optional containing the citizen with document information if found
     */
    fun findByIdWithDocuments(id: UUID): Optional<Citizen>
    
    /**
     * Searches for citizens by name with partial matching.
     *
     * @param namePart Part of the citizen name to search for
     * @return List of citizens matching the name pattern
     */
    fun searchByNameContaining(namePart: String): List<Citizen>
    
    /**
     * Finds citizens by address with partial matching for both
     * permanent and temporary addresses.
     *
     * @param addressPart Part of the address to search for
     * @return List of citizens matching the address pattern
     */
    fun findByAddressContaining(addressPart: String): List<Citizen>
}
