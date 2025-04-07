package np.sthaniya.dpis.citizen.service.impl

import np.sthaniya.dpis.citizen.domain.entity.Citizen
import np.sthaniya.dpis.citizen.exception.CitizenAuthException
import np.sthaniya.dpis.citizen.repository.CitizenRepository
import np.sthaniya.dpis.citizen.service.CitizenService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * Implementation of [CitizenService] for citizen management operations.
 */
@Service
@Transactional
class CitizenServiceImpl(
    private val citizenRepository: CitizenRepository,
    private val passwordEncoder: PasswordEncoder
) : CitizenService {
    
    /**
     * Retrieves a citizen by their ID.
     *
     * @param id The citizen's unique identifier
     * @return The citizen if found
     * @throws CitizenAuthException.CitizenNotFoundException if no citizen exists with the given ID
     */
    override fun getCitizenById(id: UUID): Citizen {
        return citizenRepository.findById(id)
            .orElseThrow { CitizenAuthException.CitizenNotFoundException(id.toString()) }
    }

    /**
     * Finds a citizen by their email address.
     *
     * @param email The citizen's email address
     * @return The citizen if found, or null if not found
     */
    override fun findCitizenByEmail(email: String): Citizen? {
        return citizenRepository.findByEmail(email).orElse(null)
    }

    /**
     * Resets a citizen's password.
     *
     * @param citizenId The citizen's unique identifier
     * @param newPassword The new password to set
     * @return The updated citizen
     * @throws CitizenAuthException.CitizenNotFoundException if no citizen exists with the given ID
     */
    override fun resetPassword(citizenId: UUID, newPassword: String): Citizen {
        val citizen = getCitizenById(citizenId)
        citizen.setPassword(passwordEncoder.encode(newPassword))
        return citizenRepository.save(citizen)
    }
}
