package np.likhupikemun.dpis.auth.domain.entity

import jakarta.persistence.*
import np.likhupikemun.dpis.auth.domain.enums.PermissionType
import np.likhupikemun.dpis.common.entity.BaseEntity

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
    val label: String get() = type.name
    fun getAuthority() = type.getAuthority()
}
