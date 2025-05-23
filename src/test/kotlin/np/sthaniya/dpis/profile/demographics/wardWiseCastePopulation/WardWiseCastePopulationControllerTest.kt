package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation

import jakarta.transaction.Transactional
import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.CasteType
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model.WardWiseCastePopulation
import np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.repository.WardWiseCastePopulationRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@Transactional
class WardWiseCastePopulationControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardWiseCastePopulationRepository: WardWiseCastePopulationRepository

    private lateinit var testEntries: List<WardWiseCastePopulation>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardWiseCastePopulationRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardWiseCastePopulationRepository.deleteAll()
    }

    @Test
    fun `should get ward-wise-caste-population data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-caste-population/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.casteType").value(CasteType.BRAHMAN.name))
            .andExpect(jsonPath("$.data.population").value(500))
            .andDo(
                document(
                    "ward-wise-caste-population-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-wise caste population data entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-wise caste population data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.casteType").description("Caste type category"),
                        fieldWithPath("data.population").description("Population count"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-wise-caste-population data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-caste-population")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(9)) // Total entries in our test data
            .andDo(
                document(
                    "ward-wise-caste-population-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise caste population data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].casteType").description("Caste type category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-caste-population data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-caste-population/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for ward 1
            .andDo(
                document(
                    "ward-wise-caste-population-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise caste population data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].casteType").description("Caste type category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-caste-population data by caste`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-caste-population/by-caste/{casteType}", CasteType.BRAHMAN)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for BRAHMAN caste
            .andDo(
                document(
                    "ward-wise-caste-population-get-by-caste",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("casteType").description("Caste type to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise caste population data for the specified caste type"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].casteType").description("Caste type category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get caste population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-caste-population/summary/by-caste")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 caste types in test data
            .andDo(
                document(
                    "ward-wise-caste-population-summary-by-caste",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of caste population summaries"),
                        fieldWithPath("data[].casteType").description("Caste type category"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this caste type")
                    )
                )
            )
    }

    @Test
    fun `should get ward population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-caste-population/summary/by-ward")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 wards in test data
            .andDo(
                document(
                    "ward-wise-caste-population-summary-by-ward",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward population summaries"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this ward")
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): List<WardWiseCastePopulation> {
        val entries = mutableListOf<WardWiseCastePopulation>()

        // Create entries for Ward 1
        entries.add(createEntry(1, CasteType.BRAHMAN, 500))
        entries.add(createEntry(1, CasteType.CHHETRI, 600))
        entries.add(createEntry(1, CasteType.THAKURI, 400))

        // Create entries for Ward 2
        entries.add(createEntry(2, CasteType.BRAHMAN, 450))
        entries.add(createEntry(2, CasteType.CHHETRI, 550))
        entries.add(createEntry(2, CasteType.THAKURI, 350))

        // Create entries for Ward 3
        entries.add(createEntry(3, CasteType.BRAHMAN, 400))
        entries.add(createEntry(3, CasteType.CHHETRI, 500))
        entries.add(createEntry(3, CasteType.THAKURI, 300))

        return wardWiseCastePopulationRepository.saveAll(entries)
    }

    private fun createEntry(wardNum: Int, casteType: CasteType, pop: Int): WardWiseCastePopulation {
        val entry = WardWiseCastePopulation()
        entry.wardNumber = wardNum
        entry.casteType = casteType
        entry.population = pop
        return entry
    }
}
