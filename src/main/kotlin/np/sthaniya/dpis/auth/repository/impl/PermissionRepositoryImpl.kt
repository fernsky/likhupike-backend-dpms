package np.sthaniya.dpis.auth.repository.impl

import jakarta.persistence.EntityManager
import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.repository.PermissionRepositoryCustom

/**
 * Implementation of custom Permission repository operations.
 *
 * This class provides:
 * - Optimized permission queries
 * - Criteria API based implementations
 * - Efficient set operations
 *
 * Features:
 * - Type-safe query building
 * - Set-based permission lookups
 * - Minimal database round trips
 *
 * @property entityManager JPA EntityManager for query execution
 */
class PermissionRepositoryImpl(
    private val entityManager: EntityManager
) : PermissionRepositoryCustom {

    /**
     * Implements efficient batch permission lookup by types.
     *
     * This implementation:
     * - Uses a single query for all types
     * - Returns results as a set for uniqueness
     * - Optimizes for bulk operations
     *
     * @param types Set of permission types to find
     * @return Set of matching Permission entities
     */
    override fun findByTypes(types: Set<PermissionType>): Set<Permission> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Permission::class.java)
        val root = query.from(Permission::class.java)

        query.where(root.get<PermissionType>("type").`in`(types))

        return entityManager.createQuery(query)
            .resultList
            .toSet()
    }
}
