package np.sthaniya.dpis.profile.model.location

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import java.math.BigDecimal

@Entity
@Table(name = "ward")
class Ward(
    @Column(nullable = false)
    var number: Int,
    
    @Column(nullable = false)
    var area: BigDecimal,
    
    @Column(nullable = true)
    var formingLocalBody: String? = null,
    
    @Column(nullable = true)
    var formingConstituentWards: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    var municipality: Municipality,
    
    @OneToMany(mappedBy = "ward", cascade = [CascadeType.ALL], orphanRemoval = true)
    val majorSettlementAreas: MutableSet<MajorSettlementAreas> = mutableSetOf()
) : UuidBaseEntity()
