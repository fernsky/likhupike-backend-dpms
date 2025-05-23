package np.sthaniya.dpis.profile.demographics.demographicSummary

import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.request.UpdateDemographicSummaryDto
import np.sthaniya.dpis.profile.demographics.demographicSummary.dto.response.DemographicSummaryResponse
import np.sthaniya.dpis.profile.demographics.demographicSummary.service.DemographicSummaryService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class DemographicSummaryControllerTest : BaseDemographicTestSupport() {

    @MockBean
    private lateinit var demographicSummaryService: DemographicSummaryService

    private val summaryId = UUID.randomUUID()
    private val now = Instant.now()

    @BeforeEach
    override fun setUp() {
        // Setup test user with view permissions
        setupTestUserWithViewPermissions()

        // Mock the service's response
        Mockito.`when`(demographicSummaryService.getDemographicSummary()).thenReturn(createTestDemographicSummary())
    }

    @Test
    fun `should get demographic summary successfully`() {
        mockMvc.perform(
                get("/api/v1/profile/demographics/summary")
                    .with(getAuthForUser())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.totalPopulation").value(25000))
            .andExpect(jsonPath("$.data.populationMale").value(12100))
            .andExpect(jsonPath("$.data.populationFemale").value(12800))
            .andExpect(jsonPath("$.data.sexRatio").value(94.5))
            .andDo(
                document(
                    "demographic-summary-get",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Demographic summary data"),
                        fieldWithPath("data.id").description("Unique identifier for the demographic summary"),
                        fieldWithPath("data.totalPopulation").description("Total population in the municipality"),
                        fieldWithPath("data.populationMale").description("Male population in the municipality"),
                        fieldWithPath("data.populationFemale").description("Female population in the municipality"),
                        fieldWithPath("data.populationOther").description("Other gender population in the municipality"),
                        fieldWithPath("data.populationAbsenteeTotal").description("Total absentee population in the municipality"),
                        fieldWithPath("data.populationMaleAbsentee").description("Male absentee population in the municipality"),
                        fieldWithPath("data.populationFemaleAbsentee").description("Female absentee population in the municipality"),
                        fieldWithPath("data.populationOtherAbsentee").description("Other gender absentee population in the municipality"),
                        fieldWithPath("data.sexRatio").description("Sex ratio (number of males per 100 females)"),
                        fieldWithPath("data.totalHouseholds").description("Total number of households"),
                        fieldWithPath("data.averageHouseholdSize").description("Average household size"),
                        fieldWithPath("data.populationDensity").description("Population density (people per sq km)"),
                        fieldWithPath("data.population0To14").description("Population aged 0-14 years"),
                        fieldWithPath("data.population15To59").description("Population aged 15-59 years"),
                        fieldWithPath("data.population60AndAbove").description("Population aged 60 and above"),
                        fieldWithPath("data.growthRate").description("Population growth rate"),
                        fieldWithPath("data.literacyRateAbove15").description("Literacy rate for population above 15 years"),
                        fieldWithPath("data.literacyRate15To24").description("Literacy rate for population between 15-24 years"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }


    // Helper method to create a test demographic summary
    private fun createTestDemographicSummary(): DemographicSummaryResponse {
        return DemographicSummaryResponse(
            id = summaryId,
            totalPopulation = 25000,
            populationMale = 12100,
            populationFemale = 12800,
            populationOther = 100,
            populationAbsenteeTotal = 2500,
            populationMaleAbsentee = 1600,
            populationFemaleAbsentee = 900,
            populationOtherAbsentee = 0,
            sexRatio = BigDecimal("94.5"),
            totalHouseholds = 5600,
            averageHouseholdSize = BigDecimal("4.46"),
            populationDensity = BigDecimal("185.2"),
            population0To14 = 7500,
            population15To59 = 14200,
            population60AndAbove = 3300,
            growthRate = BigDecimal("1.2"),
            literacyRateAbove15 = BigDecimal("86.5"),
            literacyRate15To24 = BigDecimal("98.2"),
            createdAt = now.minusSeconds(3600),
            updatedAt = now
        )
    }
}
