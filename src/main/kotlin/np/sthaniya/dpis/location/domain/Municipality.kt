package np.sthaniya.dpis.location.domain

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.BaseEntity
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.locationtech.jts.geom.Polygon
import java.math.BigDecimal

@Entity
@Table(
    name = "municipalities",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_municipality_code_district",
            columnNames = ["code", "district_code"],
        ),
    ],
    indexes = [
        Index(name = "idx_municipalities_name", columnList = "name"),
        Index(name = "idx_municipalities_code", columnList = "code"),
        Index(name = "idx_municipalities_district", columnList = "district_code"),
        Index(name = "idx_municipalities_type", columnList = "type"),
    ],
)
class Municipality : BaseEntity() {
    @Id
    @Column(nullable = false, length = 36, unique = true)
    var code: String? = null

    @Column(nullable = false, length = 100)
    var name: String? = null

    @Column(nullable = false, length = 100)
    var nameNepali: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var type: MunicipalityType? = null

    @Column(precision = 10, scale = 2)
    var area: BigDecimal? = null

    @Column
    var population: Long? = null

    @Column(precision = 10, scale = 6)
    var latitude: BigDecimal? = null

    @Column(precision = 10, scale = 6)
    var longitude: BigDecimal? = null

    @Column
    var totalWards: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code", nullable = false, referencedColumnName = "code")
    var district: District? = null

    @OneToMany(mappedBy = "municipality", cascade = [CascadeType.ALL])
    var wards: MutableSet<Ward> = mutableSetOf()

    @Transient
    var distanceInMeters: Double? = null

    @Column(columnDefinition = "geometry")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    var geometry: Polygon? = null

    fun addWard(ward: Ward) {
        wards.add(ward)
        ward.municipality = this
    }

    fun removeWard(ward: Ward) {
        wards.remove(ward)
        ward.municipality = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Municipality) return false
        if (!super.equals(other)) return false
        return code == other.code && district?.code == other.district?.code
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (district?.code?.hashCode() ?: 0)
        return result
    }
}

enum class MunicipalityType {
    METROPOLITAN_CITY,
    SUB_METROPOLITAN_CITY,
    MUNICIPALITY,
    RURAL_MUNICIPALITY,
    ;

    companion object {
        fun fromString(type: String): MunicipalityType? = values().find { it.name.equals(type, ignoreCase = true) }
    }
}
