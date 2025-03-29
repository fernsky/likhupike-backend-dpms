package np.likhupikemun.dpis.auth.repository

import np.likhupikemun.dpis.auth.domain.entity.Permission
import np.likhupikemun.dpis.auth.domain.enums.PermissionType

interface PermissionRepositoryCustom {
    fun findByTypes(types: Set<PermissionType>): Set<Permission>
}
