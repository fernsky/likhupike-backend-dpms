package np.sthaniya.dpis.citizen.service

import np.sthaniya.dpis.citizen.exception.CitizenAuthException
import np.sthaniya.dpis.citizen.repository.CitizenRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * Service for loading citizen details for Spring Security authentication.
 *
 * This implementation connects Spring Security's UserDetailsService interface
 * with our Citizen entities, enabling JWT-based authentication for citizens.
 *
 * @property citizenRepository Repository for accessing citizen data
 */
@Service
class CitizenUserDetailsService(
    private val citizenRepository: CitizenRepository
) : UserDetailsService {

    /**
     * Loads a citizen by their email address (username).
     *
     * @param email The citizen's email address
     * @return The citizen details for authentication
     * @throws CitizenAuthException.CitizenNotFoundException if no citizen is found with the given email
     */
    override fun loadUserByUsername(email: String): UserDetails {
        return citizenRepository.findByEmail(email).orElseThrow { 
            CitizenAuthException.CitizenNotFoundException(email) 
        }
    }
}
