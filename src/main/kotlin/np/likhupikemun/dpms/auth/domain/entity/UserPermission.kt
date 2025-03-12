package np.likhupikemun.dpms.auth.domain.entity

import jakarta.persistence.*
import np.likhupikemun.dpms.common.entity.UuidBaseEntity

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
        fun create(user: User, permission: Permission) = UserPermission(user, permission)
    }
}