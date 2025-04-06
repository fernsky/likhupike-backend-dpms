package np.sthaniya.dpis.location.api.controller

import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.test.UserTestDataFactory
import np.sthaniya.dpis.common.BaseIntegrationTest
import np.sthaniya.dpis.location.api.dto.request.CreateWardRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateWardRequest
import np.sthaniya.dpis.location.domain.District
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.MunicipalityType
import np.sthaniya.dpis.location.repository.DistrictRepository
import np.sthaniya.dpis.location.repository.MunicipalityRepository
import np.sthaniya.dpis.location.repository.ProvinceRepository
import np.sthaniya.dpis.location.repository.WardRepository
import np.sthaniya.dpis.location.test.fixtures.DistrictTestFixtures
import np.sthaniya.dpis.location.test.fixtures.MunicipalityTestFixtures
import np.sthaniya.dpis.location.test.fixtures.ProvinceTestFixtures
import np.sthaniya.dpis.location.test.fixtures.WardTestFixtures
import org.hamcrest.Matchers.containsString
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

class WardControllerIntegrationTest : BaseIntegrationTest() {
    
    @Autowired
    private lateinit var provinceRepository: ProvinceRepository
    
    @Autowired
    private lateinit var districtRepository: DistrictRepository
    
    @Autowired
    private lateinit var municipalityRepository: MunicipalityRepository
    
    @Autowired
    private lateinit var wardRepository: WardRepository
    
    private lateinit var testMunicipality: Municipality
    private lateinit var testDistrict: District
    private lateinit var testAdmin: User

    private lateinit var createWardRequest: CreateWardRequest
    private lateinit var updateWardRequest: UpdateWardRequest
    
    private val testWardNumber = 1
    
    @BeforeEach
    override fun setUp() {
        // Call the parent setUp() to initialize MockMvc, etc.
        super.setUp()
        
        // Delete all existing data to start fresh
        wardRepository.deleteAll()
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
        
        // Create test municipality and save it to the database
        testMunicipality = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M1",
            name = "Test Municipality",
            nameNepali = "परीक्षण नगरपालिका",
            district = testDistrict,
            type = MunicipalityType.MUNICIPALITY,
            area = BigDecimal("100.50"),
            population = 50000L,
            totalWards = 12
        )
        municipalityRepository.save(testMunicipality)
        
        // Create test admin user
        testAdmin = UserTestDataFactory.createSystemAdministrator()
        
        // Create request objects for API calls
        createWardRequest = WardTestFixtures.createWardRequest(
            municipalityCode = testMunicipality.code!!,
            wardNumber = testWardNumber,
            area = BigDecimal("10.00"),
            population = 1000L,
            latitude = BigDecimal("27.7172"),
            longitude = BigDecimal("85.3240"),
            officeLocation = "Test Office",
            officeLocationNepali = "परीक्षण कार्यालय"
        )
        
