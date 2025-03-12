package np.likhupikemun.dpms.auth.domain.entity

import jakarta.persistence.*
import np.likhupikemun.dpms.auth.domain.enum.PermissionType
import np.likhupikemun.dpms.common.entity.BaseEntity

@Entity
@Table(name = "permissions")
class Permission(
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: PermissionType
) : BaseEntity() {
    val label: String get() = type.name
    
    @OneToMany(mappedBy = "permission", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val userPermissions: MutableSet<UserPermission> = mutableSetOf()

    fun getAuthority() = type.getAuthority()
}