package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge

import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.MarriedAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model.WardAgeGenderWiseMarriedAge
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.repository.WardAgeGenderWiseMarriedAgeRepository
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
class WardAgeGenderWiseMarriedAgeControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardAgeGenderWiseMarriedAgeRepository: WardAgeGenderWiseMarriedAgeRepository

    private lateinit var testEntries: List<WardAgeGenderWiseMarriedAge>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardAgeGenderWiseMarriedAgeRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardAgeGenderWiseMarriedAgeRepository.deleteAll()
    }

    @Test
    fun `should get ward-age-gender-wise married age data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-married-age/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.ageGroup").value(MarriedAgeGroup.AGE_BELOW_15.name))
            .andExpect(jsonPath("$.data.gender").value(Gender.MALE.name))
            .andExpect(jsonPath("$.data.population").value(15))
            .andDo(
                document(
                    "ward-age-gender-wise-married-age-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-age-gender-wise married age data entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-age-gender-wise married age data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.ageGroup").description("Age group category"),
                        fieldWithPath("data.gender").description("Gender category"),
                        fieldWithPath("data.population").description("Married population count"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-age-gender-wise married age data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-married-age")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(7)) // Total entries in our test data
            .andDo(
                document(
                    "ward-age-gender-wise-married-age-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-gender-wise married age data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Married population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-gender-wise married age data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(4)) // We have 4 entries for ward 1
            .andDo(
                document(
                    "ward-age-gender-wise-married-age-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-gender-wise married age data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Married population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-gender-wise married age data by age group`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-age-group/{ageGroup}", MarriedAgeGroup.AGE_BELOW_15)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(1)) // We have 1 entry for AGE_BELOW_15
            .andDo(
                document(
                    "ward-age-gender-wise-married-age-get-by-age-group",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("ageGroup").description("Age group to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-gender-wise married age data for the specified age group"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Married population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-gender-wise married age data by gender`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-gender/{gender}", Gender.MALE)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for MALE
            .andDo(
                document(
                    "ward-age-gender-wise-married-age-get-by-gender",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("gender").description("Gender to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-gender-wise married age data for the specified gender"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Married population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get age group gender population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-married-age/summary/by-age-group-gender")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(5))) // We should have at least 5 summary entries
            .andDo(
                document(
                    "ward-age-gender-wise-married-age-summary-by-age-gender",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of age group and gender population summaries"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].totalPopulation").description("Total married population count for this age group and gender")
                    )
                )
            )
    }

    @Test
    fun `should get ward married population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-married-age/summary/by-ward")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(2)) // We have 2 wards in our test data
            .andDo(
                document(
                    "ward-age-gender-wise-married-age-summary-by-ward",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward married population summaries"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].totalPopulation").description("Total married population count for this ward")
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): List<WardAgeGenderWiseMarriedAge> {
        val entries = mutableListOf<WardAgeGenderWiseMarriedAge>()

        // Create entries for Ward 1
        entries.add(createEntry(1, MarriedAgeGroup.AGE_BELOW_15, Gender.MALE, 15))
        entries.add(createEntry(1, MarriedAgeGroup.AGE_15_19, Gender.FEMALE, 45))
        entries.add(createEntry(1, MarriedAgeGroup.AGE_20_24, Gender.MALE, 85))
        entries.add(createEntry(1, MarriedAgeGroup.AGE_25_29, Gender.FEMALE, 120))

        // Create entries for Ward 2
        entries.add(createEntry(2, MarriedAgeGroup.AGE_15_19, Gender.FEMALE, 65))
        entries.add(createEntry(2, MarriedAgeGroup.AGE_20_24, Gender.MALE, 75))
        entries.add(createEntry(2, MarriedAgeGroup.AGE_40_AND_ABOVE, Gender.FEMALE, 40))

        return wardAgeGenderWiseMarriedAgeRepository.saveAll(entries)
    }

    private fun createEntry(wardNum: Int, age: MarriedAgeGroup, gen: Gender, pop: Int): WardAgeGenderWiseMarriedAge {
        val entry = WardAgeGenderWiseMarriedAge()
        entry.wardNumber = wardNum
        entry.ageGroup = age
        entry.gender = gen
        entry.population = pop
        return entry
    }
}
