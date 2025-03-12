package np.likhupikemun.dpms.auth.service.impl

import np.likhupikemun.dpms.auth.service.PermissionService
import np.likhupikemun.dpms.auth.repository.PermissionRepository
import np.likhupikemun.dpms.auth.domain.entity.Permission
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import np.likhupikemun.dpms.auth.exception.AuthException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PermissionServiceImpl(
    private val permissionRepository: PermissionRepository
) : PermissionService {
    
    @Transactional(readOnly = true)
    override fun findByType(type: PermissionType): Permission =
        permissionRepository.findById(type)
            .orElseThrow { AuthException.PermissionNotFoundException(type.name) }

    @Transactional(readOnly = true)
    override fun findByTypes(types: Set<PermissionType>): Set<Permission> =
        permissionRepository.findByTypes(types)

    override fun ensureAllPermissionsExist(types: Set<PermissionType>) {
        val existingPermissions = permissionRepository.findByTypes(types)
        val missingTypes = types - existingPermissions.map { it.type }.toSet()
        
        if (missingTypes.isNotEmpty()) {
            throw AuthException.MissingPermissionsException(missingTypes.map { it.name }.toSet())
        }
    }
}
