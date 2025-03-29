package np.sthaniya.dpis.auth.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity

/**
 * Represents the many-to-many relationship between users and permissions in the system.
 *
 * This entity serves as a join table between [User] and [Permission] entities,
 * maintaining the association between users and their assigned permissions.
 * It includes additional auditing capabilities inherited from [UuidBaseEntity].
 *
 * Features:
 * - Unique constraint on user-permission combination
 * - Lazy loading of relationships for optimal performance
 * - Inherits UUID-based identification and auditing from [UuidBaseEntity]
 *
 * Usage:
 * ```kotlin
 * val userPermission = UserPermission.create(user, permission)
 * // or
 * val userPermission = UserPermission(user, permission)
 * ```
 *
 * @property user The user to whom the permission is assigned
 * @property permission The permission being assigned to the user
 */
@Entity
@Table(
    name = "user_permissions",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_user_permission",
            columnNames = ["user_id", "permission_type"]
        )
    ]
)
class UserPermission(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_type", nullable = false, referencedColumnName = "type")
    var permission: Permission
) : UuidBaseEntity() {
    
    companion object {
        /**
         * Factory method to create a new UserPermission instance.
         * 
         * @param user The user to whom the permission will be assigned
         * @param permission The permission to be assigned
         * @return A new UserPermission instance
         */
        fun create(user: User, permission: Permission) = UserPermission(user, permission)
    }
}
