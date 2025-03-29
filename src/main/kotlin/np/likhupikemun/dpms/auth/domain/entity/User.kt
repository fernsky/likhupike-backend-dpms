package np.likhupikemun.dpis.auth.domain.entity

import jakarta.persistence.*
import np.likhupikemun.dpis.common.entity.UuidBaseEntity
import np.likhupikemun.dpis.auth.domain.enums.PermissionType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

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

    // UserDetails implementation
    override fun getAuthorities() = permissions
        .map { SimpleGrantedAuthority("PERMISSION_${it.permission.type}") }
        .toSet()

    override fun getPassword() = password

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = !isDeleted

    // Password management
    fun setPassword(newPassword: String) {
        password = newPassword
    }

    // Permission management
    fun addPermission(permission: Permission) {
        // Remove existing permission first to avoid duplicate
        removePermission(permission)
        permissions.add(UserPermission(this, permission))
    }

    fun removePermission(permission: Permission) {
        permissions.removeIf { it.permission.type == permission.type }
    }

    fun hasPermission(permissionType: PermissionType): Boolean =
        permissions.any { it.permission.type == permissionType }

    fun getPermissions(): Set<Permission> =
        permissions.map { it.permission }.toSet()

    fun clearPermissions() {
        permissions.clear()
    }
}
