package np.likhupikemun.dpms.auth.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.JoinType
import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.domain.entity.UserPermission
import np.likhupikemun.dpms.auth.domain.entity.Permission
import np.likhupikemun.dpms.auth.repository.UserRepositoryCustom
import np.likhupikemun.dpms.auth.dto.UserProjection
import np.likhupikemun.dpms.auth.dto.UserProjectionImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

class UserRepositoryImpl(
    private val entityManager: EntityManager
) : UserRepositoryCustom {

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

    override fun findAllWithProjection(
        spec: Specification<User>,
        pageable: Pageable,
        columns: Set<String>
    ): Page<UserProjection> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(User::class.java)
        val root = query.from(User::class.java)

        // Add joins if permissions are requested
        if ("permissions" in columns) {
            root.fetch<User, UserPermission>("permissions", JoinType.LEFT)
                .fetch<UserPermission, Permission>("permission", JoinType.LEFT)
        }

        // Apply specifications
        query.where(spec.toPredicate(root, query, cb))

        // Apply sorting
        query.orderBy(
            pageable.sort.map { order ->
                if (order.isAscending) cb.asc(root.get<Any>(order.property))
                else cb.desc(root.get<Any>(order.property))
            }
        )

        // Execute query with pagination
        val totalQuery = cb.createQuery(Long::class.java)
        totalQuery.select(cb.count(totalQuery.from(User::class.java)))
        totalQuery.where(spec.toPredicate(totalQuery.from(User::class.java), totalQuery, cb))
        
        val total = entityManager.createQuery(totalQuery).singleResult

        val results = entityManager.createQuery(query)
            .setFirstResult(pageable.pageNumber * pageable.pageSize)
            .setMaxResults(pageable.pageSize)
            .resultList
            .map { user -> UserProjectionImpl(user as User, columns) }

        return PageImpl(results, pageable, total)
    }
}
