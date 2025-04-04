package np.sthaniya.dpis.auth.dto

import jakarta.validation.constraints.NotEmpty
import np.sthaniya.dpis.auth.domain.enums.RoleType

/**
 * Data transfer object for managing user role modifications.
 * 
 * This class provides an immutable representation of role changes to be applied
 * to a user, along with utility methods for role set manipulation.
 * Implements validation through Jakarta Validation annotations.
 *
 * Example usage:
 * ```kotlin
 * val dto = UserRolesDto(mapOf(
 *     RoleType.LAND_RECORDS_OFFICER to true,    // Assign LAND_RECORDS_OFFICER
 *     RoleType.SYSTEM_ADMINISTRATOR to false    // Revoke SYSTEM_ADMINISTRATOR
 * ))
 * 
 * // Get roles to add
 * val toAssign = dto.getRolesToAssign()
 * 
 * // Get roles to remove
 * val toRevoke = dto.getRolesToRevoke()
 * 
 * // Update existing role set
 * val updatedRoles = dto.updateExistingRoles(currentRoles)
 * ```
 *
 * @param roles Map of [RoleType] to boolean indicating grant (true) or revoke (false)
 * @throws jakarta.validation.ConstraintViolationException if roles map is empty
 */
data class UserRolesDto(
    @field:NotEmpty(message = "At least one role must be specified")
    val roles: Map<RoleType, Boolean>
) {

    /**
     * Returns a set of roles that should be assigned to the user.
     * Filters the roles map for entries with value = true.
     */
    fun getRolesToAssign(): Set<RoleType> = 
        roles.filterValues { it }.keys

    /**
     * Returns a set of roles that should be revoked from the user.
     * Filters the roles map for entries with value = false.
     */
    fun getRolesToRevoke(): Set<RoleType> = 
        roles.filterValues { !it }.keys

    /**
     * Applies the role changes to an existing set of roles.
     * This operation is non-destructive to the input set.
     *
     * @param currentRoles The current set of roles
     * @return A new set containing the updated roles
     */
    fun updateExistingRoles(currentRoles: Set<RoleType>): Set<RoleType> {
        val updatedRoles = currentRoles.toMutableSet()
        getRolesToAssign().forEach { updatedRoles.add(it) }
        getRolesToRevoke().forEach { updatedRoles.remove(it) }
        return updatedRoles
    }

    /**
     * Returns the set of roles that need to be modified based on their current state.
     * A role needs modification if its desired state differs from its current state.
     *
     * @param existingRoles Current set of roles
     * @return Set of [RoleType] that need to be added or removed
     */
    fun getRolesToModify(existingRoles: Set<RoleType>): Set<RoleType> {
        val rolesToModify = mutableSetOf<RoleType>()
        
        roles.forEach { (role, shouldHave) ->
            val hasRole = role in existingRoles
            // Only add to modification set if there's a change needed
            if (shouldHave != hasRole) {
                rolesToModify.add(role)
            }
        }
        
        return rolesToModify
    }

    /**
     * Checks if a specific role should be assigned.
     *
     * @param role The role to check
     * @return true if the role should be assigned, false if it should be revoked
     *         or if it's not present in the roles map
     */
    fun shouldHaveRole(role: RoleType): Boolean =
        roles[role] ?: false
}
