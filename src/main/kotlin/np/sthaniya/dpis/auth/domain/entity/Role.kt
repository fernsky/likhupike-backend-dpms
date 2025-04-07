package np.sthaniya.dpis.auth.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.auth.domain.enums.RoleType
import np.sthaniya.dpis.common.entity.BaseEntity

/**
 * Represents a system role entity that can be assigned to users.
 *
 * This entity defines the available roles in the system using [RoleType] enum values
 * as the primary key. Each role can be assigned to multiple users and can have multiple
 * permissions associated with it.
 *
 * Features:
 * - Uses enum value as natural primary key
 * - Bidirectional relationship with users through [UserRole]
 * - Bidirectional relationship with permissions through [RolePermission]
 * - Inherits auditing capabilities from [BaseEntity]
 *
 * Usage:
 * ```kotlin
 * val role = Role(RoleType.LAND_RECORDS_OFFICER)
 * user.addRole(role)
 * ```
 *
 * @property type The enum value representing the role type, serves as the primary key
 * @property userRoles Set of user associations for this role
 * @property rolePermissions Set of permission associations for this role
 * @property label Human-readable name of the role derived from the enum name
 */
@Entity
@Table(name = "roles")
class Role(
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: RoleType,

    @OneToMany(mappedBy = "role", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val userRoles: MutableSet<UserRole> = mutableSetOf(),
    
    @OneToMany(mappedBy = "role", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val rolePermissions: MutableSet<RolePermission> = mutableSetOf()
) : BaseEntity() {

    /**
     * Returns a human-readable label for the role.
     * This is derived directly from the role type's name.
     *
     * @return String representation of the role type
     */
    val label: String get() = type.name

    /**
     * Returns the Spring Security role authority string for this role.
     * Delegates to the [RoleType.getAuthority] implementation.
     *
     * @return String representation of the role used by Spring Security
     */
    fun getAuthority() = type.getAuthority()
    
    /**
     * Adds a permission to the role.
     * If the permission already exists, it will be replaced.
     *
     * @param permission The permission to add
     */
    fun addPermission(permission: Permission) {
        removePermission(permission)
        rolePermissions.add(RolePermission(this, permission))
    }

    /**
     * Removes a permission from the role.
     *
     * @param permission The permission to remove
     */
    fun removePermission(permission: Permission) {
        rolePermissions.removeIf { it.permission.type == permission.type }
    }

    /**
     * Returns all permissions assigned to the role.
     *
     * @return Set of all permissions
     */
    fun getPermissions(): Set<Permission> =
        rolePermissions?.mapNotNull { it?.permission }?.toSet() ?: emptySet()

    /**
     * Clears all permissions from the role.
     */
    fun clearPermissions() {
        rolePermissions.clear()
    }
}
