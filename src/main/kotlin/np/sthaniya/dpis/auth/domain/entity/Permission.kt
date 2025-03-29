package np.sthaniya.dpis.auth.domain.entity

/**
 * Represents a system permission entity that can be assigned to users.
 *
 * This entity defines the available permissions in the system using [PermissionType] enum values
 * as the primary key. Each permission can be assigned to multiple users through the [UserPermission]
 * join entity.
 *
 * Features:
 * - Uses enum value as natural primary key
 * - Bidirectional relationship with users through [UserPermission]
 * - Inherits auditing capabilities from [BaseEntity]
 *
 * Usage:
 * ```kotlin
 * val permission = Permission(PermissionType.READ_USER)
 * user.addPermission(permission)
 * ```
 *
 * @property type The enum value representing the permission type, serves as the primary key
 * @property userPermissions Set of user associations for this permission
 * @property label Human-readable name of the permission derived from the enum name
 */
@Entity
@Table(name = "permissions")
class Permission(
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: PermissionType,

    @OneToMany(mappedBy = "permission", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val userPermissions: MutableSet<UserPermission> = mutableSetOf()
) : BaseEntity() {

    /**
     * Returns a human-readable label for the permission.
     * This is derived directly from the permission type's name.
     *
     * @return String representation of the permission type
     */
    val label: String get() = type.name

    /**
     * Returns the Spring Security authority string for this permission.
     * Delegates to the [PermissionType.getAuthority] implementation.
     *
     * @return String representation of the authority used by Spring Security
     */
    fun getAuthority() = type.getAuthority()
}
