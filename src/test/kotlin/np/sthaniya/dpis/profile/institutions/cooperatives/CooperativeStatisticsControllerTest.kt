
package np.sthaniya.dpis.profile.institutions.cooperatives

import np.sthaniya.dpis.profile.institutions.cooperatives.base.BaseCooperativeTestSupport
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.GeoPointDto
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

class CooperativeStatisticsControllerTest : BaseCooperativeTestSupport() {

    @Autowired private lateinit var cooperativeService: CooperativeService

    @BeforeEach
    fun setup() {
        // Create a user with view permissions
        createTestUserWithPermissions(permissions = listOf("PERMISSION_VIEW_COOPERATIVE"))

        // Create test cooperatives of different types and wards
        createTestCooperative("dairy-coop-1", "Dairy Cooperative 1", CooperativeType.DAIRY, 5)
        createTestCooperative("dairy-coop-2", "Dairy Cooperative 2", CooperativeType.DAIRY, 6)
        createTestCooperative("agriculture-coop-1", "Agriculture Cooperative 1", CooperativeType.AGRICULTURE, 5)
        createTestCooperative("consumer-coop-1", "Consumer Cooperative 1", CooperativeType.CONSUMER, 7)
    }

    // @Test
    // fun `should get statistics by type`() {
    //     mockMvc.perform(
    //             get("/api/v1/cooperatives/statistics/by-type")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //             )
    //             .andExpect(status().isOk)
    //             .andExpect(jsonPath("$.success").value(true))
    //             .andExpect(jsonPath("$.data").exists())
    //             .andExpect(jsonPath("$.data.statistics").exists())
    //             .andExpect(jsonPath("$.data.statistics.DAIRY").value(2))
    //             .andExpect(jsonPath("$.data.statistics.AGRICULTURE").value(1))
    //             .andExpect(jsonPath("$.data.statistics.CONSUMER").value(1))
    //             .andDo(
    //                     document(
    //                             "cooperative-statistics-by-type",
    //                             preprocessResponse(prettyPrint()),
    //                             responseFields(
    //                                     fieldWithPath("success")
    //                                             .description("Indicates if the request was successful"),
    //                                     fieldWithPath("message")
    //                                             .description("Success message"),
    //                                     fieldWithPath("data")
    //                                             .description("Cooperative statistics data"),
    //                                     fieldWithPath("data.statistics")
    //                                             .description("Map of cooperative types to their counts"),
    //                                     fieldWithPath("data.statistics.DAIRY")
    //                                             .description("Count of DAIRY cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.AGRICULTURE")
    //                                             .description("Count of AGRICULTURE cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.CONSUMER")
    //                                             .description("Count of CONSUMER cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.MULTIPURPOSE")
    //                                             .description("Count of MULTIPURPOSE cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.SAVINGS_CREDIT")
    //                                             .description("Count of SAVINGS_CREDIT cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.ENERGY")
    //                                             .description("Count of ENERGY cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.HEALTH")
    //                                             .description("Count of HEALTH cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.PRODUCTION")
    //                                             .description("Count of PRODUCTION cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.TOURISM")
    //                                             .description("Count of TOURISM cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.COMMUNICATION")
    //                                             .description("Count of COMMUNICATION cooperatives")
    //                                             .optional(),
    //                                     fieldWithPath("data.statistics.OTHER")
    //                                             .description("Count of OTHER cooperatives")
    //                                             .optional()
    //                             )
    //                     )
    //             )
    // }

    @Test
    fun `should get statistics by ward`() {
        mockMvc.perform(
                get("/api/v1/cooperatives/statistics/by-ward")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.statistics").exists())
                .andExpect(jsonPath("$.data.statistics.5").value(2))
                .andExpect(jsonPath("$.data.statistics.6").value(1))
                .andExpect(jsonPath("$.data.statistics.7").value(1))
                .andDo(
                        document(
                                "cooperative-statistics-by-ward",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("success")
                                                .description("Indicates if the request was successful"),
                                        fieldWithPath("message")
                                                .description("Success message"),
                                        fieldWithPath("data")
                                                .description("Cooperative statistics data"),
                                        fieldWithPath("data.statistics")
                                                .description("Map of ward numbers to cooperative counts"),
                                        fieldWithPath("data.statistics.5")
                                                .description("Count of cooperatives in ward 5")
                                                .optional(),
                                        fieldWithPath("data.statistics.6")
                                                .description("Count of cooperatives in ward 6")
                                                .optional(),
                                        fieldWithPath("data.statistics.7")
                                                .description("Count of cooperatives in ward 7")
                                                .optional()
                                        // Note: For documentation purposes, we only show wards with data in the test
                                        // In a real system, there could be up to all ward numbers
                                )
                        )
                )
    }

    // Helper methods

    private fun createTestCooperative(
            code: String,
            name: String,
            type: CooperativeType,
            ward: Int,
            status: CooperativeStatus = CooperativeStatus.ACTIVE
    ) =
            cooperativeService.createCooperative(
                    CreateCooperativeDto(
                            code = code,
                            defaultLocale = "ne",
                            establishedDate = LocalDate.of(2018, 3, 15),
                            ward = ward,
                            type = type,
                            status = status,
                            registrationNumber = "REG-$code",
                            point = GeoPointDto(longitude = 85.3240, latitude = 27.7172),
                            contactEmail = "contact@$code.np",
                            contactPhone = "+977 9812345678",
                            websiteUrl = "https://$code.np",
                            translation =
                                    CreateCooperativeTranslationDto(
                                            locale = "ne",
                                            name = name,
                                            description = "Test description for $name",
                                            location = "वडा नं. $ward, लिखुपिके गाउँपालिका",
                                            slugUrl = code,
                                            services = "Test services for $name",
                                            achievements = "Test achievements for $name",
                                            operatingHours = "9 AM - 5 PM",
                                            seoTitle = "SEO Title for $name",
                                            seoDescription = "SEO Description for $name",
                                            seoKeywords = "keyword1, keyword2, keyword3",
                                            structuredData = "{}",
                                            metaRobots = "index, follow",
                                            status = ContentStatus.PUBLISHED
                                    )
                    )
            )
}
