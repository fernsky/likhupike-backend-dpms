package np.sthaniya.dpis.auth.service.impl

import np.sthaniya.dpis.auth.service.RoleService
import np.sthaniya.dpis.auth.repository.RoleRepository
import np.sthaniya.dpis.auth.domain.entity.Role
import np.sthaniya.dpis.auth.domain.enums.RoleType
import np.sthaniya.dpis.auth.exception.AuthException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Implementation of [RoleService] that manages system roles using a repository.
 *
 * This service provides methods for retrieving and validating roles, ensuring
 * that all required roles exist in the system before they are used.
 *
 * @property roleRepository Repository for role-related database operations
 */
@Service
@Transactional
class RoleServiceImpl(
    private val roleRepository: RoleRepository
) : RoleService {
    
    /**
     * Finds a role by its type from the repository.
     *
     * @param type The [RoleType] to search for
     * @return The [Role] entity matching the given type
     * @throws AuthException.RoleNotFoundException if the role does not exist
     */
    @Transactional(readOnly = true)
    override fun findByType(type: RoleType): Role =
        roleRepository.findById(type)
            .orElseThrow { AuthException.RoleNotFoundException(type.name) }

    /**
     * Finds multiple roles by their types from the repository.
     *
     * @param types Set of [RoleType]s to search for
     * @return Set of [Role] entities matching the given types
     */
    @Transactional(readOnly = true)
    override fun findByTypes(types: Set<RoleType>): Set<Role> =
        roleRepository.findByTypes(types)

    /**
     * Verifies that all specified role types exist in the system.
     *
     * @param types Set of [RoleType]s to verify
     * @throws AuthException.MissingRolesException if any roles are missing
     */
    override fun ensureAllRolesExist(types: Set<RoleType>) {
        val existingRoles = roleRepository.findByTypes(types)
        val missingTypes = types - existingRoles.map { it.type }.toSet()
        
        if (missingTypes.isNotEmpty()) {
            throw AuthException.MissingRolesException(missingTypes.map { it.name }.toSet())
        }
    }

    /**
     * Retrieves all roles from the system.
     *
     * @return Set of all [Role] entities in the system
     */
    @Transactional(readOnly = true)
    override fun findAll(): Set<Role> =
        roleRepository.findAll().toSet()
}
