package np.sthaniya.dpis.auth.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity

/**
 * Represents the many-to-many relationship between roles and permissions in the system.
 *
 * This entity serves as a join table between [Role] and [Permission] entities,
 * maintaining the association between roles and their assigned permissions.
 * It includes additional auditing capabilities inherited from [UuidBaseEntity].
 *
 * Features:
 * - Unique constraint on role-permission combination
 * - Lazy loading of relationships for optimal performance
 * - Inherits UUID-based identification and auditing from [UuidBaseEntity]
 *
 * Usage:
 * ```kotlin
 * val rolePermission = RolePermission.create(role, permission)
 * // or
 * val rolePermission = RolePermission(role, permission)
 * ```
 *
 * @property role The role to which the permission is assigned
 * @property permission The permission being assigned to the role
 */
@Entity
@Table(
    name = "role_permissions",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_role_permission",
            columnNames = ["role_type", "permission_type"]
        )
    ]
)
class RolePermission(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_type", nullable = false, referencedColumnName = "type")
    var role: Role,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_type", nullable = false, referencedColumnName = "type")
    var permission: Permission
) : UuidBaseEntity() {
    
    companion object {
        /**
         * Factory method to create a new RolePermission instance.
         * 
         * @param role The role to which the permission will be assigned
         * @param permission The permission to be assigned
         * @return A new RolePermission instance
         */
        fun create(role: Role, permission: Permission) = RolePermission(role, permission)
    }
}
