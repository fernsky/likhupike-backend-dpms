package np.sthaniya.dpis.auth.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

/**
 * Represents a user entity in the Digital Profile Information System.
 * 
 * This entity implements Spring Security's [UserDetails] interface for authentication and authorization.
 * Users can be either ward-level or system-level users, with different permissions and access levels.
 * The entity includes audit fields for tracking user approval and deletion status.
 *
 * @property email The unique email address of the user, used as the username for authentication
 * @property password The encrypted password of the user
 * @property isWardLevelUser Indicates if the user has ward-level access restrictions
 * @property wardNumber The ward number this user is associated with (null for system-level users)
 * @property isApproved Indicates if the user account has been approved
 * @property approvedBy UUID of the admin who approved this user
 * @property approvedAt Timestamp when the user was approved
 * @property isDeleted Soft deletion flag
 * @property deletedAt Timestamp when the user was deleted
 * @property deletedBy UUID of the admin who deleted this user
 * @property permissions Set of permissions assigned to this user
 */
@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_users_email", columnList = "email")
    ],
)
class User :
    UuidBaseEntity(),
    UserDetails {
    
    @Column(nullable = false, unique = true)
    var email: String? = null

    @Column(nullable = false)
    private var password: String? = null

    @Column(name="is_ward_level_user", nullable = false)
    var isWardLevelUser: Boolean = false

    @Column(name = "ward_number")
    var wardNumber: Int? = null

    @Column(name = "is_approved")
    var isApproved: Boolean = false

    @Column(name = "approved_by")
    var approvedBy: UUID? = null

    @Column(name = "approved_at")
    var approvedAt: LocalDateTime? = null

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

    @Column(name = "deleted_by")
    var deletedBy: UUID? = null

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    private var permissions: MutableSet<UserPermission> = mutableSetOf()

    /**
     * Returns the authorities granted to the user.
     * Converts user permissions to Spring Security's SimpleGrantedAuthority format.
     *
     * @return Set of GrantedAuthority representing the user's permissions
     */
    override fun getAuthorities() = permissions
        .map { SimpleGrantedAuthority("PERMISSION_${it.permission.type}") }
        .toSet()

    /**
     * Returns the password used to authenticate the user.
     *
     * @return The user's encrypted password
     */
    override fun getPassword() = password

    /**
     * Returns the username used to authenticate the user.
     * In this implementation, the email address is used as the username.
     *
     * @return The user's email address
     */
    override fun getUsername() = email

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true as account expiration is not implemented
     */
    override fun isAccountNonExpired() = true

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true as account locking is not implemented
     */
    override fun isAccountNonLocked() = true

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true as credential expiration is not implemented
     */
    override fun isCredentialsNonExpired() = true

    /**
     * Indicates whether the user is enabled or disabled.
     * A user is considered disabled if they have been marked as deleted.
     *
     * @return false if the user is deleted, true otherwise
     */
    override fun isEnabled() = !isDeleted

    /**
     * Sets a new password for the user.
     * Note: The password should be encrypted before calling this method.
     *
     * @param newPassword The new encrypted password
     */
    fun setPassword(newPassword: String) {
        password = newPassword
    }

    /**
     * Adds a permission to the user.
     * If the permission already exists, it will be replaced.
     *
     * @param permission The permission to add
     */
    fun addPermission(permission: Permission) {
        removePermission(permission)
        permissions.add(UserPermission(this, permission))
    }

    /**
     * Removes a permission from the user.
     *
     * @param permission The permission to remove
     */
    fun removePermission(permission: Permission) {
        permissions.removeIf { it.permission.type == permission.type }
    }

    /**
     * Checks if the user has a specific permission.
     *
     * @param permissionType The type of permission to check
     * @return true if the user has the permission, false otherwise
     */
    fun hasPermission(permissionType: PermissionType): Boolean =
        permissions.any { it.permission.type == permissionType }

    /**
     * Returns all permissions assigned to the user.
     *
     * @return Set of all permissions
     */
    fun getPermissions(): Set<Permission> =
        permissions.map { it.permission }.toSet()

    /**
     * Removes all permissions from the user.
     */
    fun clearPermissions() {
        permissions.clear()
    }
}
