package np.likhupikemun.dpms.auth.repository

import np.likhupikemun.dpms.auth.domain.entity.Permission
import np.likhupikemun.dpms.auth.domain.enum.PermissionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PermissionRepository : 
    JpaRepository<Permission, PermissionType>, 
    JpaSpecificationExecutor<Permission>,
    PermissionRepositoryCustom {
    
    fun existsByType(type: PermissionType): Boolean
}
