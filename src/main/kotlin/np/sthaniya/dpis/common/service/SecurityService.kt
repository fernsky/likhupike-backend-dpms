package np.sthaniya.dpis.common.service

import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired

@Service
class SecurityService {
    @Autowired
    private lateinit var userRepository: UserRepository

    /**
     * Gets the username of the current authenticated user.
     * 
     * @return The username of the current authenticated user
     * @throws IllegalStateException if no authentication is found
     */
    fun getCurrentUsername(): String {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("No authentication found in context")
        
        // Handle different types of principals correctly
        val principal = authentication.principal
        return when (principal) {
            is UserDetails -> principal.username
            is String -> principal // When the principal is just a string (likely the username)
            else -> authentication.name // Fallback to authentication name
        }
    }

    /**
     * Gets the current authenticated user.
     * 
     * @return The current authenticated user
     * @throws IllegalStateException if the user cannot be found
     */
    fun getCurrentUser(): User {
        val username = getCurrentUsername()
        return userRepository.findByEmail(username)
            .orElseThrow { IllegalStateException("User not found for email: $username") }
    }

    /**
     * Gets the current authenticated user details.
     * This method is kept for backward compatibility but is now less strict.
     * 
     * @return The current authenticated UserDetails or null if not available
     * @throws IllegalStateException if no authentication is found
     */
    @Deprecated("Use getCurrentUsername() instead", ReplaceWith("getCurrentUsername()"))
    fun getCurrentUserDetails(): UserDetails? {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("No authentication found in context")
        
        val principal = authentication.principal
        return when (principal) {
            is UserDetails -> principal
            else -> null
        }
    }
}
