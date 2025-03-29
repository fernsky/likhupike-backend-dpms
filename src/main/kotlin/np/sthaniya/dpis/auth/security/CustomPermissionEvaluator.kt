package np.sthaniya.dpis.auth.security

import np.sthaniya.dpis.auth.domain.enums.PermissionType
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import org.slf4j.LoggerFactory

@Component
class CustomPermissionEvaluator : PermissionEvaluator {
    private val log = LoggerFactory.getLogger(javaClass)

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

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        return hasPermission(authentication, null, permission)
    }
}
