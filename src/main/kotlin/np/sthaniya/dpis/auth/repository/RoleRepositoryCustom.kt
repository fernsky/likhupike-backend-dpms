package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.Role
import np.sthaniya.dpis.auth.domain.enums.RoleType

/**
 * Custom repository interface for specialized Role entity operations.
 *
 * This interface provides:
 * - Batch role lookups
 * - Type-based role queries
 * - Optimized role set operations
 *
 * Features:
 * - Efficient role set retrieval
 * - Type-safe role queries
 * - Batch operation support
 */
interface RoleRepositoryCustom {
    /**
     * Finds all roles matching the given set of types.
     *
     * This operation:
     * - Efficiently fetches multiple roles
     * - Returns them as a set to ensure uniqueness
     * - Handles empty type sets gracefully
     *
     * @param types Set of RoleType to find
     * @return Set of matching Role entities
     */
    fun findByTypes(types: Set<RoleType>): Set<Role>
}
