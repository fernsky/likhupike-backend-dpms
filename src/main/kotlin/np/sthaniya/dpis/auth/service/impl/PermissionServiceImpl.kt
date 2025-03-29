package np.sthaniya.dpis.auth.service.impl

import np.sthaniya.dpis.auth.service.PermissionService
import np.sthaniya.dpis.auth.repository.PermissionRepository
import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.exception.AuthException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Implementation of [PermissionService] that manages system permissions using a repository.
 *
 * This service provides methods for retrieving and validating permissions, ensuring
 * that all required permissions exist in the system before they are used.
 *
 * @property permissionRepository Repository for permission-related database operations
 */
@Service
@Transactional
class PermissionServiceImpl(
    private val permissionRepository: PermissionRepository
) : PermissionService {
    
    /**
     * Finds a permission by its type from the repository.
     *
     * @param type The [PermissionType] to search for
     * @return The [Permission] entity matching the given type
     * @throws AuthException.PermissionNotFoundException if the permission does not exist
     */
    @Transactional(readOnly = true)
    override fun findByType(type: PermissionType): Permission =
        permissionRepository.findById(type)
            .orElseThrow { AuthException.PermissionNotFoundException(type.name) }

    /**
     * Finds multiple permissions by their types from the repository.
     *
     * @param types Set of [PermissionType]s to search for
     * @return Set of [Permission] entities matching the given types
     */
    @Transactional(readOnly = true)
    override fun findByTypes(types: Set<PermissionType>): Set<Permission> =
        permissionRepository.findByTypes(types)

    /**
     * Verifies that all specified permission types exist in the system.
     *
     * @param types Set of [PermissionType]s to verify
     * @throws AuthException.MissingPermissionsException if any permissions are missing
     */
    override fun ensureAllPermissionsExist(types: Set<PermissionType>) {
        val existingPermissions = permissionRepository.findByTypes(types)
        val missingTypes = types - existingPermissions.map { it.type }.toSet()
        
        if (missingTypes.isNotEmpty()) {
            throw AuthException.MissingPermissionsException(missingTypes.map { it.name }.toSet())
        }
    }
}
