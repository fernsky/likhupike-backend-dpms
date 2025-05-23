package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee

import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.AbsenteeAgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model.WardAgeGenderWiseAbsentee
import np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.repository.WardAgeGenderWiseAbsenteeRepository
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
class WardAgeGenderWiseAbsenteeControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardAgeGenderWiseAbsenteeRepository: WardAgeGenderWiseAbsenteeRepository

    private lateinit var testEntries: List<WardAgeGenderWiseAbsentee>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardAgeGenderWiseAbsenteeRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardAgeGenderWiseAbsenteeRepository.deleteAll()
    }

    @Test
    fun `should get ward-age-gender-wise absentee data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-absentee/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.ageGroup").value(AbsenteeAgeGroup.AGE_0_4.name))
            .andExpect(jsonPath("$.data.gender").value(Gender.MALE.name))
            .andExpect(jsonPath("$.data.population").value(75))
            .andDo(
                document(
                    "ward-age-gender-wise-absentee-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-age-gender-wise absentee data entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-age-gender-wise absentee data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.ageGroup").description("Age group category"),
                        fieldWithPath("data.gender").description("Gender category"),
                        fieldWithPath("data.population").description("Absentee population count"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-age-gender-wise absentee data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-absentee")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(7)) // Total entries in our test data
            .andDo(
                document(
                    "ward-age-gender-wise-absentee-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-gender-wise absentee data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Absentee population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-gender-wise absentee data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(4)) // We have 4 entries for ward 1
            .andDo(
                document(
                    "ward-age-gender-wise-absentee-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-gender-wise absentee data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Absentee population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-gender-wise absentee data by age group`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-age-group/{ageGroup}", AbsenteeAgeGroup.AGE_0_4)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(1)) // We have 1 entries for AGE_0_4
            .andDo(
                document(
                    "ward-age-gender-wise-absentee-get-by-age-group",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("ageGroup").description("Age group to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-gender-wise absentee data for the specified age group"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Absentee population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-gender-wise absentee data by gender`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-gender/{gender}", Gender.MALE)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for MALE
            .andDo(
                document(
                    "ward-age-gender-wise-absentee-get-by-gender",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("gender").description("Gender to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-gender-wise absentee data for the specified gender"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Absentee population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get age group gender population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-absentee/summary/by-age-gender")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(5))) // We should have at least 5 summary entries
            .andDo(
                document(
                    "ward-age-gender-wise-absentee-summary-by-age-gender",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of age group and gender population summaries"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].totalPopulation").description("Total absentee population count for this age group and gender")
                    )
                )
            )
    }

    @Test
    fun `should get ward absentee population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-gender-wise-absentee/summary/by-ward")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(2)) // We have 2 wards in our test data
            .andDo(
                document(
                    "ward-age-gender-wise-absentee-summary-by-ward",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward absentee population summaries"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].totalPopulation").description("Total absentee population count for this ward")
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): List<WardAgeGenderWiseAbsentee> {
        val entries = mutableListOf<WardAgeGenderWiseAbsentee>()

        // Create entries for Ward 1
        entries.add(createEntry(1, AbsenteeAgeGroup.AGE_0_4, Gender.MALE, 75))
        entries.add(createEntry(1, AbsenteeAgeGroup.AGE_5_9, Gender.FEMALE, 65))
        entries.add(createEntry(1, AbsenteeAgeGroup.AGE_15_19, Gender.MALE, 120))
        entries.add(createEntry(1, AbsenteeAgeGroup.AGE_25_29, Gender.FEMALE, 90))

        // Create entries for Ward 2
        entries.add(createEntry(2, AbsenteeAgeGroup.AGE_15_19, Gender.FEMALE, 85))
        entries.add(createEntry(2, AbsenteeAgeGroup.AGE_25_29, Gender.MALE, 110))
        entries.add(createEntry(2, AbsenteeAgeGroup.AGE_50_AND_ABOVE, Gender.FEMALE, 45))

        return wardAgeGenderWiseAbsenteeRepository.saveAll(entries)
    }

    private fun createEntry(wardNum: Int, age: AbsenteeAgeGroup, gen: Gender, pop: Int): WardAgeGenderWiseAbsentee {
        val entry = WardAgeGenderWiseAbsentee()
        entry.wardNumber = wardNum
        entry.ageGroup = age
        entry.gender = gen
        entry.population = pop
        return entry
    }
}
