package np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender

import jakarta.transaction.Transactional
import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.model.WardWiseHouseHeadGender
import np.sthaniya.dpis.profile.demographics.wardWiseHouseheadGender.repository.WardWiseHouseHeadGenderRepository
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
class WardWiseHouseHeadGenderControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardWiseHouseHeadGenderRepository: WardWiseHouseHeadGenderRepository

    private lateinit var testEntries: List<WardWiseHouseHeadGender>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardWiseHouseHeadGenderRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardWiseHouseHeadGenderRepository.deleteAll()
    }

    @Test
    fun `should get ward-wise-househead-gender data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-househead-gender/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.gender").value(Gender.MALE.name))
            .andExpect(jsonPath("$.data.population").value(180))
            .andDo(
                document(
                    "ward-wise-househead-gender-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-wise househead gender data entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-wise househead gender data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data.gender").description("Gender category"),
                        fieldWithPath("data.population").description("Population count"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-wise-househead-gender data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-househead-gender")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(9)) // Total entries in our test data
            .andDo(
                document(
                    "ward-wise-househead-gender-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise househead gender data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-househead-gender data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-househead-gender/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for ward 1
            .andDo(
                document(
                    "ward-wise-househead-gender-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise househead gender data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-househead-gender data by gender`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-househead-gender/by-gender/{gender}", Gender.FEMALE)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for FEMALE gender
            .andDo(
                document(
                    "ward-wise-househead-gender-get-by-gender",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("gender").description("Gender to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise househead gender data for the specified gender"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get gender population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-househead-gender/summary/by-gender")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 gender types in test data
            .andDo(
                document(
                    "ward-wise-househead-gender-summary-by-gender",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of gender population summaries"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this gender")
                    )
                )
            )
    }

    @Test
    fun `should get ward population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-househead-gender/summary/by-ward")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 wards in test data
            .andDo(
                document(
                    "ward-wise-househead-gender-summary-by-ward",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward population summaries"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this ward")
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): List<WardWiseHouseHeadGender> {
        val entries = mutableListOf<WardWiseHouseHeadGender>()

        // Create entries for Ward 1
        entries.add(createEntry(1, "Ward One", Gender.MALE, 180))
        entries.add(createEntry(1, "Ward One", Gender.FEMALE, 70))
        entries.add(createEntry(1, "Ward One", Gender.OTHER, 5))

        // Create entries for Ward 2
        entries.add(createEntry(2, "Ward Two", Gender.MALE, 150))
        entries.add(createEntry(2, "Ward Two", Gender.FEMALE, 80))
        entries.add(createEntry(2, "Ward Two", Gender.OTHER, 3))

        // Create entries for Ward 3
        entries.add(createEntry(3, "Ward Three", Gender.MALE, 120))
        entries.add(createEntry(3, "Ward Three", Gender.FEMALE, 65))
        entries.add(createEntry(3, "Ward Three", Gender.OTHER, 2))

        return wardWiseHouseHeadGenderRepository.saveAll(entries)
    }

    private fun createEntry(wardNum: Int, wardName: String?, gender: Gender, pop: Int): WardWiseHouseHeadGender {
        val entry = WardWiseHouseHeadGender()
        entry.wardNumber = wardNum
        entry.wardName = wardName
        entry.gender = gender
        entry.population = pop
        return entry
    }
}
