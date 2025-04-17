package np.sthaniya.dpis.profile.location.model

import jakarta.persistence.*
import java.math.BigDecimal
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.locationtech.jts.geom.Polygon

@Entity
@Table(name = "profile_municipality")
class ProfileMunicipality(
        @Column(nullable = false) var province: String,
        @Column(nullable = false) var district: String,
        @Column(nullable = false) var rightmostLatitude: BigDecimal,
        @Column(nullable = false) var leftmostLatitude: BigDecimal,
        @Column(nullable = false) var bottommostLongitude: BigDecimal,
        @Column(nullable = false) var topmostLongitude: BigDecimal,
        @Column(nullable = true) var lowestAltitude: BigDecimal? = null,
        @Column(nullable = true) var highestAltitude: BigDecimal? = null,
        @Column(nullable = false) var areaInSquareKilometers: BigDecimal,
        @Column(nullable = false) var name: String,
        
        /**
         * Geographic boundary of the municipality represented as a polygon.
         * This field allows for precise representation of the municipality's shape and enables
         * spatial queries like containment checks and boundary calculations.
         *
         * The polygon is stored in the database using the default SRID (4326 - WGS84).
         */
        @Column(nullable = true, columnDefinition = "geometry(Polygon,4326)")
        var boundary: Polygon? = null,
        
        @OneToMany(
                mappedBy = "municipality",
                cascade = [CascadeType.ALL],
                orphanRemoval = true
        )
        val wards: MutableSet<ProfileWard> = mutableSetOf()
) : UuidBaseEntity()
