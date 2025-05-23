package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation

import np.sthaniya.dpis.profile.demographics.base.BaseDemographicTestSupport
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.model.WardTimeSeriesPopulation
import np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.repository.WardTimeSeriesPopulationRepository
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
import jakarta.transaction.Transactional
import java.math.BigDecimal
import java.util.*

@Transactional
class WardTimeSeriesPopulationControllerTest : BaseDemographicTestSupport() {

    @Autowired
    private lateinit var wardTimeSeriesPopulationRepository: WardTimeSeriesPopulationRepository

    private lateinit var testEntries: List<WardTimeSeriesPopulation>
    private lateinit var testEntryId: UUID

    @BeforeEach
    override fun setUp() {
        // Setup test user with admin permissions to ensure we can create the test data
        setupTestUserWithAdminPermissions()

        // Clean up any existing test data
        wardTimeSeriesPopulationRepository.deleteAll()

        // Create test data
        testEntries = createTestData()
        testEntryId = testEntries.first().id!!

        // Set up user with view permissions for the tests
        setupTestUserWithViewPermissions()
    }

    @AfterEach
    fun tearDownData() {
        // Clean up test data
        wardTimeSeriesPopulationRepository.deleteAll()
    }

    @Test
    fun `should get ward-time-series-population data by ID`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-time-series-population/{id}", testEntryId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.wardNumber").value(1))
            .andExpect(jsonPath("$.data.year").value(2068))
            .andExpect(jsonPath("$.data.totalPopulation").value(15000))
            .andDo(
                document(
                    "ward-time-series-population-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id").description("ID of the ward time series population data entry")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Ward time series population data"),
                        fieldWithPath("data.id").description("Unique identifier"),
                        fieldWithPath("data.wardNumber").description("Ward number"),
                        fieldWithPath("data.wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data.year").description("Census year"),
                        fieldWithPath("data.totalPopulation").description("Total population in the ward"),
                        fieldWithPath("data.malePopulation").description("Male population in the ward"),
                        fieldWithPath("data.femalePopulation").description("Female population in the ward"),
                        fieldWithPath("data.otherPopulation").description("Other gender population in the ward"),
                        fieldWithPath("data.totalHouseholds").description("Total number of households in the ward"),
                        fieldWithPath("data.averageHouseholdSize").description("Average household size in the ward"),
                        fieldWithPath("data.population0To14").description("Population aged 0-14 years in the ward"),
                        fieldWithPath("data.population15To59").description("Population aged 15-59 years in the ward"),
                        fieldWithPath("data.population60AndAbove").description("Population aged 60 and above in the ward"),
                        fieldWithPath("data.literacyRate").description("Literacy rate in the ward"),
                        fieldWithPath("data.maleLiteracyRate").description("Male literacy rate in the ward"),
                        fieldWithPath("data.femaleLiteracyRate").description("Female literacy rate in the ward"),
                        fieldWithPath("data.growthRate").description("Population growth rate in the ward"),
                        fieldWithPath("data.areaSqKm").description("Area of the ward in square kilometers"),
                        fieldWithPath("data.populationDensity").description("Population density in the ward"),
                        fieldWithPath("data.sexRatio").description("Sex ratio in the ward"),
                        fieldWithPath("data.createdAt").description("When this record was created"),
                        fieldWithPath("data.updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get all ward-time-series-population data`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-time-series-population")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(6)) // Total entries in our test data
            .andDo(
                document(
                    "ward-time-series-population-get-all",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward time series population data"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data[].year").description("Census year"),
                        fieldWithPath("data[].totalPopulation").description("Total population in the ward"),
                        fieldWithPath("data[].malePopulation").description("Male population in the ward"),
                        fieldWithPath("data[].femalePopulation").description("Female population in the ward"),
                        fieldWithPath("data[].otherPopulation").description("Other gender population in the ward"),
                        fieldWithPath("data[].totalHouseholds").description("Total number of households in the ward"),
                        fieldWithPath("data[].averageHouseholdSize").description("Average household size in the ward"),
                        fieldWithPath("data[].population0To14").description("Population aged 0-14 years in the ward"),
                        fieldWithPath("data[].population15To59").description("Population aged 15-59 years in the ward"),
                        fieldWithPath("data[].population60AndAbove").description("Population aged 60 and above in the ward"),
                        fieldWithPath("data[].literacyRate").description("Literacy rate in the ward"),
                        fieldWithPath("data[].maleLiteracyRate").description("Male literacy rate in the ward"),
                        fieldWithPath("data[].femaleLiteracyRate").description("Female literacy rate in the ward"),
                        fieldWithPath("data[].growthRate").description("Population growth rate in the ward"),
                        fieldWithPath("data[].areaSqKm").description("Area of the ward in square kilometers"),
                        fieldWithPath("data[].populationDensity").description("Population density in the ward"),
                        fieldWithPath("data[].sexRatio").description("Sex ratio in the ward"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-time-series-population data by ward`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-time-series-population/by-ward/{wardNumber}", 1)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(2)) // We have 3 entries for ward 1
            .andDo(
                document(
                    "ward-time-series-population-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("wardNumber").description("Ward number to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward time series population data for the specified ward"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data[].year").description("Census year"),
                        fieldWithPath("data[].totalPopulation").description("Total population in the ward"),
                        fieldWithPath("data[].malePopulation").description("Male population in the ward"),
                        fieldWithPath("data[].femalePopulation").description("Female population in the ward"),
                        fieldWithPath("data[].otherPopulation").description("Other gender population in the ward"),
                        fieldWithPath("data[].totalHouseholds").description("Total number of households in the ward"),
                        fieldWithPath("data[].averageHouseholdSize").description("Average household size in the ward"),
                        fieldWithPath("data[].population0To14").description("Population aged 0-14 years in the ward"),
                        fieldWithPath("data[].population15To59").description("Population aged 15-59 years in the ward"),
                        fieldWithPath("data[].population60AndAbove").description("Population aged 60 and above in the ward"),
                        fieldWithPath("data[].literacyRate").description("Literacy rate in the ward"),
                        fieldWithPath("data[].maleLiteracyRate").description("Male literacy rate in the ward"),
                        fieldWithPath("data[].femaleLiteracyRate").description("Female literacy rate in the ward"),
                        fieldWithPath("data[].growthRate").description("Population growth rate in the ward"),
                        fieldWithPath("data[].areaSqKm").description("Area of the ward in square kilometers"),
                        fieldWithPath("data[].populationDensity").description("Population density in the ward"),
                        fieldWithPath("data[].sexRatio").description("Sex ratio in the ward"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get ward-time-series-population data by year`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-time-series-population/by-year/{year}", 2068)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 entries for year 2068
            .andDo(
                document(
                    "ward-time-series-population-get-by-year",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("year").description("Year to filter by")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of ward time series population data for the specified year"),
                        fieldWithPath("data[].id").description("Unique identifier"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data[].year").description("Census year"),
                        fieldWithPath("data[].totalPopulation").description("Total population in the ward"),
                        fieldWithPath("data[].malePopulation").description("Male population in the ward"),
                        fieldWithPath("data[].femalePopulation").description("Female population in the ward"),
                        fieldWithPath("data[].otherPopulation").description("Other gender population in the ward"),
                        fieldWithPath("data[].totalHouseholds").description("Total number of households in the ward"),
                        fieldWithPath("data[].averageHouseholdSize").description("Average household size in the ward"),
                        fieldWithPath("data[].population0To14").description("Population aged 0-14 years in the ward"),
                        fieldWithPath("data[].population15To59").description("Population aged 15-59 years in the ward"),
                        fieldWithPath("data[].population60AndAbove").description("Population aged 60 and above in the ward"),
                        fieldWithPath("data[].literacyRate").description("Literacy rate in the ward"),
                        fieldWithPath("data[].maleLiteracyRate").description("Male literacy rate in the ward"),
                        fieldWithPath("data[].femaleLiteracyRate").description("Female literacy rate in the ward"),
                        fieldWithPath("data[].growthRate").description("Population growth rate in the ward"),
                        fieldWithPath("data[].areaSqKm").description("Area of the ward in square kilometers"),
                        fieldWithPath("data[].populationDensity").description("Population density in the ward"),
                        fieldWithPath("data[].sexRatio").description("Sex ratio in the ward"),
                        fieldWithPath("data[].createdAt").description("When this record was created"),
                        fieldWithPath("data[].updatedAt").description("When this record was last updated")
                    )
                )
            )
    }

    @Test
    fun `should get latest ward population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-time-series-population/summary/latest-by-ward")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // We have 3 wards in our test data
            .andDo(
                document(
                    "ward-time-series-population-summary-latest-by-ward",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of latest ward population summaries"),
                        fieldWithPath("data[].wardNumber").description("Ward number"),
                        fieldWithPath("data[].wardName").description("Ward name (if available)").optional(),
                        fieldWithPath("data[].totalPopulation").description("Latest total population for this ward"),
                        fieldWithPath("data[].year").description("Year of the latest data")
                    )
                )
            )
    }

    @Test
    fun `should get year population summary`() {
        mockMvc.perform(
            get("/api/v1/profile/demographics/ward-time-series-population/summary/by-year")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(2)) // We have 2 years in our test data
            .andDo(
                document(
                    "ward-time-series-population-summary-by-year",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of year population summaries"),
                        fieldWithPath("data[].year").description("Census year"),
                        fieldWithPath("data[].totalPopulation").description("Total population for this year")
                    )
                )
            )
    }

    // Helper method to create test data
    private fun createTestData(): List<WardTimeSeriesPopulation> {
        val entries = mutableListOf<WardTimeSeriesPopulation>()

        // Create entries for 2068 census year
        entries.add(createEntry(1, "Ward One", 2068, 15000, 7300, 7650, 50, 3200, BigDecimal("4.7"), 4500, 8000, 2500))
        entries.add(createEntry(2, "Ward Two", 2068, 12000, 5850, 6100, 50, 2600, BigDecimal("4.6"), 3700, 6500, 1800))
        entries.add(createEntry(3, "Ward Three", 2068, 8000, 3900, 4050, 50, 1750, BigDecimal("4.6"), 2400, 4300, 1300))

        // Create entries for 2078 census year
        entries.add(createEntry(1, "Ward One", 2078, 16500, 8000, 8400, 100, 3500, BigDecimal("4.7"), 4800, 9000, 2700))
        entries.add(createEntry(2, "Ward Two", 2078, 13200, 6400, 6700, 100, 2800, BigDecimal("4.7"), 3900, 7100, 2200))
        entries.add(createEntry(3, "Ward Three", 2078, 9000, 4400, 4500, 100, 2000, BigDecimal("4.5"), 2600, 4900, 1500))

        return wardTimeSeriesPopulationRepository.saveAll(entries)
    }

    private fun createEntry(
        wardNum: Int,
        wardName: String,
        year: Int,
        totalPop: Int,
        malePop: Int,
        femalePop: Int,
        otherPop: Int,
        totalHouseholds: Int,
        avgHouseholdSize: BigDecimal,
        pop0To14: Int,
        pop15To59: Int,
        pop60AndAbove: Int
    ): WardTimeSeriesPopulation {
        val entry = WardTimeSeriesPopulation()
        entry.wardNumber = wardNum
        entry.wardName = wardName
        entry.year = year
        entry.totalPopulation = totalPop
        entry.malePopulation = malePop
        entry.femalePopulation = femalePop
        entry.otherPopulation = otherPop
        entry.totalHouseholds = totalHouseholds
        entry.averageHouseholdSize = avgHouseholdSize
        entry.population0To14 = pop0To14
        entry.population15To59 = pop15To59
        entry.population60AndAbove = pop60AndAbove
        entry.literacyRate = BigDecimal("85.5")
        entry.maleLiteracyRate = BigDecimal("88.2")
        entry.femaleLiteracyRate = BigDecimal("82.7")
        entry.growthRate = BigDecimal("1.2")
        entry.areaSqKm = BigDecimal("28.5")
        entry.populationDensity = BigDecimal(totalPop.toDouble() / 28.5)
        entry.sexRatio = BigDecimal(malePop.toDouble() / femalePop.toDouble() * 100)
        return entry
    }
}
