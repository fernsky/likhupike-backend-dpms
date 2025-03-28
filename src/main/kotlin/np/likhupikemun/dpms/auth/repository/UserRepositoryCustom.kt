package np.likhupikemun.dpms.auth.repository

import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.dto.UserProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

interface UserRepositoryCustom {
    fun findByEmailWithPermissions(email: String): Optional<User>
    fun findByIdWithPermissions(id: UUID): Optional<User>
    fun findAllWithProjection(spec: Specification<User>, pageable: Pageable, columns: Set<String>): Page<UserProjection>
    fun countDistinct(spec: Specification<User>): Long
}
