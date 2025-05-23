package np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation

import jakarta.transaction.Transactional
import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.ReligionType
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model.WardWiseReligionPopulation
import np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.repository.WardWiseReligionPopulationRepository
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
class WardWiseReligionPopulationControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardWiseReligionPopulationRepository: WardWiseReligionPopulationRepository

    private lateinit var testEntries: List<WardWiseReligionPopulation>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardWiseReligionPopulationRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardWiseReligionPopulationRepository.deleteAll()
    }

    @Test
    fun `should get ward-wise-religion-population data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-religion-population/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.religionType").value(ReligionType.HINDU.name))
            .andExpect(jsonPath("$.data.population").value(500))
            .andDo(
                document(
                    "ward-wise-religion-population-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-wise religion population entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-wise religion population data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.religionType").description("Religion type"),
                        fieldWithPath("data.population").description("Population count"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-wise-religion-population data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-religion-population")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(9)) // Total entries in our test data
            .andDo(
                document(
                    "ward-wise-religion-population-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise religion population data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].religionType").description("Religion type"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-religion-population data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-religion-population/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for ward 1
            .andDo(
                document(
                    "ward-wise-religion-population-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise religion population data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].religionType").description("Religion type"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-religion-population data by religion`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-religion-population/by-religion/{religionType}", ReligionType.HINDU)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for HINDU religion
            .andDo(
                document(
                    "ward-wise-religion-population-get-by-religion",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("religionType").description("Religion type to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise religion population data for the specified religion"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].religionType").description("Religion type"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get religion population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-religion-population/summary/by-religion")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 religion types in test data
            .andDo(
                document(
                    "ward-wise-religion-population-summary-by-religion",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of religion population summaries"),
                        fieldWithPath("data[].religionType").description("Religion type"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this religion")
                    )
                )
            )
    }

    @Test
    fun `should get ward population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-religion-population/summary/by-ward")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 wards in test data
            .andDo(
                document(
                    "ward-wise-religion-population-summary-by-ward",
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
    private fun createTestData(): List<WardWiseReligionPopulation> {
        val entries = mutableListOf<WardWiseReligionPopulation>()

        // Create entries for Ward 1
        entries.add(createEntry(1, ReligionType.HINDU, 500))
        entries.add(createEntry(1, ReligionType.BUDDHIST, 300))
        entries.add(createEntry(1, ReligionType.CHRISTIAN, 100))

        // Create entries for Ward 2
        entries.add(createEntry(2, ReligionType.HINDU, 600))
        entries.add(createEntry(2, ReligionType.BUDDHIST, 250))
        entries.add(createEntry(2, ReligionType.CHRISTIAN, 150))

        // Create entries for Ward 3
        entries.add(createEntry(3, ReligionType.HINDU, 450))
        entries.add(createEntry(3, ReligionType.BUDDHIST, 200))
        entries.add(createEntry(3, ReligionType.CHRISTIAN, 120))

        return wardWiseReligionPopulationRepository.saveAll(entries)
    }

    private fun createEntry(wardNum: Int, religionType: ReligionType, pop: Int): WardWiseReligionPopulation {
        val entry = WardWiseReligionPopulation()
        entry.wardNumber = wardNum
        entry.religionType = religionType
        entry.population = pop
        return entry
    }
}
