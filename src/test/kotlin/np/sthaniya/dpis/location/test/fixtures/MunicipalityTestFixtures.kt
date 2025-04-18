package np.sthaniya.dpis.location.test.fixtures

import np.sthaniya.dpis.location.api.dto.enums.MunicipalityField
import np.sthaniya.dpis.location.api.dto.request.CreateMunicipalityRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateMunicipalityRequest
import np.sthaniya.dpis.location.api.dto.response.*
import np.sthaniya.dpis.location.domain.District
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.MunicipalityType
import np.sthaniya.dpis.location.domain.Ward
import np.sthaniya.dpis.common.dto.GeometryRequest
import np.sthaniya.dpis.common.util.GeometryConverter
import org.geojson.GeoJsonObject
import org.geojson.LngLatAlt
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LinearRing
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.geom.impl.CoordinateArraySequence
import java.math.BigDecimal

object MunicipalityTestFixtures {
    private val geometryFactory = GeometryFactory()
    private val defaultGeometryConverter = GeometryConverter()

    fun createMunicipality(
        district: District = DistrictTestFixtures.createDistrict(),
        name: String = "Test Municipality",
        nameNepali: String = "परीक्षण नगरपालिका".uppercase(),
        code: String = "TEST-M",
        type: MunicipalityType = MunicipalityType.MUNICIPALITY,
        area: BigDecimal = BigDecimal("100.50"),
        population: Long = 50000L,
        latitude: BigDecimal = BigDecimal("27.7172"),
        longitude: BigDecimal = BigDecimal("85.3240"),
        totalWards: Int = 12,
    ) = Municipality().apply {
        this.district = district
        this.name = name
        this.nameNepali = nameNepali
        this.code = code
        this.type = type
        this.area = area
        this.population = population
        this.latitude = latitude
        this.longitude = longitude
        this.totalWards = totalWards
    }

    fun createMunicipalityRequest(
        name: String = "Test Municipality",
        nameNepali: String = "परीक्षण नगरपालिका".uppercase(),
        code: String = "TEST-M",
        type: MunicipalityType = MunicipalityType.MUNICIPALITY,
        area: BigDecimal = BigDecimal("100.50"),
        population: Long = 50000L,
        latitude: BigDecimal = BigDecimal("27.7172"),
        longitude: BigDecimal = BigDecimal("85.3240"),
        totalWards: Int = 12,
        districtCode: String = "TEST-D",
        geometry: GeometryRequest =
            GeometryRequest(
                type = "Polygon",
                coordinates =
                    listOf(
                        arrayOf(85.3240, 27.7172),
                        arrayOf(85.3340, 27.7172),
                        arrayOf(85.3340, 27.7272),
                        arrayOf(85.3240, 27.7272),
                        arrayOf(85.3240, 27.7172),
                    ),
            ),
    ) = CreateMunicipalityRequest(
        name = name,
        nameNepali = nameNepali,
        code = code,
        type = type,
        area = area,
        population = population,
        latitude = latitude,
        longitude = longitude,
        totalWards = totalWards,
        districtCode = districtCode,
        geometry = geometry,
    )

    fun createUpdateMunicipalityRequest(
        name: String = "Updated Municipality",
        nameNepali: String = "अद्यावधिक नगरपालिका".uppercase(),
        area: BigDecimal = BigDecimal("150.75"),
        population: Long = 75000L,
        latitude: BigDecimal = BigDecimal("27.7173"),
        longitude: BigDecimal = BigDecimal("85.3241"),
        totalWards: Int = 15,
        geometry: GeometryRequest? =
            GeometryRequest(
                type = "Polygon",
                coordinates =
                    listOf(
                        arrayOf(85.3241, 27.7173),
                        arrayOf(85.3341, 27.7173),
                        arrayOf(85.3341, 27.7273),
                        arrayOf(85.3241, 27.7273),
                        arrayOf(85.3241, 27.7173),
                    ),
            ),
    ) = UpdateMunicipalityRequest(
        name = name,
        nameNepali = nameNepali,
        area = area,
        population = population,
        latitude = latitude,
        longitude = longitude,
        totalWards = totalWards,
        geometry = geometry,
    )

    fun createMunicipalityResponse(
        code: String = "TEST-M",
        name: String = "Test Municipality",
        nameNepali: String = "परीक्षण नगरपालिका".uppercase(),
        type: MunicipalityType = MunicipalityType.MUNICIPALITY,
        area: BigDecimal = BigDecimal("100.50"),
        population: Long = 50000L,
        latitude: BigDecimal = BigDecimal("27.7172"),
        longitude: BigDecimal = BigDecimal("85.3240"),
        totalWards: Int = 12,
        district: DistrictSummaryResponse = DistrictTestFixtures.createDistrictSummaryResponse(),
    ) = MunicipalityResponse(
        code = code,
        name = name,
        nameNepali = nameNepali,
        type = type,
        area = area,
        population = population,
        latitude = latitude,
        longitude = longitude,
        totalWards = totalWards,
        district = district,
    )

