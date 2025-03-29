package np.sthaniya.dpis.auth.service

import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.enums.PermissionType
/**
 * Service interface for managing system permissions.
 *
 * This interface provides methods for retrieving and validating permissions
 * based on their types. It ensures that all required permissions exist in the system
 * before they are assigned to users.
 */
interface PermissionService {
    /**
     * Finds a permission by its type.
     *
     * @param type The [PermissionType] to search for
     * @return The [Permission] entity matching the given type
     * @throws PermissionNotFoundException if the permission does not exist
     */
    fun findByType(type: PermissionType): Permission

    /**
     * Finds multiple permissions by their types.
     *
     * @param types Set of [PermissionType]s to search for
     * @return Set of [Permission] entities matching the given types
     * @throws PermissionNotFoundException if any of the permissions do not exist
     */
    fun findByTypes(types: Set<PermissionType>): Set<Permission>

    /**
     * Ensures that all specified permission types exist in the system.
     *
     * This method is typically used during system initialization or before
     * assigning permissions to users.
     *
     * @param types Set of [PermissionType]s to verify
     * @throws PermissionNotFoundException if any of the permissions do not exist
     */
    fun ensureAllPermissionsExist(types: Set<PermissionType>)
}
