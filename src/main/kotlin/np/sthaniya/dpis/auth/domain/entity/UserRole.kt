package np.sthaniya.dpis.auth.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity

/**
 * Represents the many-to-many relationship between users and roles in the system.
 *
 * This entity serves as a join table between [User] and [Role] entities,
 * maintaining the association between users and their assigned roles.
 * It includes additional auditing capabilities inherited from [UuidBaseEntity].
 *
 * Features:
 * - Unique constraint on user-role combination
 * - Lazy loading of relationships for optimal performance
 * - Inherits UUID-based identification and auditing from [UuidBaseEntity]
 *
 * Usage:
 * ```kotlin
 * val userRole = UserRole.create(user, role)
 * // or
 * val userRole = UserRole(user, role)
 * ```
 *
 * @property user The user to whom the role is assigned
 * @property role The role being assigned to the user
 */
@Entity
@Table(
    name = "user_roles",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_user_role",
            columnNames = ["user_id", "role_type"]
        )
    ]
)
class UserRole(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_type", nullable = false, referencedColumnName = "type")
    var role: Role
) : UuidBaseEntity() {
    
    companion object {
        /**
         * Factory method to create a new UserRole instance.
         * 
         * @param user The user to whom the role will be assigned
         * @param role The role to be assigned
         * @return A new UserRole instance
         */
        fun create(user: User, role: Role) = UserRole(user, role)
    }
}
