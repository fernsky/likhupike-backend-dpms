package np.sthaniya.dpis.auth.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.JoinType
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.domain.entity.UserPermission
import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.repository.UserRepositoryCustom
import np.sthaniya.dpis.auth.dto.UserProjection
import np.sthaniya.dpis.auth.dto.UserProjectionImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

/**
 * Implementation of [UserRepositoryCustom] that provides custom query functionality for [User] entities.
 * 
 * This implementation uses JPA Criteria API to build dynamic queries with efficient fetching strategies
 * for user permissions and related entities.
 *
 * @property entityManager The JPA [EntityManager] used for creating and executing queries
 */
class UserRepositoryImpl(
    private val entityManager: EntityManager
) : UserRepositoryCustom {

    /**
     * @see UserRepositoryCustom.findByEmailWithPermissions
     */
    override fun findByEmailWithPermissions(email: String): Optional<User> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(User::class.java)
        val root = query.from(User::class.java)
        
        root.fetch<User, UserPermission>("permissions", JoinType.LEFT)
            .fetch<UserPermission, Permission>("permission", JoinType.LEFT)

        query.where(
            cb.and(
                cb.equal(root.get<String>("email"), email),
                cb.equal(root.get<Boolean>("isDeleted"), false)
            )
        )

        return entityManager.createQuery(query)
            .resultList
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }

    /**
     * @see UserRepositoryCustom.findByIdWithPermissions
     */
    override fun findByIdWithPermissions(id: UUID): Optional<User> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(User::class.java)
        val root = query.from(User::class.java)
        
        root.fetch<User, UserPermission>("permissions", JoinType.LEFT)
            .fetch<UserPermission, Permission>("permission", JoinType.LEFT)

        query.where(
            cb.and(
                cb.equal(root.get<UUID>("id"), id),
                cb.equal(root.get<Boolean>("isDeleted"), false)
            )
        )

        return entityManager.createQuery(query)
            .resultList
            .firstOrNull()
            .let { Optional.ofNullable(it) }
    }

    /**
     * @see UserRepositoryCustom.findAllWithProjection
     *
     * The implementation handles distinct counting and fetching when permissions are included
     * in the requested columns or when the specification references permissions.
     */
    override fun findAllWithProjection(
        spec: Specification<User>,
        pageable: Pageable,
        columns: Set<String>
    ): Page<UserProjection> {
        val cb = entityManager.criteriaBuilder
        
        // Execute count query with distinct
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(User::class.java)
        
        // If permissions are in columns or spec contains permissions, we need distinct count
        val needsDistinct = "permissions" in columns || spec.toString().contains("permissions")
        
        if (needsDistinct) {
            countQuery.select(cb.countDistinct(countRoot))
            // Join permissions for count query if needed
            countRoot.join<User, UserPermission>("permissions", JoinType.LEFT)
                .join<UserPermission, Permission>("permission", JoinType.LEFT)
        } else {
            countQuery.select(cb.count(countRoot))
        }
        
        spec.toPredicate(countRoot, countQuery, cb)?.let { countQuery.where(it) }
        val total = entityManager.createQuery(countQuery).singleResult

        // If no results or invalid page, return empty page
        if (total == 0L || pageable.offset >= total) {
            return PageImpl(emptyList(), pageable, total)
        }

        // Execute main query with pagination
        val query = cb.createQuery(User::class.java)
        val root = query.from(User::class.java)
        
        if (needsDistinct) {
            query.distinct(true)
        }

        // Always join permissions if they're requested in columns
        if ("permissions" in columns) {
            root.fetch<User, UserPermission>("permissions", JoinType.LEFT)
                .fetch<UserPermission, Permission>("permission", JoinType.LEFT)
        }

        spec.toPredicate(root, query, cb)?.let { query.where(it) }

        if (pageable.sort.isSorted) {
            val orders = mutableListOf<jakarta.persistence.criteria.Order>()
            pageable.sort.forEach { order ->
                val path = root.get<Any>(order.property)
                orders.add(
                    if (order.isAscending) cb.asc(path) else cb.desc(path)
                )
            }
            query.orderBy(*orders.toTypedArray())
        }

        val results = entityManager.createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
            .map { user -> UserProjectionImpl(user as User, columns) }

        return PageImpl(results, pageable, total)
    }

    /**
     * @see UserRepositoryCustom.countDistinct
     */
    override fun countDistinct(spec: Specification<User>): Long {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Long::class.java)
        val root = query.from(User::class.java)
        
        // Select distinct count
        query.select(cb.countDistinct(root))
        
        // Add the specification predicate if it exists
        spec.toPredicate(root, query, cb)?.let { query.where(it) }
        
        return entityManager.createQuery(query).singleResult
    }
}
