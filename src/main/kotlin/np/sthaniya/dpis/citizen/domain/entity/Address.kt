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
 * Note: This is designed to work with @AttributeOverride in the parent entity
 * to specify the actual column names with appropriate prefixes.
 */
@Embeddable
class Address {
    /**
     * Province code - stored directly without relationship to improve performance
     */
    @Column(name = "province_code")
    private var provinceCode: String? = null

    /**
     * District code - stored directly without relationship to improve performance
     */
    @Column(name = "district_code")
    private var districtCode: String? = null

    /**
     * Municipality code - stored directly without relationship to improve performance
     */
    @Column(name = "municipality_code")
    private var municipalityCode: String? = null

    /**
     * Ward number - stored directly without relationship to improve performance
     */
    @Column(name = "ward_number")
    private var wardNumber: Int? = null

    /**
     * Ward municipality code - stored directly without relationship to improve performance
     */
    @Column(name = "ward_municipality_code")
    private var wardMunicipalityCode: String? = null

    /**
     * Street address or local identifier
     */
    @Column(name = "street_address")
    var streetAddress: String? = null

    // Transient relationships for convenient access
    @Transient
    var province: Province? = null
        get() = if (provinceCode != null) {
            Province().apply { code = provinceCode!! }
        } else null
        set(value) {
            field = value
            provinceCode = value?.code
        }

    @Transient
    var district: District? = null
        get() = if (districtCode != null) {
            District().apply { code = districtCode!! }
        } else null
        set(value) {
            field = value
            districtCode = value?.code
        }

    @Transient
    var municipality: Municipality? = null
        get() = if (municipalityCode != null) {
            Municipality().apply { code = municipalityCode!! }
        } else null
        set(value) {
            field = value
            municipalityCode = value?.code
        }

    @Transient
    var ward: Ward? = null
        get() = if (wardNumber != null && wardMunicipalityCode != null) {
            Ward().apply { 
                this.wardNumber = this@Address.wardNumber!!
                this.municipality = Municipality().apply {
                    code = wardMunicipalityCode!!
                }
            }
        } else null
        set(value) {
            field = value
            wardNumber = value?.wardNumber
            wardMunicipalityCode = value?.municipality?.code
        }

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

        if (provinceCode != other.provinceCode) return false
        if (districtCode != other.districtCode) return false
        if (municipalityCode != other.municipalityCode) return false
        if (wardNumber != other.wardNumber) return false
        if (wardMunicipalityCode != other.wardMunicipalityCode) return false
        if (streetAddress != other.streetAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = provinceCode?.hashCode() ?: 0
        result = 31 * result + (districtCode?.hashCode() ?: 0)
        result = 31 * result + (municipalityCode?.hashCode() ?: 0)
        result = 31 * result + (wardNumber ?: 0)
        result = 31 * result + (wardMunicipalityCode?.hashCode() ?: 0)
        result = 31 * result + (streetAddress?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Address(provinceCode=$provinceCode, districtCode=$districtCode, " +
               "municipalityCode=$municipalityCode, wardNumber=$wardNumber, " +
               "wardMunicipalityCode=$wardMunicipalityCode, streetAddress=$streetAddress)"
    }
}
