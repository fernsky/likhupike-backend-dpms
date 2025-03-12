package np.likhupikemun.dpms.auth.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.JoinType
import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.domain.entity.UserPermission
import np.likhupikemun.dpms.auth.domain.entity.Permission
import np.likhupikemun.dpms.auth.repository.UserRepositoryCustom
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
}
