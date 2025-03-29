package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.enums.PermissionType

interface PermissionRepositoryCustom {
    fun findByTypes(types: Set<PermissionType>): Set<Permission>
}
