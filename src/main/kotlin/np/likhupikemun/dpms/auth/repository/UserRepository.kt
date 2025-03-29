package np.likhupikemun.dpis.auth.repository

import np.likhupikemun.dpis.auth.domain.entity.User
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
