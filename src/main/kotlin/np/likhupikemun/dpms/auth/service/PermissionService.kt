package np.likhupikemun.dpms.auth.service

import np.likhupikemun.dpms.auth.domain.entity.Permission
import np.likhupikemun.dpms.auth.domain.enum.PermissionType

interface PermissionService {
    fun findByType(type: PermissionType): Permission
    fun findByTypes(types: Set<PermissionType>): Set<Permission>
    fun ensureAllPermissionsExist(types: Set<PermissionType>)
}
