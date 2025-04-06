package np.sthaniya.dpis.citizen.domain.entity

import jakarta.persistence.*
import np.sthaniya.dpis.location.domain.District
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.Province
import np.sthaniya.dpis.location.domain.Ward

/**
 * Represents a physical address in Nepal's administrative structure.
 * 
 * This embeddable entity contains address components following Nepal's
 * federal structure with provinces, districts, municipalities, and wards.
 * It can be embedded into other entities like Citizen for representing
 * permanent or temporary addresses.
 * 
 * Foreign key relationships are established with location entities (Province, District, 
 * Municipality, Ward) but without cascading delete operations.
 */
@Embeddable
class Address {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "province_code", referencedColumnName = "code", nullable = false)
    var province: Province? = null
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_code", referencedColumnName = "code", nullable = false)
    var district: District? = null
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipality_code", referencedColumnName = "code", nullable = false)
    var municipality: Municipality? = null
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(
        JoinColumn(name = "ward_number", referencedColumnName = "ward_number", nullable = false),
        JoinColumn(name = "ward_municipality_code", referencedColumnName = "municipality_code", nullable = false)
    )
    var ward: Ward? = null
    
    @Column(name = "street_address")
    var streetAddress: String? = null

    // No-arg constructor required for JPA
    constructor()

    constructor(
        province: Province?,
        district: District?,
        municipality: Municipality?,
        ward: Ward?,
        streetAddress: String?
    ) {
        this.province = province
        this.district = district
        this.municipality = municipality
        this.ward = ward
        this.streetAddress = streetAddress
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Address) return false

        if (province?.code != other.province?.code) return false
        if (district?.code != other.district?.code) return false
        if (municipality?.code != other.municipality?.code) return false
        if (ward?.wardNumber != other.ward?.wardNumber ||
            ward?.municipality?.code != other.ward?.municipality?.code) return false
        if (streetAddress != other.streetAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = province?.code?.hashCode() ?: 0
        result = 31 * result + (district?.code?.hashCode() ?: 0)
        result = 31 * result + (municipality?.code?.hashCode() ?: 0)
        result = 31 * result + (ward?.wardNumber ?: 0)
        result = 31 * result + (ward?.municipality?.code?.hashCode() ?: 0)
        result = 31 * result + (streetAddress?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Address(province=${province?.name}, district=${district?.name}, " +
               "municipality=${municipality?.name}, ward=${ward?.wardNumber}, " +
               "streetAddress=$streetAddress)"
    }
}