    fun createMunicipalityDetailResponse(
        code: String = "TEST-M",
        name: String = "Test Municipality",
        nameNepali: String = "परीक्षण नगरपालिका".uppercase(),
        type: MunicipalityType = MunicipalityType.MUNICIPALITY,
        area: BigDecimal = BigDecimal("100.50"),
        population: Long = 50000L,
        latitude: BigDecimal = BigDecimal("27.7172"),
        longitude: BigDecimal = BigDecimal("85.3240"),
        totalWards: Int = 12,
        district: DistrictDetailResponse = DistrictTestFixtures.createDistrictDetailResponse(),
        geometry: GeoJsonObject? = createTestGeometry(),
    ) = MunicipalityDetailResponse(
        code = code,
        name = name,
        nameNepali = nameNepali,
        type = type,
        area = area,
        population = population,
        latitude = latitude,
        longitude = longitude,
        totalWards = totalWards,
        district = district,
        geometry = geometry,
    )

    fun createTestGeometry(): GeoJsonObject {
        val polygon = org.geojson.Polygon()

        // Create and set exterior ring first
        val exteriorRing =
            listOf(
                LngLatAlt(85.3240, 27.7172),
                LngLatAlt(85.3340, 27.7172),
                LngLatAlt(85.3340, 27.7272),
                LngLatAlt(85.3240, 27.7272),
                LngLatAlt(85.3240, 27.7172), // Close the ring
            )
        polygon.coordinates = mutableListOf(exteriorRing)

        // Add interior ring if needed
        val interiorRing =
            listOf(
                LngLatAlt(85.3270, 27.7192),
                LngLatAlt(85.3310, 27.7192),
                LngLatAlt(85.3310, 27.7252),
                LngLatAlt(85.3270, 27.7252),
                LngLatAlt(85.3270, 27.7192), // Close the ring
            )
        polygon.coordinates.add(interiorRing)

        return polygon
    }

    fun createMunicipalitySummaryResponse(
        code: String = "TEST-M",
        name: String = "Test Municipality",
        nameNepali: String = "परीक्षण नगरपालिका",
        type: MunicipalityType = MunicipalityType.MUNICIPALITY,
        totalWards: Int = 12,
    ) = MunicipalitySummaryResponse(
        code = code,
        name = name,
        nameNepali = nameNepali,
        type = type,
        totalWards = totalWards,
    )

    fun createMunicipalityProjection(
        code: String,
        fields: Set<MunicipalityField> = MunicipalityField.DEFAULT_FIELDS,
        includeTotals: Boolean = false,
        includeGeometry: Boolean = false,
        includeWards: Boolean = false,
        geometryConverter: GeometryConverter = defaultGeometryConverter,
    ): DynamicMunicipalityProjection {
        val municipality = createMunicipality(code = code)
        val allFields = fields.toMutableSet()

        if (includeTotals) {
            allFields.addAll(
                setOf(
                    MunicipalityField.TOTAL_AREA,
                    MunicipalityField.TOTAL_POPULATION,
                    MunicipalityField.TOTAL_WARDS, // Changed from WARD_COUNT to TOTAL_WARDS
                ),
            )
        }
        if (includeGeometry) {
            allFields.add(MunicipalityField.GEOMETRY)
            municipality.geometry = createTestJTSPolygon()
        }
        if (includeWards) {
            allFields.add(MunicipalityField.WARDS)
            municipality.wards = createTestWards(municipality).toMutableSet()
        }

        return DynamicMunicipalityProjection.from(municipality, allFields, geometryConverter)
    }

    fun createSearchTestData(): List<Municipality> =
        listOf(
            Municipality().apply {
                name = "Kathmandu Metropolitan City"
                nameNepali = "काठमाडौं महानगरपालिका"
                code = "KMC"
                type = MunicipalityType.METROPOLITAN_CITY
                population = 975453
                area = BigDecimal("49.45")
                geometry = createTestJTSPolygon()
            },
            Municipality().apply {
                name = "Lalitpur Metropolitan City"
                nameNepali = "ललितपुर महानगरपालिका"
                code = "LMC"
                type = MunicipalityType.METROPOLITAN_CITY
                population = 284922
                area = BigDecimal("37.4")
                geometry = createTestJTSPolygon()
            },
            Municipality().apply {
                name = "Bharatpur Metropolitan City"
                nameNepali = "भरतपुर महानगरपालिका"
                code = "BMC"
                type = MunicipalityType.METROPOLITAN_CITY
                population = 280502
                area = BigDecimal("432.95")
                geometry = createTestJTSPolygon()
            },
            Municipality().apply {
                name = "Pokhara Metropolitan City"
                nameNepali = "पोखरा महानगरपालिका"
                code = "PMC"
                type = MunicipalityType.METROPOLITAN_CITY
                population = 402995
                area = BigDecimal("464.24")
                geometry = createTestJTSPolygon()
            },
        )

    private fun createTestJTSPolygon(): Polygon {
        val coordinates =
            arrayOf(
                Coordinate(85.3240, 27.7172),
                Coordinate(85.3340, 27.7172),
                Coordinate(85.3340, 27.7272),
                Coordinate(85.3240, 27.7272),
                Coordinate(85.3240, 27.7172), // Close the ring
            )

        val ring = LinearRing(CoordinateArraySequence(coordinates), geometryFactory)
        return geometryFactory.createPolygon(ring)
    }

    private fun createTestWards(municipality: Municipality): Set<Ward> =
        (1..3)
            .map { number ->
                WardTestFixtures.createWard(
                    municipality = municipality,
                    wardNumber = number,
                )
            }.toSet()
}
