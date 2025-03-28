package np.likhupikemun.dpms.auth.repository

import np.likhupikemun.dpms.auth.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface UserRepository : 
    JpaRepository<User, UUID>, 
    JpaSpecificationExecutor<User>, 
    UserRepositoryCustom {
    
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<User>
}
