package np.sthaniya.dpis.auth.repository.specification

import jakarta.persistence.criteria.JoinType
import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.domain.entity.UserPermission
import np.sthaniya.dpis.auth.domain.entity.Role
import np.sthaniya.dpis.auth.domain.entity.UserRole
import np.sthaniya.dpis.auth.dto.UserSearchCriteria
import org.springframework.data.jpa.domain.Specification

/**
 * Object providing specification builders for dynamic User entity queries.
 *
 * This object encapsulates:
 * - Dynamic query criteria building
 * - Complex join handling
 * - Search and filter specifications
 *
 * Key Features:
 * - Type-safe specification building
 * - Support for complex search criteria
 * - Permission-based filtering
 * - Date range queries
 * - Ward-level filtering
 *
 * Usage:
 * ```
 * val spec = UserSpecifications.fromCriteria(criteria)
 * userRepository.findAll(spec)
 * ```
 */
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

    /**
     * Builds ward number specification.
     *
     * @param criteria Search criteria containing ward number
     * @return Specification filtering by ward number if provided
     */
    private fun withWardNumber(criteria: UserSearchCriteria) =
        Specification<User> { root, _, cb ->
            criteria.wardNumber?.let { wardNumber ->
                cb.equal(root.get<Int>("wardNumber"), wardNumber)
            }
        }

    /**
     * Builds search term specification for email pattern matching.
     *
     * @param criteria Search criteria containing search term
     * @return Specification for case-insensitive email search
     */
    private fun withSearchTerm(criteria: UserSearchCriteria) =
        Specification<User> { root, _, cb ->
            criteria.searchTerm?.let { term ->
                val pattern = "%${term.lowercase()}%"
                cb.like(cb.lower(root.get("email")), pattern)
            }
        }

    /**
     * Builds permission-based specification with proper joins.
     *
     * @param criteria Search criteria containing permission filters
     * @return Specification handling permission joins and filtering
     */
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

    
    /**
     * Builds role-based specification with proper joins.
     *
     * @param criteria Search criteria containing role filters
     * @return Specification handling role joins and filtering
     */
    private fun withRoles(criteria: UserSearchCriteria) =
        Specification<User> { root, query, _ ->
            criteria.roles?.let { roles ->
                if (roles.isNotEmpty()) {
                    query?.distinct(true)
                    val rolesJoin =
                        root
                            .join<User, UserRole>("roles", JoinType.LEFT)
                            .join<UserRole, Role>("role", JoinType.LEFT)
                    rolesJoin.get<Any>("type").`in`(roles)
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
