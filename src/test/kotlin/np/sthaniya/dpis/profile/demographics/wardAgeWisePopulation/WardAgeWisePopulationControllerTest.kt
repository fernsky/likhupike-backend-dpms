package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation

import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.AgeGroup
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model.WardAgeWisePopulation
import np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.repository.WardAgeWisePopulationRepository
import np.sthaniya.dpis.profile.demographics.common.model.Gender
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
class WardAgeWisePopulationControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardAgeWisePopulationRepository: WardAgeWisePopulationRepository

    private lateinit var testEntries: List<WardAgeWisePopulation>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardAgeWisePopulationRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardAgeWisePopulationRepository.deleteAll()
    }

    @Test
    fun `should get ward-age-wise population data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-population/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.ageGroup").value(AgeGroup.AGE_0_4.name))
            .andExpect(jsonPath("$.data.gender").value(Gender.MALE.name))
            .andExpect(jsonPath("$.data.population").value(25))
            .andDo(
                document(
                    "ward-age-wise-population-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward-age-wise population data entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward-age-wise population data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.ageGroup").description("Age group category"),
                        fieldWithPath("data.gender").description("Gender category"),
                        fieldWithPath("data.population").description("Population count"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-age-wise population data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-population")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(7)) // Total entries in our test data
            .andDo(
                document(
                    "ward-age-wise-population-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-wise population data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-wise population data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-population/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(4)) // We have 4 entries for ward 1
            .andDo(
                document(
                    "ward-age-wise-population-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-wise population data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-wise population data by age group`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-population/by-age-group/{ageGroup}", AgeGroup.AGE_0_4)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(1)) // We have 1 entry for AGE_0_4
            .andDo(
                document(
                    "ward-age-wise-population-get-by-age-group",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("ageGroup").description("Age group to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-wise population data for the specified age group"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-age-wise population data by gender`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-population/by-gender/{gender}", Gender.MALE)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for MALE
            .andDo(
                document(
                    "ward-age-wise-population-get-by-gender",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("gender").description("Gender to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward-age-wise population data for the specified gender"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].population").description("Population count"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get age group population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-population/summary/by-age-group")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(4))) // We should have at least 4 summary entries
            .andDo(
                document(
                    "ward-age-wise-population-summary-by-age-group",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of age group population summaries"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this age group")
                    )
                )
            )
    }

    @Test
    fun `should get gender population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-population/summary/by-gender")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(2))) // We should have at least 2 summary entries (MALE, FEMALE)
            .andDo(
                document(
                    "ward-age-wise-population-summary-by-gender",
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
    fun `should get detailed ward-age-gender summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-age-wise-population/summary/detailed")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()", greaterThanOrEqualTo(5))) // We should have at least 5 summary entries
            .andDo(
                document(
                    "ward-age-wise-population-summary-detailed",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of detailed ward-age-gender population summaries"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].ageGroup").description("Age group category"),
                        fieldWithPath("data[].gender").description("Gender category"),
                        fieldWithPath("data[].totalPopulation").description("Total population count for this combination")
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): List<WardAgeWisePopulation> {
        val entries = mutableListOf<WardAgeWisePopulation>()

        // Create entries for Ward 1
        entries.add(createEntry(1, AgeGroup.AGE_0_4, Gender.MALE, 25))
        entries.add(createEntry(1, AgeGroup.AGE_5_9, Gender.FEMALE, 30))
        entries.add(createEntry(1, AgeGroup.AGE_10_14, Gender.MALE, 35))
        entries.add(createEntry(1, AgeGroup.AGE_15_19, Gender.FEMALE, 40))

        // Create entries for Ward 2
        entries.add(createEntry(2, AgeGroup.AGE_20_24, Gender.MALE, 45))
        entries.add(createEntry(2, AgeGroup.AGE_25_29, Gender.FEMALE, 50))
        entries.add(createEntry(2, AgeGroup.AGE_30_34, Gender.OTHER, 15))

        return wardAgeWisePopulationRepository.saveAll(entries)
    }

    private fun createEntry(wardNum: Int, age: AgeGroup, gen: Gender, pop: Int): WardAgeWisePopulation {
        val entry = WardAgeWisePopulation()
        entry.wardNumber = wardNum
        entry.ageGroup = age
        entry.gender = gen
        entry.population = pop
        return entry
    }
}
