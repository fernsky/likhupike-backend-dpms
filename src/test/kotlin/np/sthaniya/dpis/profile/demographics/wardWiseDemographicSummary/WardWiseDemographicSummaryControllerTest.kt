package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary

import jakarta.transaction.Transactional
import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.model.WardWiseDemographicSummary
import np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.repository.WardWiseDemographicSummaryRepository
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
import java.math.BigDecimal
import java.util.*

@Transactional
class WardWiseDemographicSummaryControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardWiseDemographicSummaryRepository: WardWiseDemographicSummaryRepository

    private lateinit var testEntries: List<WardWiseDemographicSummary>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardWiseDemographicSummaryRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardWiseDemographicSummaryRepository.deleteAll()
    }

    @Test
    fun `should get ward-wise-demographic-summary data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-demographic-summary/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.totalPopulation").value(1000))
            .andExpect(jsonPath("$.data.populationMale").value(510))
            .andExpect(jsonPath("$.data.populationFemale").value(480))
            .andDo(
                document(
                    "ward-wise-demographic-summary-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-wise demographic summary entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-wise demographic summary data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.wardName").description("Ward name (optional)"),
                        fieldWithPath("data.totalPopulation").description("Total population in the ward"),
                        fieldWithPath("data.populationMale").description("Male population in the ward"),
                        fieldWithPath("data.populationFemale").description("Female population in the ward"),
                        fieldWithPath("data.populationOther").description("Other gender population in the ward"),
                        fieldWithPath("data.totalHouseholds").description("Total number of households in the ward"),
                        fieldWithPath("data.averageHouseholdSize").description("Average household size in the ward"),
                        fieldWithPath("data.sexRatio").description("Sex ratio (number of males per 100 females) in the ward"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-wise-demographic-summary data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-demographic-summary")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // Total entries in our test data
            .andDo(
                document(
                    "ward-wise-demographic-summary-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise demographic summary data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (optional)"),
                        fieldWithPath("data[].totalPopulation").description("Total population in the ward"),
                        fieldWithPath("data[].populationMale").description("Male population in the ward"),
                        fieldWithPath("data[].populationFemale").description("Female population in the ward"),
                        fieldWithPath("data[].populationOther").description("Other gender population in the ward"),
                        fieldWithPath("data[].totalHouseholds").description("Total number of households in the ward"),
                        fieldWithPath("data[].averageHouseholdSize").description("Average household size in the ward"),
                        fieldWithPath("data[].sexRatio").description("Sex ratio (number of males per 100 females) in the ward"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-demographic-summary data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-demographic-summary/by-ward/{wardNumber}", 2)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.wardNumber").value(2))
            .andExpect(jsonPath("$.data.totalPopulation").value(1200))
            .andDo(
                document(
                    "ward-wise-demographic-summary-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-wise demographic summary data for the specified ward"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.wardName").description("Ward name (optional)"),
                        fieldWithPath("data.totalPopulation").description("Total population in the ward"),
                        fieldWithPath("data.populationMale").description("Male population in the ward"),
                        fieldWithPath("data.populationFemale").description("Female population in the ward"),
                        fieldWithPath("data.populationOther").description("Other gender population in the ward"),
                        fieldWithPath("data.totalHouseholds").description("Total number of households in the ward"),
                        fieldWithPath("data.averageHouseholdSize").description("Average household size in the ward"),
                        fieldWithPath("data.sexRatio").description("Sex ratio (number of males per 100 females) in the ward"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): List<WardWiseDemographicSummary> {
        val entries = mutableListOf<WardWiseDemographicSummary>()

        // Create entries for different wards
        entries.add(createEntry(1, "Ward One", 1000, 510, 480, 10, 250, BigDecimal("4.0"), BigDecimal("106.25")))
        entries.add(createEntry(2, "Ward Two", 1200, 590, 600, 10, 300, BigDecimal("4.0"), BigDecimal("98.33")))
        entries.add(createEntry(3, "Ward Three", 900, 440, 450, 10, 220, BigDecimal("4.09"), BigDecimal("97.78")))

        return wardWiseDemographicSummaryRepository.saveAll(entries)
    }

    private fun createEntry(
        wardNum: Int,
        wardName: String,
        totalPop: Int,
        malePop: Int,
        femalePop: Int,
        otherPop: Int,
        totalHouseholds: Int,
        avgHouseholdSize: BigDecimal,
        sexRatio: BigDecimal
    ): WardWiseDemographicSummary {
        val entry = WardWiseDemographicSummary()
        entry.wardNumber = wardNum
        entry.wardName = wardName
        entry.totalPopulation = totalPop
        entry.populationMale = malePop
        entry.populationFemale = femalePop
        entry.populationOther = otherPop
        entry.totalHouseholds = totalHouseholds
        entry.averageHouseholdSize = avgHouseholdSize
        entry.sexRatio = sexRatio
        return entry
    }
}
