package np.likhupikemun.dpis.auth.service

import np.likhupikemun.dpis.auth.domain.entity.Permission
import np.likhupikemun.dpis.auth.domain.enums.PermissionType

interface PermissionService {
    fun findByType(type: PermissionType): Permission
    fun findByTypes(types: Set<PermissionType>): Set<Permission>
    fun ensureAllPermissionsExist(types: Set<PermissionType>)
}
