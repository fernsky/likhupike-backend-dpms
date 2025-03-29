package np.likhupikemun.dpis.auth.repository

import np.likhupikemun.dpis.auth.domain.entity.Permission
import np.likhupikemun.dpis.auth.domain.enums.PermissionType
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
