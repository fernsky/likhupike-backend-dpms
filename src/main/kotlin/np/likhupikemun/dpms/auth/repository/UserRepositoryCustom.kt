package np.likhupikemun.dpms.auth.repository

import np.likhupikemun.dpms.auth.domain.entity.User
import java.util.*

interface UserRepositoryCustom {
    fun findByEmailWithPermissions(email: String): Optional<User>
    fun findByIdWithPermissions(id: UUID): Optional<User>
}
