package np.sthaniya.dpis.profile.location.model

import jakarta.persistence.*
import java.math.BigDecimal
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.locationtech.jts.geom.Polygon

@Entity
@Table(name = "profile_ward")
class ProfileWard(
        @Column(nullable = false) var number: Int,
        @Column(nullable = false) var area: BigDecimal,
        @Column(name = "forming_local_bodies", nullable = true)
        var formingLocalBodies: Array<String> = arrayOf(),
        @Column(name = "forming_constituent_wards", nullable = true)
        var formingConstituentWards: Array<String> = arrayOf(),
        
        /**
         * Geographic boundary of the ward represented as a polygon.
         * This field allows for precise representation of the ward's shape and enables
         * spatial queries like containment checks and boundary calculations.
         *
         * The polygon is stored in the database using the default SRID (4326 - WGS84).
         */
        @Column(nullable = true, columnDefinition = "geometry(Polygon,4326)")
        var boundary: Polygon? = null,
        
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "profile_municipality_id", nullable = false)
        var municipality: ProfileMunicipality,
        
        @OneToMany(mappedBy = "ward", cascade = [CascadeType.ALL], orphanRemoval = true)
        val majorSettlementAreas: MutableSet<MajorSettlementAreas> = mutableSetOf()
) : UuidBaseEntity()
