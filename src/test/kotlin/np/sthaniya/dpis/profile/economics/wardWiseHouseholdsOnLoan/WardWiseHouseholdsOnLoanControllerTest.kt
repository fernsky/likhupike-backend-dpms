package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan

import np.sthaniya.dpis.profile.economics.base.BaseEconomicsTestSupport
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.dto.response.WardWiseHouseholdsOnLoanSummaryResponse
import np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.service.WardWiseHouseholdsOnLoanService
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
import java.time.Instant
import java.util.*

class WardWiseHouseholdsOnLoanControllerTest : BaseEconomicsTestSupport() {

    @MockBean
    private lateinit var wardWiseHouseholdsOnLoanService: WardWiseHouseholdsOnLoanService

    private val entryId = UUID.randomUUID()
    private val now = Instant.now()

    @BeforeEach
    override fun setUp() {
        // Setup test user with view permissions
        setupTestUserWithViewPermissions()

        // Mock the service's responses
        val testData = createTestData()
        val testSummary = createTestSummary()

        Mockito.`when`(wardWiseHouseholdsOnLoanService.getAllWardWiseHouseholdsOnLoan(null))
            .thenReturn(listOf(testData))
        Mockito.`when`(wardWiseHouseholdsOnLoanService.getHouseholdsOnLoanSummary())
            .thenReturn(testSummary)
    }

    @Test
    fun `should get all households on loan data successfully`() {
        mockMvc.perform(
                get("/api/v1/profile/economics/ward-wise-households-on-loan")
                    .with(getAuthForUser())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data[0].id").exists())
            .andExpect(jsonPath("$.data[0].wardNumber").value(1))
            .andExpect(jsonPath("$.data[0].households").value(250))
            .andDo(
                document(
                    "ward-wise-households-on-loan-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Array of ward-wise households on loan data"),
                        fieldWithPath("data[].id").description("Unique identifier for the ward-wise households on loan entry"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].households").description("Number of households with loans in the ward"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get summary successfully`() {
        mockMvc.perform(
                get("/api/v1/profile/economics/ward-wise-households-on-loan/summary")
                    .with(getAuthForUser())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.totalHouseholdsOnLoan").value(2500))
            .andDo(
                document(
                    "ward-wise-households-on-loan-summary",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Households on loan summary data"),
                        fieldWithPath("data.totalHouseholdsOnLoan").description("Total number of households with loans across all wards")
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): WardWiseHouseholdsOnLoanResponse {
        return WardWiseHouseholdsOnLoanResponse(
            id = entryId,
            wardNumber = 1,
            households = 250,
            createdAt = now.minusSeconds(3600),
            updatedAt = now
        )
    }

    // Helper method to create test summary
    private fun createTestSummary(): WardWiseHouseholdsOnLoanSummaryResponse {
        return WardWiseHouseholdsOnLoanSummaryResponse(
            totalHouseholdsOnLoan = 2500
        )
    }
}
