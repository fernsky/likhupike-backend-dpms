package np.sthaniya.dpis.profile.location.model

import jakarta.persistence.*
import java.math.BigDecimal
import np.sthaniya.dpis.common.entity.UuidBaseEntity

@Entity
@Table(name = "profile_ward")
class ProfileWard(
        @Column(nullable = false) var number: Int,
        @Column(nullable = false) var area: BigDecimal,
        @Column(name = "forming_local_bodies", nullable = true)
        var formingLocalBodies: Array<String> = arrayOf(),
        @Column(name = "forming_constituent_wards", nullable = true)
        var formingConstituentWards: Array<String> = arrayOf(),
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "profile_municipality_id", nullable = false)
        var municipality: ProfileMunicipality,
        @OneToMany(mappedBy = "profile_ward", cascade = [CascadeType.ALL], orphanRemoval = true)
        val majorSettlementAreas: MutableSet<MajorSettlementAreas> = mutableSetOf()
) : UuidBaseEntity()
