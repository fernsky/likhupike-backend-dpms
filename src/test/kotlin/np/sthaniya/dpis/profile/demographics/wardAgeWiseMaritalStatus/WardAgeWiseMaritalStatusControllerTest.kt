package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus

import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.MaritalStatusGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model.WardWiseMaritalStatus
import np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.repository.WardAgeWiseMaritalStatusRepository
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
import org.hamcrest.Matchers.greaterThanOrEqualTo
import jakarta.transaction.Transactional
import java.util.*

@Transactional
class WardAgeWiseMaritalStatusControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardAgeWiseMaritalStatusRepository: WardAgeWiseMaritalStatusRepository

    private lateinit var testEntries: List<WardWiseMaritalStatus>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardAgeWiseMaritalStatusRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardAgeWiseMaritalStatusRepository.deleteAll()
    }

    @Test
    fun `should get ward-age-wise marital status data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-marital-status/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.ageGroup").value(MaritalAgeGroup.AGE_BELOW_15.name))
            .andExpect(jsonPath("$.data.maritalStatus").value(MaritalStatusGroup.SINGLE.name))
            .andExpect(jsonPath("$.data.population").value(25))
            .andDo(
                document(
                    "ward-age-wise-marital-status-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-age-wise marital status data entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-age-wise marital status data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.ageGroup").description("Age group category"),
                        fieldWithPath("data.maritalStatus").description("Marital status category"),
                        fieldWithPath("data.population").description("Population count"),
                        fieldWithPath("data.malePopulation").description("Male population count").optional(),
                        fieldWithPath("data.femalePopulation").description("Female population count").optional(),
                        fieldWithPath("data.otherPopulation").description("Other gender population count").optional(),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-age-wise marital status data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-marital-status")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(7)) // Total entries in our test data
            .andDo(
                document(
                    "ward-age-wise-marital-status-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-wise marital status data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].maritalStatus").description("Marital status category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].malePopulation").description("Male population count").optional(),
                        fieldWithPath("data[].femalePopulation").description("Female population count").optional(),
                        fieldWithPath("data[].otherPopulation").description("Other gender population count").optional(),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-wise marital status data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-marital-status/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(4)) // We have 4 entries for ward 1
            .andDo(
                document(
                    "ward-age-wise-marital-status-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-wise marital status data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].maritalStatus").description("Marital status category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].malePopulation").description("Male population count").optional(),
                        fieldWithPath("data[].femalePopulation").description("Female population count").optional(),
                        fieldWithPath("data[].otherPopulation").description("Other gender population count").optional(),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-wise marital status data by age group`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-marital-status/by-age-group/{ageGroup}", MaritalAgeGroup.AGE_BELOW_15)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(1)) // We have 1 entry for AGE_BELOW_15
            .andDo(
                document(
                    "ward-age-wise-marital-status-get-by-age-group",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("ageGroup").description("Age group to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-wise marital status data for the specified age group"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].maritalStatus").description("Marital status category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].malePopulation").description("Male population count").optional(),
                        fieldWithPath("data[].femalePopulation").description("Female population count").optional(),
                        fieldWithPath("data[].otherPopulation").description("Other gender population count").optional(),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-wise marital status data by marital status`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-marital-status/by-marital-status/{maritalStatus}", MaritalStatusGroup.MARRIED)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for MARRIED
            .andDo(
                document(
                    "ward-age-wise-marital-status-get-by-marital-status",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("maritalStatus").description("Marital status to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-wise marital status data for the specified marital status"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].maritalStatus").description("Marital status category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].malePopulation").description("Male population count").optional(),
                        fieldWithPath("data[].femalePopulation").description("Female population count").optional(),
                        fieldWithPath("data[].otherPopulation").description("Other gender population count").optional(),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get marital status population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-marital-status/summary/by-marital-status")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(3))) // We should have at least 3 summary entries
            .andDo(
                document(
                    "ward-age-wise-marital-status-summary-by-marital-status",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of marital status population summaries"),
                        fieldWithPath("data[].maritalStatus").description("Marital status category"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this marital status"),
                        fieldWithPath("data[].malePopulation").description("Total male population count for this marital status").optional(),
                        fieldWithPath("data[].femalePopulation").description("Total female population count for this marital status").optional(),
                        fieldWithPath("data[].otherPopulation").description("Total other gender population count for this marital status").optional()
                    )
                )
            )
    }

    @Test
    fun `should get age group population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-marital-status/summary/by-age-group")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(4))) // We should have at least 4 summary entries
            .andDo(
                document(
                    "ward-age-wise-marital-status-summary-by-age-group",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of age group population summaries"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this age group"),
                        fieldWithPath("data[].malePopulation").description("Total male population count for this age group").optional(),
                        fieldWithPath("data[].femalePopulation").description("Total female population count for this age group").optional(),
                        fieldWithPath("data[].otherPopulation").description("Total other gender population count for this age group").optional()
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): List<WardWiseMaritalStatus> {
        val entries = mutableListOf<WardWiseMaritalStatus>()

        // Create entries for Ward 1
        entries.add(createEntry(1, MaritalAgeGroup.AGE_BELOW_15, MaritalStatusGroup.SINGLE, 25, 15, 10, 0))
        entries.add(createEntry(1, MaritalAgeGroup.AGE_15_19, MaritalStatusGroup.MARRIED, 45, 20, 25, 0))
        entries.add(createEntry(1, MaritalAgeGroup.AGE_20_24, MaritalStatusGroup.MARRIED, 85, 40, 45, 0))
        entries.add(createEntry(1, MaritalAgeGroup.AGE_25_29, MaritalStatusGroup.DIVORCED, 15, 5, 10, 0))

        // Create entries for Ward 2
        entries.add(createEntry(2, MaritalAgeGroup.AGE_15_19, MaritalStatusGroup.SINGLE, 60, 35, 25, 0))
        entries.add(createEntry(2, MaritalAgeGroup.AGE_20_24, MaritalStatusGroup.MARRIED, 110, 50, 60, 0))
        entries.add(createEntry(2, MaritalAgeGroup.AGE_40_44, MaritalStatusGroup.WIDOWED, 30, 10, 20, 0))

        return wardAgeWiseMaritalStatusRepository.saveAll(entries)
    }

    private fun createEntry(
        wardNum: Int,
        age: MaritalAgeGroup,
        status: MaritalStatusGroup,
        pop: Int,
        malePop: Int? = null,
        femalePop: Int? = null,
        otherPop: Int? = null
    ): WardWiseMaritalStatus {
        val entry = WardWiseMaritalStatus()
        entry.wardNumber = wardNum
        entry.ageGroup = age
        entry.maritalStatus = status
        entry.population = pop
        entry.malePopulation = malePop
        entry.femalePopulation = femalePop
        entry.otherPopulation = otherPop
        return entry
    }
}
