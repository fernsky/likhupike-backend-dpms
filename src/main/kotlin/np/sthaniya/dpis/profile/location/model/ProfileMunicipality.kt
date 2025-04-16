package np.sthaniya.dpis.profile.location.model

import jakarta.persistence.*
import java.math.BigDecimal
import np.sthaniya.dpis.common.entity.UuidBaseEntity

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
        @OneToMany(
                mappedBy = "municipality", // Changed from "profile_municipality" to "municipality"
                cascade = [CascadeType.ALL],
                orphanRemoval = true
        )
        val wards: MutableSet<ProfileWard> = mutableSetOf()
) : UuidBaseEntity()
