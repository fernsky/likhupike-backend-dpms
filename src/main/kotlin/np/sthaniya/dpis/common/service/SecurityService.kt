package np.sthaniya.dpis.common.service

import np.sthaniya.dpis.auth.domain.entity.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SecurityService {
    /**
     * Gets the current authenticated user.
     * 
     * @return The current authenticated user
     * @throws ClassCastException if the principal is not a User
     */
    fun getCurrentUser(): User = SecurityContextHolder.getContext().authentication.principal as User
    
    /**
     * Gets the UUID of the current authenticated user.
     * 
     * @return The UUID of the current authenticated user
     * @throws IllegalStateException if the user is not authenticated or has no ID
     */
    fun getCurrentUserId(): UUID {
        return try {
            val user = getCurrentUser()
            user.id ?: throw IllegalStateException("Current user has no ID assigned")
        } catch (e: Exception) {
            throw IllegalStateException("Failed to get current user ID: ${e.message}")
        }
    }
}
