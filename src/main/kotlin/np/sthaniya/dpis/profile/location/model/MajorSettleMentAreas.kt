package np.sthaniya.dpis.profile.location.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity

@Entity
@Table(name = "major_settlement_areas")
class MajorSettlementAreas(
        @Column(nullable = false) var name: String,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "profile_ward_id", nullable = false)
        var ward: ProfileWard
) : UuidBaseEntity()
