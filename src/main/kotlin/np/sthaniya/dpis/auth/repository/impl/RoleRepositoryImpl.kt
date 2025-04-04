package np.sthaniya.dpis.auth.repository.impl

import jakarta.persistence.EntityManager
import np.sthaniya.dpis.auth.domain.entity.Role
import np.sthaniya.dpis.auth.domain.enums.RoleType
import np.sthaniya.dpis.auth.repository.RoleRepositoryCustom

/**
 * Implementation of custom Role repository operations.
 *
 * This class provides:
 * - Optimized role queries
 * - Criteria API based implementations
 * - Efficient set operations
 *
 * Features:
 * - Type-safe query building
 * - Set-based role lookups
 * - Minimal database round trips
 *
 * @property entityManager JPA EntityManager for query execution
 */
class RoleRepositoryImpl(
    private val entityManager: EntityManager
) : RoleRepositoryCustom {

    /**
     * Implements efficient batch role lookup by types.
     *
     * This implementation:
     * - Uses a single query for all types
     * - Returns results as a set for uniqueness
     * - Optimizes for bulk operations
     *
     * @param types Set of role types to find
     * @return Set of matching Role entities
     */
    override fun findByTypes(types: Set<RoleType>): Set<Role> {
        if (types.isEmpty()) {
            return emptySet()
        }
        
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Role::class.java)
        val root = query.from(Role::class.java)

        query.where(root.get<RoleType>("type").`in`(types))

        return entityManager.createQuery(query)
            .resultList
            .toSet()
    }
}