        updateWardRequest = WardTestFixtures.createUpdateWardRequest(
            area = BigDecimal("15.00"),
            population = 1500L,
            latitude = BigDecimal("27.7173"),
            longitude = BigDecimal("85.3241"),
            officeLocation = "Updated Office",
            officeLocationNepali = "अद्यावधिक कार्यालय"
        )
    }
    
    // @Test
    // @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    // fun `should create ward`() {
    //     // Create a ward using the API
    //     mockMvc.perform(post("/api/v1/wards")
    //         .with(csrf())
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(objectMapper.writeValueAsString(createWardRequest)))
    //         .andDo(print())
    //         .andExpect(status().isOk)
    //         .andExpect(jsonPath("$.success").value(true))
    //         .andExpect(jsonPath("$.data.wardNumber").value(createWardRequest.wardNumber))
    //         .andExpect(jsonPath("$.data.municipalityCode").value(createWardRequest.municipalityCode))
    //         .andExpect(jsonPath("$.message").value("Ward created successfully"))
    // }
    
    @Test
    @WithMockUser(username = "admin@system.com", roles = ["SYSTEM_ADMINISTRATOR"]) 
    fun `should get ward by municipality code and ward number`() {
        // First create a ward in the database
        val ward = WardTestFixtures.createWard(
            wardNumber = 2,
            municipality = testMunicipality,
            area = BigDecimal("10.00"),
            population = 1000L
        )
        wardRepository.save(ward)
        
        // Get the ward using the API
        mockMvc.perform(get("/api/v1/wards/${testMunicipality.code}/${ward.wardNumber}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.wardNumber").value(ward.wardNumber))
    }
    
    // @Test
    // @WithMockUser(username = "admin@system.com", roles = ["SYSTEM_ADMINISTRATOR"])
    // fun `should update ward`() {
    //     // First create a ward in the database
    //     val ward = WardTestFixtures.createWard(
    //         wardNumber = 3,
    //         municipality = testMunicipality,
    //         area = BigDecimal("10.00"),
    //         population = 1000L
    //     )
    //     wardRepository.save(ward)
        
    //     // Update the ward using the API
    //     mockMvc.perform(put("/api/v1/wards/${testMunicipality.code}/${ward.wardNumber}")
    //         .with(csrf())
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(objectMapper.writeValueAsString(updateWardRequest)))
    //         .andDo(print())
    //         .andExpect(status().isOk)
    //         .andExpect(jsonPath("$.success").value(true))
    //         .andExpect(jsonPath("$.data.wardNumber").value(ward.wardNumber))
    //         .andExpect(jsonPath("$.data.municipalityCode").value(testMunicipality.code))
    //         .andExpect(jsonPath("$.message").value("Ward updated successfully"))
    // }
    
    @Test
    @WithMockUser(username = "admin@system.com", roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should search wards`() {
        // Create multiple wards in the database
        val ward1 = WardTestFixtures.createWard(
            wardNumber = 1,
            municipality = testMunicipality,
            area = BigDecimal("12.50"),
            population = 2500L
        )
        
        val ward2 = WardTestFixtures.createWard(
            wardNumber = 2,
            municipality = testMunicipality,
            area = BigDecimal("8.75"),
            population = 1800L
        )
        
        val ward3 = WardTestFixtures.createWard(
            wardNumber = 3,
            municipality = testMunicipality,
            area = BigDecimal("15.30"),
            population = 3200L
        )
        
        wardRepository.saveAll(listOf(ward1, ward2, ward3))
        
        // Search for wards by minimum population
        mockMvc.perform(get("/api/v1/wards/search")
            .param("minPopulation", "2000"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(2)) // Should find wards 1 and 3
            .andExpect(jsonPath("$.message").value(containsString("Found")))
        
        // Search by municipality code
        mockMvc.perform(get("/api/v1/wards/search")
            .param("municipalityCode", testMunicipality.code))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(3)) // Should find all 3 wards
    }
    
    @Test
    @WithMockUser(username = "admin@system.com", roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should get wards by municipality code`() {
        // Create multiple wards in the database for the same municipality
        val wards = (1..5).map { i ->
            WardTestFixtures.createWard(
                wardNumber = i,
                municipality = testMunicipality
            )
        }
        wardRepository.saveAll(wards)
        
        // Create another municipality and wards in that municipality
        val anotherMunicipality = MunicipalityTestFixtures.createMunicipality(
            code = "TEST-M2", 
            district = testDistrict
        )
        municipalityRepository.save(anotherMunicipality)
        
        val wardInAnotherMunicipality = WardTestFixtures.createWard(
            wardNumber = 1,
            municipality = anotherMunicipality
        )
        wardRepository.save(wardInAnotherMunicipality)
        
        // Get wards for the test municipality
        mockMvc.perform(get("/api/v1/wards/by-municipality/${testMunicipality.code}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }
    
    @Test
    @WithMockUser(username = "admin@system.com", roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should find nearby wards`() {
        // Create wards with different coordinates
        val ward1 = WardTestFixtures.createWard(
            wardNumber = 1,
            municipality = testMunicipality,
            latitude = BigDecimal("27.7000"),
            longitude = BigDecimal("85.3000")
        )
        
        val ward2 = WardTestFixtures.createWard(
            wardNumber = 2,
            municipality = testMunicipality,
            latitude = BigDecimal("27.7050"),
            longitude = BigDecimal("85.3050")
        )
        
        val ward3 = WardTestFixtures.createWard(
            wardNumber = 3,
            municipality = testMunicipality,
            latitude = BigDecimal("28.0000"),
            longitude = BigDecimal("86.0000")
        )
        
        wardRepository.saveAll(listOf(ward1, ward2, ward3))
        
        // Search for wards near a specific location
        mockMvc.perform(get("/api/v1/wards/nearby")
            .param("latitude", "27.7000")
            .param("longitude", "85.3000")
            .param("radiusKm", "10.0"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(2)) // Should find wards 1 and 2
    }
}
