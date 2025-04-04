package np.sthaniya.dpis.auth.service

import np.sthaniya.dpis.auth.domain.entity.Role
import np.sthaniya.dpis.auth.domain.enums.RoleType

/**
 * Service interface for managing system roles.
 *
 * This interface provides methods for retrieving and validating roles
 * based on their types. It ensures that all required roles exist in the system
 * before they are assigned to users.
 */
interface RoleService {
    /**
     * Finds a role by its type.
     *
     * @param type The [RoleType] to search for
     * @return The [Role] entity matching the given type
     * @throws RoleNotFoundException if the role does not exist
     */
    fun findByType(type: RoleType): Role

    /**
     * Finds multiple roles by their types.
     *
     * @param types Set of [RoleType]s to search for
     * @return Set of [Role] entities matching the given types
     * @throws RoleNotFoundException if any of the roles do not exist
     */
    fun findByTypes(types: Set<RoleType>): Set<Role>

    /**
     * Ensures that all specified role types exist in the system.
     *
     * This method is typically used during system initialization or before
     * assigning roles to users.
     *
     * @param types Set of [RoleType]s to verify
     * @throws RoleNotFoundException if any of the roles do not exist
     */
    fun ensureAllRolesExist(types: Set<RoleType>)

    /**
     * Retrieves all roles from the system.
     *
     * @return Set of all [Role] entities in the system
     */
    fun findAll(): Set<Role>
}
