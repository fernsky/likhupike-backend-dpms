package np.sthaniya.dpis.auth.repository.specification

import jakarta.persistence.criteria.JoinType
import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.domain.entity.UserPermission
import np.sthaniya.dpis.auth.dto.UserSearchCriteria
import org.springframework.data.jpa.domain.Specification

object UserSpecifications {
    fun fromCriteria(criteria: UserSearchCriteria): Specification<User> =
        Specification
            .where(withWardNumber(criteria))
            .and(withSearchTerm(criteria))
            .and(withPermissions(criteria))
            .and(withApprovalStatus(criteria))
            .and(withWardLevelStatus(criteria))
            .and(withCreatedDateRange(criteria))
            .and(withEmail(criteria))
            .and(notDeleted())

    private fun withWardNumber(criteria: UserSearchCriteria) =
        Specification<User> { root, _, cb ->
            criteria.wardNumber?.let { wardNumber ->
                cb.equal(root.get<Int>("wardNumber"), wardNumber)
            }
        }

    private fun withSearchTerm(criteria: UserSearchCriteria) =
        Specification<User> { root, _, cb ->
            criteria.searchTerm?.let { term ->
                val pattern = "%${term.lowercase()}%"
                cb.like(cb.lower(root.get("email")), pattern)
            }
        }

    private fun withPermissions(criteria: UserSearchCriteria) =
        Specification<User> { root, query, _ ->
            criteria.permissions?.let { permissions -> 
                if (permissions.isNotEmpty()) {
                    query?.distinct(true)
                    val permissionsJoin =
                        root
                            .join<User, UserPermission>("permissions", JoinType.LEFT)
                            .join<UserPermission, Permission>("permission", JoinType.LEFT)
                    permissionsJoin.get<Any>("type").`in`(permissions)
                } else {
                    null
                }
            }
        }

    private fun withApprovalStatus(criteria: UserSearchCriteria) =
        Specification<User> { root, _, cb ->
            criteria.isApproved?.let { isApproved ->
                cb.equal(root.get<Boolean>("isApproved"), isApproved)
            }
        }

    private fun withWardLevelStatus(criteria: UserSearchCriteria) =
        Specification<User> { root, _, cb ->
            criteria.isWardLevelUser?.let { isWardLevelUser ->
                cb.equal(root.get<Boolean>("isWardLevelUser"), isWardLevelUser)
            }
        }

    private fun withEmail(criteria: UserSearchCriteria) =
        Specification<User> { root, _, cb ->
            criteria.email?.let { email ->
                cb.equal(cb.lower(root.get("email")), email.lowercase())
            }
        }

    private fun withCreatedDateRange(criteria: UserSearchCriteria) =
        Specification<User> { root, _, cb ->
            val predicates = mutableListOf<jakarta.persistence.criteria.Predicate>()

            criteria.createdAfter?.let {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), it.atStartOfDay()))
            }
            criteria.createdBefore?.let {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), it.plusDays(1).atStartOfDay()))
            }

            if (predicates.isEmpty()) null else cb.and(*predicates.toTypedArray())
        }

    private fun notDeleted() =
        Specification<User> { root, _, cb ->
            cb.equal(root.get<Boolean>("isDeleted"), false)
        }
}
