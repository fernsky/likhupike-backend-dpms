package np.likhupikemun.dpis.auth.repository.impl

import jakarta.persistence.EntityManager
import np.likhupikemun.dpis.auth.domain.entity.Permission
import np.likhupikemun.dpis.auth.domain.enums.PermissionType
import np.likhupikemun.dpis.auth.repository.PermissionRepositoryCustom

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
