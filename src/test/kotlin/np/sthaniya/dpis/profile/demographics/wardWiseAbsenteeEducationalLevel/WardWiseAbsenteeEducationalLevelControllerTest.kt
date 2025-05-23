package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel

import jakarta.transaction.Transactional
import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.model.WardWiseAbsenteeEducationalLevel
import np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.repository.WardWiseAbsenteeEducationalLevelRepository
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
class WardWiseAbsenteeEducationalLevelControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardWiseAbsenteeEducationalLevelRepository: WardWiseAbsenteeEducationalLevelRepository

    private lateinit var testEntries: List<WardWiseAbsenteeEducationalLevel>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardWiseAbsenteeEducationalLevelRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardWiseAbsenteeEducationalLevelRepository.deleteAll()
    }

    @Test
    fun `should get ward-wise-absentee-educational-level data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-absentee-educational-level/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.educationalLevel").value(EducationalLevelType.NURSERY.name))
            .andExpect(jsonPath("$.data.population").value(100))
            .andDo(
                document(
                    "ward-wise-absentee-educational-level-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-wise absentee educational level data entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-wise absentee educational level data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.educationalLevel").description("Educational level category"),
                        fieldWithPath("data.population").description("Population count"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-wise-absentee-educational-level data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-absentee-educational-level")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(6)) // Total entries in our test data
            .andDo(
                document(
                    "ward-wise-absentee-educational-level-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise absentee educational level data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].educationalLevel").description("Educational level category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-absentee-educational-level data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-absentee-educational-level/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for ward 1
            .andDo(
                document(
                    "ward-wise-absentee-educational-level-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise absentee educational level data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].educationalLevel").description("Educational level category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-wise-absentee-educational-level data by educational level`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-absentee-educational-level/by-educational-level/{educationalLevel}", EducationalLevelType.NURSERY)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(2)) // We have 2 entries for PRIMARY level
            .andDo(
                document(
                    "ward-wise-absentee-educational-level-get-by-educational-level",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("educationalLevel").description("Educational level to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-wise absentee educational level data for the specified educational level"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].educationalLevel").description("Educational level category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get educational level population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-absentee-educational-level/summary/by-educational-level")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 educational levels in test data
            .andDo(
                document(
                    "ward-wise-absentee-educational-level-summary-by-educational-level",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of educational level population summaries"),
                        fieldWithPath("data[].educationalLevel").description("Educational level category"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this educational level")
                    )
                )
            )
    }

    @Test
    fun `should get ward population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-wise-absentee-educational-level/summary/by-ward")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(2)) // We have 2 wards in test data
            .andDo(
                document(
                    "ward-wise-absentee-educational-level-summary-by-ward",
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
    private fun createTestData(): List<WardWiseAbsenteeEducationalLevel> {
        val entries = mutableListOf<WardWiseAbsenteeEducationalLevel>()

        // Create entries for Ward 1
        entries.add(createEntry(1, EducationalLevelType.NURSERY, 100))
        entries.add(createEntry(1, EducationalLevelType.CLASS_1, 75))
        entries.add(createEntry(1, EducationalLevelType.CLASS_2, 50))

        // Create entries for Ward 2
        entries.add(createEntry(2, EducationalLevelType.NURSERY, 90))
        entries.add(createEntry(2, EducationalLevelType.CLASS_1, 60))
        entries.add(createEntry(2, EducationalLevelType.CLASS_2, 30))

        return wardWiseAbsenteeEducationalLevelRepository.saveAll(entries)
    }

    private fun createEntry(wardNum: Int, educationalLevel: EducationalLevelType, pop: Int): WardWiseAbsenteeEducationalLevel {
        val entry = WardWiseAbsenteeEducationalLevel()
        entry.wardNumber = wardNum
        entry.educationalLevel = educationalLevel
        entry.population = pop
        return entry
    }
}
