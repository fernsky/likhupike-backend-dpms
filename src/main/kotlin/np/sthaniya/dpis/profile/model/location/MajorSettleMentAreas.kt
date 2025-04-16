package np.sthaniya.dpis.profile.model.location

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity

@Entity
@Table(name = "major_settlement_areas")
class MajorSettlementAreas(
    @Column(nullable = false)
    var name: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    var ward: Ward
) : UuidBaseEntity()
