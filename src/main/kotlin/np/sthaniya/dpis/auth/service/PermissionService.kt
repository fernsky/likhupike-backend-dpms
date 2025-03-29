package np.sthaniya.dpis.auth.service

import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.enums.PermissionType

interface PermissionService {
    fun findByType(type: PermissionType): Permission
    fun findByTypes(types: Set<PermissionType>): Set<Permission>
    fun ensureAllPermissionsExist(types: Set<PermissionType>)
}
