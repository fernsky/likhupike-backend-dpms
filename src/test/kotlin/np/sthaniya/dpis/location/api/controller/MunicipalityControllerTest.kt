package np.sthaniya.dpis.location.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.test.UserTestDataFactory
import np.sthaniya.dpis.common.BaseIntegrationTest
import np.sthaniya.dpis.location.api.dto.request.CreateMunicipalityRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateMunicipalityRequest
import np.sthaniya.dpis.location.domain.District
import np.sthaniya.dpis.location.domain.MunicipalityType
import np.sthaniya.dpis.location.repository.DistrictRepository
import np.sthaniya.dpis.location.repository.MunicipalityRepository
import np.sthaniya.dpis.location.repository.ProvinceRepository
import np.sthaniya.dpis.location.test.fixtures.DistrictTestFixtures
import np.sthaniya.dpis.location.test.fixtures.MunicipalityTestFixtures
import np.sthaniya.dpis.location.test.fixtures.ProvinceTestFixtures
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

class MunicipalityControllerIntegrationTest : BaseIntegrationTest() {
    
    @Autowired
    private lateinit var provinceRepository: ProvinceRepository
    
    @Autowired
    private lateinit var districtRepository: DistrictRepository
    
    @Autowired
    private lateinit var municipalityRepository: MunicipalityRepository
    
    private lateinit var testDistrict: District
    private lateinit var testAdmin: User

    private lateinit var createMunicipalityRequest: CreateMunicipalityRequest
    private lateinit var updateMunicipalityRequest: UpdateMunicipalityRequest
    
