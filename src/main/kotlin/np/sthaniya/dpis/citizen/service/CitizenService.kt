package np.sthaniya.dpis.citizen.service

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import java.util.UUID

/**
 * Service interface for citizen management operations.
 */
interface CitizenService {
    
    /**
     * Retrieves a citizen by their ID.
     *
     * @param id The citizen's unique identifier
     * @return The citizen if found
     * @throws CitizenAuthException.CitizenNotFoundException if no citizen exists with the given ID
     */
    fun getCitizenById(id: UUID): Citizen
    
    /**
     * Finds a citizen by their email address.
     *
     * @param email The citizen's email address
     * @return The citizen if found, or null if not found
     */
    fun findCitizenByEmail(email: String): Citizen?
    
    /**
     * Resets a citizen's password.
     *
     * @param citizenId The citizen's unique identifier
     * @param newPassword The new password to set
     * @return The updated citizen
     * @throws CitizenAuthException.CitizenNotFoundException if no citizen exists with the given ID
     */
    fun resetPassword(citizenId: UUID, newPassword: String): Citizen
}
