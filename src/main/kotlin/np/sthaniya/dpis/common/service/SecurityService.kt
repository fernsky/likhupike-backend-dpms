package np.sthaniya.dpis.common.service

import np.sthaniya.dpis.auth.domain.entity.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityService {
    fun getCurrentUser(): User = SecurityContextHolder.getContext().authentication.principal as User
}