    @BeforeEach
    override fun setUp() {
        // Call the parent setUp() to initialize MockMvc, etc.
        super.setUp()
        
        // Delete all existing municipalities, districts, and provinces to start fresh
        municipalityRepository.deleteAll()
        districtRepository.deleteAll()
        provinceRepository.deleteAll()
        
        // Create test province and save it to the database
        val testProvince = ProvinceTestFixtures.createProvince(
            code = "TEST-P1",
            name = "Test Province",
            nameNepali = "परीक्षण प्रदेश"
        )
        provinceRepository.save(testProvince)
        
        // Create test district and save it to the database
        testDistrict = DistrictTestFixtures.createDistrict(
            code = "TEST-D1",
            name = "Test District",
            nameNepali = "परीक्षण जिल्ला",
            province = testProvince
        )
        districtRepository.save(testDistrict)
        
        // Create test admin user
        testAdmin = UserTestDataFactory.createSystemAdministrator()
        
        // Create non-null values for request objects
        val area = BigDecimal("100.50")
        val population = 50000L
        val latitude = BigDecimal("27.7172")
        val longitude = BigDecimal("85.3240")
        val totalWards = 12
        
        // Create request objects for API calls
        createMunicipalityRequest = MunicipalityTestFixtures.createMunicipalityRequest(
            code = "TEST-M1",
            name = "Test Municipality",
            nameNepali = "परीक्षण नगरपालिका",
            districtCode = testDistrict.code!!,
            type = MunicipalityType.MUNICIPALITY,
            area = area,
            population = population,
            latitude = latitude,
            longitude = longitude,
            totalWards = totalWards
        )
        
        updateMunicipalityRequest = MunicipalityTestFixtures.createUpdateMunicipalityRequest(
            name = "Updated Municipality",
            nameNepali = "अद्यावधिक नगरपालिका",
            area = BigDecimal("150.75"),
            population = 75000L,
            latitude = BigDecimal("27.7173"),
            longitude = BigDecimal("85.3241"),
            totalWards = 15
        )
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should create municipality`() {
        // Create a municipality using the API
        mockMvc.perform(post("/api/v1/municipalities")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createMunicipalityRequest)))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(createMunicipalityRequest.code))
            .andExpect(jsonPath("$.data.name").value(createMunicipalityRequest.name))
            .andExpect(jsonPath("$.message").value("Municipality created successfully"))
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should get municipality by code`() {
        // First create a municipality in the database
        val municipality = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M2",
            name = "Test Municipality 2",
            nameNepali = "परीक्षण नगरपालिका २",
            district = testDistrict,
            type = MunicipalityType.MUNICIPALITY,
            area = BigDecimal("100.50"),
            population = 50000L
        )
        municipalityRepository.save(municipality)
        
        // Get the municipality using the API
        mockMvc.perform(get("/api/v1/municipalities/${municipality.code}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(municipality.code))
            .andExpect(jsonPath("$.data.name").value(municipality.name))
            .andExpect(jsonPath("$.data.district.code").value(testDistrict.code))
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should update municipality`() {
        // First create a municipality in the database
        val municipality = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M3",
            name = "Test Municipality 3",
            nameNepali = "परीक्षण नगरपालिका ३",
            district = testDistrict,
            type = MunicipalityType.MUNICIPALITY,
            area = BigDecimal("100.50"),
            population = 50000L
        )
        municipalityRepository.save(municipality)
        
        // Update the municipality using the API
        mockMvc.perform(put("/api/v1/municipalities/${municipality.code}")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateMunicipalityRequest)))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(municipality.code))
            .andExpect(jsonPath("$.data.name").value(updateMunicipalityRequest.name))
            .andExpect(jsonPath("$.message").value("Municipality updated successfully"))
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should search municipalities`() {
        // Create multiple municipalities in the database
        val municipality1 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M4",
            name = "Kathmandu",
            nameNepali = "काठमाडौं",
            district = testDistrict,
            type = MunicipalityType.METROPOLITAN_CITY,
            population = 1000000L
        )
        val municipality2 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M5",
            name = "Bhaktapur",
            nameNepali = "भक्तपुर",
            district = testDistrict,
            type = MunicipalityType.MUNICIPALITY,
            population = 300000L
        )
        val municipality3 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M6",
            name = "Lalitpur",
            nameNepali = "ललितपुर",
            district = testDistrict,
            type = MunicipalityType.METROPOLITAN_CITY,
            population = 500000L
        )
        municipalityRepository.saveAll(listOf(municipality1, municipality2, municipality3))
        
        // Search for municipalities with 'pur' in the name
        mockMvc.perform(get("/api/v1/municipalities/search")
            .param("name", "pur"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(3)) // Should find Bhaktapur and Lalitpur
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Found")))
            
        // Search by minimum population
        mockMvc.perform(get("/api/v1/municipalities/search")
            .param("minPopulation", "600000"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(1)) // Should find only Kathmandu
            
        // Search by type
        mockMvc.perform(get("/api/v1/municipalities/search")
            .param("type", "METROPOLITAN_CITY"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(3)) // Should find Kathmandu and Lalitpur
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should get municipalities by district code`() {
        // Create multiple municipalities in the database for the same district
        val municipality1 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M7",
            name = "Municipality 7",
            district = testDistrict
        )
        val municipality2 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M8",
            name = "Municipality 8",
            district = testDistrict
        )
        municipalityRepository.saveAll(listOf(municipality1, municipality2))
        
        // Create another district and municipalities in that district
        val anotherDistrict = DistrictTestFixtures.createDistrict(code = "TEST-D2")
        districtRepository.save(anotherDistrict)
        
        val municipality3 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M9",
            name = "Municipality 9",
            district = anotherDistrict
        )
        municipalityRepository.save(municipality3)
        
        // Get municipalities for the test district
        mockMvc.perform(get("/api/v1/municipalities/by-district/${testDistrict.code}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.length()").value(2)) // Should find 2 municipalities for the test district
            .andExpect(jsonPath("$.data[0].district.code").value(testDistrict.code))
            .andExpect(jsonPath("$.data[1].district.code").value(testDistrict.code))
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should get municipalities by type`() {
        // Create municipalities with different types
        val ruralMunicipality = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M10",
            name = "Rural Municipality",
            district = testDistrict,
            type = MunicipalityType.RURAL_MUNICIPALITY
        )
        
        val municipality = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M11",
            name = "Municipality",
            district = testDistrict,
            type = MunicipalityType.MUNICIPALITY
        )
        
        val subMetro = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M12",
            name = "Sub-Metropolitan City",
            district = testDistrict,
            type = MunicipalityType.SUB_METROPOLITAN_CITY
        )
        
        municipalityRepository.saveAll(listOf(ruralMunicipality, municipality, subMetro))
        
        // Get municipalities by type
        mockMvc.perform(get("/api/v1/municipalities/by-type/MUNICIPALITY"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data[0].code").value(municipality.code))
            .andExpect(jsonPath("$.data[0].type").value(MunicipalityType.MUNICIPALITY.toString()))
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should find nearby municipalities`() {
        // Create municipalities with different coordinates
        val municipality1 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M13",
            name = "Central Municipality",
            district = testDistrict,
            latitude = BigDecimal("27.7000"),
            longitude = BigDecimal("85.3000")
        )
        
        val municipality2 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M14",
            name = "Nearby Municipality",
            district = testDistrict,
            latitude = BigDecimal("27.7050"),
            longitude = BigDecimal("85.3050")
        )
        
        val municipality3 = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M15",
            name = "Far Municipality",
            district = testDistrict,
            latitude = BigDecimal("28.0000"),
            longitude = BigDecimal("86.0000")
        )
        
        municipalityRepository.saveAll(listOf(municipality1, municipality2, municipality3))
        
        // Search for municipalities near a specific location
        mockMvc.perform(get("/api/v1/municipalities/nearby")
            .param("latitude", "27.7000")
            .param("longitude", "85.3000")
            .param("radiusKm", "10.0"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(2)) // Should find the central and nearby municipalities
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should get all municipalities`() {
        // Create multiple municipalities in the database
        val municipalities = (1..5).map { i ->
            MunicipalityTestFixtures.createMunicipality(
                code = "TEST-M2$i",
                name = "Municipality $i",
                district = testDistrict
            )
        }
        municipalityRepository.saveAll(municipalities)
        
        // Get all municipalities
        mockMvc.perform(get("/api/v1/municipalities"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.length()").value(municipalities.size))
    }
}
