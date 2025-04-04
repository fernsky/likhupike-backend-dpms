package np.sthaniya.dpis.location.domain

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.BaseEntity
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.locationtech.jts.geom.Polygon
import java.io.Serializable
import java.math.BigDecimal

@Entity
@Table(
    name = "wards",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_ward_number_municipality",
            columnNames = ["ward_number", "municipality_code"],
        ),
    ],
    indexes = [
        Index(name = "idx_wards_number", columnList = "ward_number"),
        Index(name = "idx_wards_municipality", columnList = "municipality_code"),
    ],
)
@IdClass(WardId::class)
class Ward : BaseEntity() {
    @Id
    @Column(name = "ward_number", nullable = false)
    var wardNumber: Int? = null

    @Id
    @Column(name = "municipality_code", nullable = false, insertable = false, updatable = false)
    var municipalityCode: String? = null

    @Column(precision = 10, scale = 2)
    var area: BigDecimal? = null

    @Column
    var population: Long? = null

    @Column(precision = 10, scale = 6)
    var latitude: BigDecimal? = null

    @Column(precision = 10, scale = 6)
    var longitude: BigDecimal? = null

    @Column(length = 100)
    var officeLocation: String? = null

    @Column(length = 100)
    var officeLocationNepali: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_code", nullable = false, referencedColumnName = "code")
    var municipality: Municipality? = null
        set(value) {
            field = value
            municipalityCode = value?.code
        }

    @Column(columnDefinition = "geometry")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    var geometry: Polygon? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ward) return false
        if (!super.equals(other)) return false
        return wardNumber == other.wardNumber &&
            municipality?.code == other.municipality?.code
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (wardNumber ?: 0)
        result = 31 * result + (municipality?.code?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "Ward(number=$wardNumber, municipality=${municipality?.name})"
}

/**
 * Composite ID class for Ward entity.
 * 
 * This class represents the composite primary key consisting of:
 * - wardNumber: The ward number within the municipality
 * - municipalityCode: The code of the municipality to which the ward belongs
 */
class WardId : Serializable {
    var wardNumber: Int? = null
    var municipalityCode: String? = null

    constructor()

    constructor(wardNumber: Int?, municipalityCode: String?) {
        this.wardNumber = wardNumber
        this.municipalityCode = municipalityCode
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WardId) return false
        return wardNumber == other.wardNumber &&
                municipalityCode == other.municipalityCode
    }

    override fun hashCode(): Int {
        var result = wardNumber ?: 0
        result = 31 * result + (municipalityCode?.hashCode() ?: 0)
        return result
    }
}
