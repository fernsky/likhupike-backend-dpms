package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation

import jakarta.transaction.Transactional
import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.LanguageType
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model.WardWiseMotherTonguePopulation
import np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.repository.WardWiseMotherTonguePopulationRepository
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
class WardWiseMotherTonguePopulationControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardWiseMotherTonguePopulationRepository: WardWiseMotherTonguePopulationRepository

    private lateinit var testEntries: List<WardWiseMotherTonguePopulation>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardWiseMotherTonguePopulationRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardWiseMotherTonguePopulationRepository.deleteAll()
    }

    @Test
    fun `should get ward-wise-mother-tongue-population data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-mother-tongue-population/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.languageType").value(LanguageType.NEPALI.name))
            .andExpect(jsonPath("$.data.population").value(500))
            .andDo(
                document(
                    "ward-wise-mother-tongue-population-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-wise mother tongue population entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-wise mother tongue population data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.languageType").description("Language type"),
                        fieldWithPath("data.population").description("Population count"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-wise-mother-tongue-population data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-mother-tongue-population")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(9)) // Total entries in our test data
            .andDo(
                document(
                    "ward-wise-mother-tongue-population-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise mother tongue population data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].languageType").description("Language type"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-mother-tongue-population data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-mother-tongue-population/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for ward 1
            .andDo(
                document(
                    "ward-wise-mother-tongue-population-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise mother tongue population data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].languageType").description("Language type"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-mother-tongue-population data by language`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-mother-tongue-population/by-language/{languageType}", LanguageType.NEPALI)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for NEPALI language
            .andDo(
                document(
                    "ward-wise-mother-tongue-population-get-by-language",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("languageType").description("Language type to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise mother tongue population data for the specified language"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].languageType").description("Language type"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get language population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-mother-tongue-population/summary/by-language")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 language types in test data
            .andDo(
                document(
                    "ward-wise-mother-tongue-population-summary-by-language",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of language population summaries"),
                        fieldWithPath("data[].languageType").description("Language type"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this language")
                    )
                )
            )
    }

    @Test
    fun `should get ward population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-mother-tongue-population/summary/by-ward")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 wards in test data
            .andDo(
                document(
                    "ward-wise-mother-tongue-population-summary-by-ward",
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
    private fun createTestData(): List<WardWiseMotherTonguePopulation> {
        val entries = mutableListOf<WardWiseMotherTonguePopulation>()

        // Create entries for Ward 1
        entries.add(createEntry(1, LanguageType.NEPALI, 500))
        entries.add(createEntry(1, LanguageType.MAITHILI, 300))
        entries.add(createEntry(1, LanguageType.TAMANG, 200))

        // Create entries for Ward 2
        entries.add(createEntry(2, LanguageType.NEPALI, 600))
        entries.add(createEntry(2, LanguageType.MAITHILI, 250))
        entries.add(createEntry(2, LanguageType.TAMANG, 150))

        // Create entries for Ward 3
        entries.add(createEntry(3, LanguageType.NEPALI, 450))
        entries.add(createEntry(3, LanguageType.MAITHILI, 200))
        entries.add(createEntry(3, LanguageType.TAMANG, 100))

        return wardWiseMotherTonguePopulationRepository.saveAll(entries)
    }

    private fun createEntry(wardNum: Int, languageType: LanguageType, pop: Int): WardWiseMotherTonguePopulation {
        val entry = WardWiseMotherTonguePopulation()
        entry.wardNumber = wardNum
        entry.languageType = languageType
        entry.population = pop
        return entry
    }
}
