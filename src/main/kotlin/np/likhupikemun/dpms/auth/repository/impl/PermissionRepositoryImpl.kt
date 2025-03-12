package np.likhupikemun.dpms.auth.repository

import jakarta.persistence.EntityManager
import np.likhupikemun.dpms.auth.domain.entity.Permission
import np.likhupikemun.dpms.auth.domain.enum.PermissionType

class PermissionRepositoryImpl(
    private val entityManager: EntityManager
) : PermissionRepositoryCustom {

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
