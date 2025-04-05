package np.sthaniya.dpis.location.api.controller

import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.test.UserTestDataFactory
import np.sthaniya.dpis.common.BaseIntegrationTest
import np.sthaniya.dpis.location.api.dto.request.CreateProvinceRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateProvinceRequest
import np.sthaniya.dpis.location.repository.DistrictRepository
import np.sthaniya.dpis.location.repository.ProvinceRepository
import np.sthaniya.dpis.location.test.fixtures.ProvinceTestFixtures
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

class ProvinceControllerIntegrationTest : BaseIntegrationTest() {
    
    @Autowired
    private lateinit var provinceRepository: ProvinceRepository
    
    @Autowired
    private lateinit var districtRepository: DistrictRepository
    
    private lateinit var testAdmin: User

    private lateinit var createProvinceRequest: CreateProvinceRequest
    private lateinit var updateProvinceRequest: UpdateProvinceRequest
    
    @BeforeEach
    override fun setUp() {
        // Call the parent setUp() to initialize MockMvc, etc.
        super.setUp()
        
        // Delete all existing districts and provinces to start fresh
        districtRepository.deleteAll()
        provinceRepository.deleteAll()
        
        // Create test admin user
        testAdmin = UserTestDataFactory.createSystemAdministrator()
        
        // Create request objects for API calls using the test fixtures
        createProvinceRequest = ProvinceTestFixtures.createProvinceRequest(
            code = "TEST-P1",
            name = "Test Province",
            nameNepali = "परीक्षण प्रदेश",
            area = BigDecimal("25905.00"),
            population = 4972021L,
            headquarter = "Test Headquarter",
            headquarterNepali = "परीक्षण सदरमुकाम"
        )
        
        updateProvinceRequest = ProvinceTestFixtures.createUpdateProvinceRequest(
            name = "Updated Province",
            nameNepali = "अद्यावधिक प्रदेश",
            area = BigDecimal("26000.00"),
            population = 5000000L,
            headquarter = "Updated Headquarter",
            headquarterNepali = "अद्यावधिक सदरमुकाम"
        )
    }
    
    @Test
    @WithUserDetails("admin@system.com")
    fun `should create province`() {
        // Create a province using the API
        mockMvc.perform(post("/api/v1/provinces")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createProvinceRequest)))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(createProvinceRequest.code))
            .andExpect(jsonPath("$.data.name").value(createProvinceRequest.name))
            .andExpect(jsonPath("$.message").value("Province created successfully"))
    }
    
    @Test
    @WithUserDetails("admin@system.com")
    fun `should get province by code`() {
        // First create a province in the database
        val province = ProvinceTestFixtures.createProvince(
            code = "TEST-P2",
            name = "Test Province 2",
            nameNepali = "परीक्षण प्रदेश २"
        )
        provinceRepository.save(province)
        
        // Get the province using the API
        mockMvc.perform(get("/api/v1/provinces/${province.code}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(province.code))
            .andExpect(jsonPath("$.data.name").value(province.name))
            .andExpect(jsonPath("$.data.nameNepali").value(province.nameNepali))
    }
    
    @Test
    @WithUserDetails("admin@system.com")
    fun `should update province`() {
        // First create a province in the database
        val province = ProvinceTestFixtures.createProvince(
            code = "TEST-P3",
            name = "Test Province 3",
            nameNepali = "परीक्षण प्रदेश ३"
        )
        provinceRepository.save(province)
        
        // Update the province using the API
        mockMvc.perform(put("/api/v1/provinces/${province.code}")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateProvinceRequest)))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(province.code))
            .andExpect(jsonPath("$.data.name").value(updateProvinceRequest.name))
            .andExpect(jsonPath("$.message").value("Province updated successfully"))
    }
    
    @Test
    @WithUserDetails("admin@system.com")
    fun `should search provinces`() {
        // Create multiple provinces in the database
        val province1 = ProvinceTestFixtures.createProvince(
            code = "TEST-P4",
            name = "Bagmati Pradesh",
            nameNepali = "बागमती प्रदेश",
            population = 6000000L,
            area = BigDecimal("20300.00")
        )
        
        val province2 = ProvinceTestFixtures.createProvince(
            code = "TEST-P5",
            name = "Gandaki Pradesh",
            nameNepali = "गण्डकी प्रदेश",
            population = 2500000L,
            area = BigDecimal("21504.00")
        )
        
        val province3 = ProvinceTestFixtures.createProvince(
            code = "TEST-P6",
            name = "Lumbini Pradesh",
            nameNepali = "लुम्बिनी प्रदेश",
            population = 4500000L,
            area = BigDecimal("22288.00")
        )
        
        provinceRepository.saveAll(listOf(province1, province2, province3))
        
        // Search for provinces with 'Bagmati' in the name
        mockMvc.perform(get("/api/v1/provinces/search")
            .param("name", "Bagmati"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content.length()").value(1))
            .andExpect(jsonPath("$.data.content[0].code").value(province1.code))
            .andExpect(jsonPath("$.message").value(containsString("Found")))
            
        // Search by minimum population
        mockMvc.perform(get("/api/v1/provinces/search")
            .param("minPopulation", "5000000"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content.length()").value(1)) // Should find only Bagmati
    }
    
    @Test
    @WithUserDetails("admin@system.com")
    fun `should find large provinces`() {
        // Create multiple provinces in the database with different sizes
        val provinces = ProvinceTestFixtures.createSearchTestData()
        provinceRepository.saveAll(provinces)
        
        // Search for large provinces
        mockMvc.perform(get("/api/v1/provinces/large")
            .param("minArea", "21000.00")
            .param("minPopulation", "2000000"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content.length()").value(2)) // Should find Gandaki and Lumbini
    }
    
    @Test
    @WithUserDetails("admin@system.com")
    fun `should get all provinces`() {
        // Create multiple provinces in the database
        val provinces = (1..3).map { i ->
            ProvinceTestFixtures.createProvince(
                code = "TEST-P${i+10}",
                name = "Province $i",
                nameNepali = "प्रदेश $i"
            )
        }
        provinceRepository.saveAll(provinces)
        
        // Get all provinces
        mockMvc.perform(get("/api/v1/provinces"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.length()").value(provinces.size))
    }
}
