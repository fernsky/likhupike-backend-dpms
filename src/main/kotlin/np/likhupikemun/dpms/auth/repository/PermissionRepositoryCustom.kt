package np.likhupikemun.dpms.auth.repository

import np.likhupikemun.dpms.auth.domain.entity.Permission
import np.likhupikemun.dpms.auth.domain.enums.PermissionType

interface PermissionRepositoryCustom {
    fun findByTypes(types: Set<PermissionType>): Set<Permission>
}
