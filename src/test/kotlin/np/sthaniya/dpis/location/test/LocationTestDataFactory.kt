package np.sthaniya.dpis.location.test

import np.sthaniya.dpis.location.domain.*
import java.math.BigDecimal
import java.time.Instant

/**
 * Factory class for creating test location entities.
 */
object LocationTestDataFactory {
    
    /**
     * Creates a test Province entity.
     */
    fun createProvince(
        code: String = "P1",
        name: String = "Test Province",
        nameNepali: String = "परीक्षण प्रदेश",
        area: BigDecimal = BigDecimal("25905.00"),
        population: Long = 4972021,
        headquarter: String = "Test HQ",
        headquarterNepali: String = "परीक्षण सदरमुकाम"
    ): Province {
        return Province().apply {
            this.code = code
            this.name = name
            this.nameNepali = nameNepali
            this.area = area
            this.population = population
            this.headquarter = headquarter
            this.headquarterNepali = headquarterNepali
            this.createdAt = Instant.now()
            this.updatedAt = Instant.now()
        }
    }
    
    /**
     * Creates a test District entity.
     */
    fun createDistrict(
        code: String = "D1",
        name: String = "Test District",
        nameNepali: String = "परीक्षण जिल्ला",
        province: Province = createProvince(),
        area: BigDecimal = BigDecimal("1507.00"),
        population: Long = 182459,
        headquarter: String = "Test District HQ",
        headquarterNepali: String = "परीक्षण जिल्ला सदरमुकाम"
    ): District {
        return District().apply {
            this.code = code
            this.name = name
            this.nameNepali = nameNepali
            this.province = province
            this.area = area
            this.population = population
            this.headquarter = headquarter
            this.headquarterNepali = headquarterNepali
            this.createdAt = Instant.now()
            this.updatedAt = Instant.now()
        }
    }
    
    /**
     * Creates a test Municipality entity.
     */
    fun createMunicipality(
        code: String = "M1",
        name: String = "Test Municipality",
        nameNepali: String = "परीक्षण नगरपालिका",
        district: District = createDistrict(),
        type: MunicipalityType = MunicipalityType.MUNICIPALITY,
        area: BigDecimal = BigDecimal("49.45"),
        population: Long = 100000,
        latitude: BigDecimal = BigDecimal("27.700769"),
        longitude: BigDecimal = BigDecimal("85.300140"),
        totalWards: Int = 10
    ): Municipality {
        return Municipality().apply {
            this.code = code
            this.name = name
            this.nameNepali = nameNepali
            this.district = district
            this.type = type
            this.area = area
            this.population = population
            this.latitude = latitude
            this.longitude = longitude
            this.totalWards = totalWards
            this.createdAt = Instant.now()
            this.updatedAt = Instant.now()
        }
    }
    
    /**
     * Creates a test Ward entity.
     */
    fun createWard(
        wardNumber: Int = 1,
        municipality: Municipality = createMunicipality(),
        area: BigDecimal = BigDecimal("1.82"),
        population: Long = 10000,
        latitude: BigDecimal = BigDecimal("27.719660"),
        longitude: BigDecimal = BigDecimal("85.303901"),
        officeLocation: String = "Test Location",
        officeLocationNepali: String = "परीक्षण स्थान"
    ): Ward {
        return Ward().apply {
            this.wardNumber = wardNumber
            this.municipality = municipality
            this.area = area
            this.population = population
            this.latitude = latitude
            this.longitude = longitude
            this.officeLocation = officeLocation
            this.officeLocationNepali = officeLocationNepali
            this.createdAt = Instant.now()
            this.updatedAt = Instant.now()
        }
    }
}
