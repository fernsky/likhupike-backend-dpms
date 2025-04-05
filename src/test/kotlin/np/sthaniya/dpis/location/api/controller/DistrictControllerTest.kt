package np.sthaniya.dpis.location.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.test.UserTestDataFactory
import np.sthaniya.dpis.common.BaseIntegrationTest
import np.sthaniya.dpis.location.api.dto.request.CreateDistrictRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateDistrictRequest
import np.sthaniya.dpis.location.domain.Province
import np.sthaniya.dpis.location.repository.DistrictRepository
import np.sthaniya.dpis.location.repository.ProvinceRepository
import np.sthaniya.dpis.location.test.fixtures.DistrictTestFixtures
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

class DistrictControllerIntegrationTest : BaseIntegrationTest() {
    
    @Autowired
    private lateinit var provinceRepository: ProvinceRepository
    
    @Autowired
    private lateinit var districtRepository: DistrictRepository
    
    private lateinit var testProvince: Province
    private lateinit var testAdmin: User

    private lateinit var createDistrictRequest: CreateDistrictRequest
    private lateinit var updateDistrictRequest: UpdateDistrictRequest
    
    @BeforeEach
    override fun setUp() {
        // Call the parent setUp() to initialize MockMvc, etc.
        super.setUp()
        
        // Delete all existing districts and provinces to start fresh
        districtRepository.deleteAll()
        provinceRepository.deleteAll()
        
        // Create test province and save it to the database
        testProvince = ProvinceTestFixtures.createProvince(
            code = "TEST-P1",
            name = "Test Province",
            nameNepali = "परीक्षण प्रदेश"
        )
        provinceRepository.save(testProvince)
        
        // Create test admin user
        testAdmin = UserTestDataFactory.createSystemAdministrator()
        
        // Create request objects for API calls
        createDistrictRequest = DistrictTestFixtures.createDistrictRequest(
            code = "TEST-D1",
            name = "Test District",
            nameNepali = "परीक्षण जिल्ला",
            provinceCode = testProvince.code!!,
            area = BigDecimal("1000.50"),
            population = 100000L,
            headquarter = "Test Headquarter",
            headquarterNepali = "परीक्षण सदरमुकाम"
        )
        
        updateDistrictRequest = DistrictTestFixtures.createUpdateDistrictRequest(
            name = "Updated District",
            nameNepali = "अद्यावधिक जिल्ला",
            area = BigDecimal("1100.50"),
            population = 110000L,
            headquarter = "Updated Headquarter",
            headquarterNepali = "अद्यावधिक सदरमुकाम"
        )
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should create district`() {
        // Create a district using the API
        mockMvc.perform(post("/api/v1/districts")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDistrictRequest)))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(createDistrictRequest.code))
            .andExpect(jsonPath("$.data.name").value(createDistrictRequest.name))
            .andExpect(jsonPath("$.message").value("District created successfully"))
            
        // Verify district was actually saved to the database
        val savedDistrict = districtRepository.findByCode(createDistrictRequest.code)
        assert(savedDistrict.isPresent)
        assert(savedDistrict.get().name == createDistrictRequest.name)
        assert(savedDistrict.get().province?.code == testProvince.code)
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should get district by code`() {
        // First create a district in the database
        val district = DistrictTestFixtures.createDistrict(
            code = "TEST-D2",
            name = "Test District 2",
            nameNepali = "परीक्षण जिल्ला २",
            province = testProvince
        )
        districtRepository.save(district)
        
        // Get the district using the API
        mockMvc.perform(get("/api/v1/districts/${district.code}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(district.code))
            .andExpect(jsonPath("$.data.name").value(district.name))
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should update district`() {
        // First create a district in the database
        val district = DistrictTestFixtures.createDistrict(
            code = "TEST-D3",
            name = "Test District 3",
            nameNepali = "परीक्षण जिल्ला ३",
            province = testProvince
        )
        districtRepository.save(district)
        
        // Update the district using the API
        mockMvc.perform(put("/api/v1/districts/${district.code}")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDistrictRequest)))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value(district.code))
            .andExpect(jsonPath("$.data.name").value(updateDistrictRequest.name))
            .andExpect(jsonPath("$.message").value("District updated successfully"))
            
        // Verify district was actually updated in the database
        val updatedDistrict = districtRepository.findByCode(district.code!!)
        assert(updatedDistrict.isPresent)
        assert(updatedDistrict.get().name == updateDistrictRequest.name)
        assert(updatedDistrict.get().nameNepali == updateDistrictRequest.nameNepali)
        assert(updatedDistrict.get().area == updateDistrictRequest.area)
        assert(updatedDistrict.get().population == updateDistrictRequest.population)
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should search districts`() {
        // Create multiple districts in the database
        val district1 = DistrictTestFixtures.createDistrict(
            code = "TEST-D4",
            name = "Kathmandu",
            nameNepali = "काठमाडौं",
            province = testProvince,
            population = 2000000L
        )
        val district2 = DistrictTestFixtures.createDistrict(
            code = "TEST-D5",
            name = "Bhaktapur",
            nameNepali = "भक्तपुर",
            province = testProvince,
            population = 500000L
        )
        val district3 = DistrictTestFixtures.createDistrict(
            code = "TEST-D6",
            name = "Lalitpur",
            nameNepali = "ललितपुर",
            province = testProvince,
            population = 800000L
        )
        districtRepository.saveAll(listOf(district1, district2, district3))
        
        // Search for districts with 'pur' in the name
        mockMvc.perform(get("/api/v1/districts/search")
            .param("name", "pur"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(3)) // Should find Bhaktapur and Lalitpur
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Found")))
            
        // Search by minimum population
        mockMvc.perform(get("/api/v1/districts/search")
            .param("minPopulation", "1000000"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.meta.totalElements").value(3)) // Should find only Kathmandu
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should get districts by province code`() {
        // Create multiple districts in the database for the same province
        val district1 = DistrictTestFixtures.createDistrict(
            code = "TEST-D7",
            name = "District 7",
            province = testProvince
        )
        val district2 = DistrictTestFixtures.createDistrict(
            code = "TEST-D8",
            name = "District 8",
            province = testProvince
        )
        districtRepository.saveAll(listOf(district1, district2))
        
        // Create another province and districts in that province
        val anotherProvince = ProvinceTestFixtures.createProvince(code = "TEST-P2")
        provinceRepository.save(anotherProvince)
        
        val district3 = DistrictTestFixtures.createDistrict(
            code = "TEST-D9",
            name = "District 9",
            province = anotherProvince
        )
        districtRepository.save(district3)
        
        // Get districts for the test province
        mockMvc.perform(get("/api/v1/districts/by-province/${testProvince.code}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.length()").value(2)) // Should find 2 districts for the test province
    }
    
    @Test
    @WithMockUser(roles = ["SYSTEM_ADMINISTRATOR"])
    fun `should get all districts`() {
        // Create multiple districts in the database
        val districts = (1..5).map { i ->
            DistrictTestFixtures.createDistrict(
                code = "TEST-D1$i",
                name = "District $i",
                province = testProvince
            )
        }
        districtRepository.saveAll(districts)
        
        // Get all districts
        mockMvc.perform(get("/api/v1/districts"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.length()").value(districts.size))
    }
}
