package np.sthaniya.dpis.auth.repository

/**
 * Custom repository interface for specialized Permission entity operations.
 *
 * This interface provides:
 * - Batch permission lookups
 * - Type-based permission queries
 * - Optimized permission set operations
 *
 * Features:
 * - Efficient permission set retrieval
 * - Type-safe permission queries
 * - Batch operation support
 */
interface PermissionRepositoryCustom {
    /**
     * Finds all permissions matching the given set of types.
     *
     * This operation:
     * - Efficiently fetches multiple permissions
     * - Returns them as a set to ensure uniqueness
     * - Handles empty type sets gracefully
     *
     * @param types Set of PermissionType to find
     * @return Set of matching Permission entities
     */
    fun findByTypes(types: Set<PermissionType>): Set<Permission>
}
