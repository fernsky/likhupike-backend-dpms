package np.sthaniya.dpis.auth.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import np.sthaniya.dpis.auth.domain.enums.RoleType
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
 * This class uses JPA inheritance and can be extended by specific user types like Citizen.
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
 * @property roles Set of roles assigned to this user
 */
@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_users_email", columnList = "email")
    ],
)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@DiscriminatorValue("USER")
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
    
    @Column(name = "phone_number")
    var phoneNumber: String? = null

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    private var permissions: MutableSet<UserPermission> = mutableSetOf()

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    private var roles: MutableSet<UserRole> = mutableSetOf()

    /**
     * Returns the authorities granted to the user.
     * Combines both direct permissions and role-based authorities.
     *
     * @return Set of GrantedAuthority representing the user's permissions and roles
     */
    override fun getAuthorities(): Set<SimpleGrantedAuthority> {
        val authorities = mutableSetOf<SimpleGrantedAuthority>()
        
        // Add direct permissions
        permissions.forEach { 
            authorities.add(SimpleGrantedAuthority(it.permission.getAuthority())) 
        }
        
        // Add role authorities
        roles.forEach { 
            authorities.add(SimpleGrantedAuthority(it.role.getAuthority()))
        }
        
        // Add permissions from roles (these won't override direct permissions)
        roles.forEach { userRole ->
            userRole.role.getPermissions().forEach { permission ->
                // Only add if not already directly assigned
                if (!hasPermission(permission.type)) {
                    authorities.add(SimpleGrantedAuthority(permission.getAuthority()))
                }
            }
        }
        
        return authorities
    }

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

    /**
     * Adds a role to the user.
     * If the role already exists, it will be replaced.
     *
     * @param role The role to add
     */
    fun addRole(role: Role) {
        removeRole(role)
        roles.add(UserRole(this, role))
    }

    /**
     * Removes a role from the user.
     *
     * @param role The role to remove
     */
    fun removeRole(role: Role) {
        roles.removeIf { it.role.type == role.type }
    }

    /**
     * Checks if the user has a specific role.
     *
     * @param roleType The type of role to check
     * @return true if the user has the role, false otherwise
     */
    fun hasRole(roleType: RoleType): Boolean =
        roles.any { it.role.type == roleType }

    /**
     * Returns all roles assigned to the user.
     *
     * @return Set of all roles
     */
    fun getRoles(): Set<Role> =
        roles.map { it.role }.toSet()

    /**
     * Clears all roles from the user.
     */
    fun clearRoles() {
        roles.clear()
    }

    /**
     * Gets the effective permissions of the user, combining direct permissions and those from roles.
     * Direct permissions take precedence over role-based permissions.
     * 
     * @return Set of all effective permission types
     */
    fun getEffectivePermissions(): Set<PermissionType> {
        val effectivePermissions = mutableSetOf<PermissionType>()
        
        // Add permissions from roles
        roles.forEach { userRole ->
            userRole.role.getPermissions().forEach { 
                effectivePermissions.add(it.type)
            }
        }
        
        // Add direct permissions (these will override any from roles due to Set behavior)
        permissions.forEach { 
            effectivePermissions.add(it.permission.type)
        }
        
        return effectivePermissions
    }

    /**
     * Checks if the user effectively has a specific permission, either directly
     * or through an assigned role.
     *
     * @param permissionType The type of permission to check
     * @return true if the user has the permission directly or through a role, false otherwise
     */
    fun hasEffectivePermission(permissionType: PermissionType): Boolean {
        // Check direct permissions first
        if (hasPermission(permissionType)) {
            return true
        }
        
        // Check permissions from roles
        return roles.any { userRole ->
            userRole.role.getPermissions().any { it.type == permissionType }
        }
    }
}
