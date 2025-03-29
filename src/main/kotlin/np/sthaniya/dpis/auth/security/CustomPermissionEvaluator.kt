package np.sthaniya.dpis.auth.security

import np.sthaniya.dpis.auth.domain.enums.PermissionType
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import org.slf4j.LoggerFactory

/**
 * Custom permission evaluator for Spring Security's @PreAuthorize annotations.
 *
 * Usage:
 * ```kotlin
 * @PreAuthorize("hasPermission(null, 'CREATE_USER')")
 * fun createUser() {
 *     // Only executes if user has CREATE_USER permission
 * }
 * ```
 *
 * Technical Details:
 * - Automatically prefixes permissions with "PERMISSION_"
 * - Case-sensitive permission matching
 * - Thread-safe implementation
 * - Supports debug logging
 */
@Component
class CustomPermissionEvaluator : PermissionEvaluator {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Evaluates if the authenticated user has the specified permission.
     * 
     * Evaluation Process:
     * 1. Prefixes permission with "PERMISSION_"
     * 2. Compares against user's granted authorities
     * 3. Returns true if matching permission found
     *
     * @param authentication Current authentication context
     * @param targetDomainObject Not used in this implementation
     * @param permission Permission to check (without "PERMISSION_" prefix)
     * @return true if user has the permission
     */
    override fun hasPermission(
        authentication: Authentication?,
        targetDomainObject: Any?,
        permission: Any?
    ): Boolean {
        if (authentication == null || permission == null) {
            log.debug("Authentication or permission is null")
            return false
        }

        val requiredPermission = "PERMISSION_$permission"
        log.debug("Checking permission {} against authorities: {}", requiredPermission, authentication.authorities)

        return authentication.authorities.any { authority ->
            log.debug("Comparing authority: {} with required: {}", authority.authority, requiredPermission)
            authority.authority == requiredPermission
        }
    }

    /**
     * Alternative permission check for target ID and type.
     * 
     * This implementation delegates to the primary hasPermission method
     * since we don't use target-specific permissions.
     */
    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        return hasPermission(authentication, null, permission)
    }
}
