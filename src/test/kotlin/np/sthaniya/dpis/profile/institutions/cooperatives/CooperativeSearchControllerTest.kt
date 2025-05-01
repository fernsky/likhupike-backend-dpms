package np.sthaniya.dpis.profile.institutions.cooperatives

import java.time.LocalDate
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
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CooperativeSearchControllerTest : BaseCooperativeTestSupport() {

    @Autowired private lateinit var cooperativeService: CooperativeService

    @BeforeEach
    fun setup() {
        // Create a user with view permissions
        createTestUserWithPermissions(permissions = listOf("PERMISSION_VIEW_COOPERATIVE"))

        // Create test cooperatives
        createTestCooperative("dairy-coop-1", "Dairy Cooperative 1", CooperativeType.DAIRY, 5)

        createTestCooperative(
                "agriculture-coop-1",
                "Agriculture Cooperative 1",
                CooperativeType.AGRICULTURE,
                6
        )

        createTestCooperative(
                "dairy-coop-2",
                "Another Dairy Cooperative",
                CooperativeType.DAIRY,
                5,
                CooperativeStatus.INACTIVE
        )
    }

    @Test
    fun `should search cooperatives with advanced criteria using GET`() {
        mockMvc.perform(
                        get("/api/v1/cooperatives/search")
                                .param("type", "DAIRY")
                                .param("status", "ACTIVE")
                                .param("ward", "5")
                                .param("page", "1")
                                .param("size", "10")
                                .param("sortBy", "createdAt")
                                .param("sortDirection", "DESC")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray)
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].type").value("DAIRY"))
                .andExpect(jsonPath("$.data[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$.data[0].ward").value(5))
                .andExpect(jsonPath("$.message").exists())
                .andDo(
                        document(
                                "cooperative-advanced-search-get",
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("type")
                                                .description("Filter by cooperative type")
                                                .optional(),
                                        parameterWithName("status")
                                                .description("Filter by cooperative status")
                                                .optional(),
                                        parameterWithName("ward")
                                                .description("Filter by ward number")
                                                .optional(),
                                        parameterWithName("page")
                                                .description("Page number (1-based)")
                                                .optional(),
                                        parameterWithName("size")
                                                .description("Page size")
                                                .optional(),
                                        parameterWithName("sortBy")
                                                .description("Field to sort by")
                                                .optional(),
                                        parameterWithName("sortDirection")
                                                .description("Sort direction (ASC or DESC)")
                                                .optional()
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description("Indicates if the request was successful"),
                                        fieldWithPath("message")
                                                .description("Success message"),
                                        fieldWithPath("data")
                                                .description("List of cooperatives matching the search criteria"),
                                        fieldWithPath("data[].id")
                                                .description("Unique identifier for the cooperative"),
                                        fieldWithPath("data[].code")
                                                .description("Code/slug for the cooperative"),
                                        fieldWithPath("data[].type")
                                                .description("Type of cooperative"),
                                        fieldWithPath("data[].status")
                                                .description("Status of the cooperative"),
                                        fieldWithPath("data[].ward")
                                                .description("Ward where the cooperative is located"),
                                        fieldWithPath("data[].defaultLocale")
                                                .description("Default locale for this cooperative's content"),
                                        fieldWithPath("data[].establishedDate")
                                                .description("Date when the cooperative was established")
                                                .type(JsonFieldType.STRING),
                                        fieldWithPath("data[].registrationNumber")
                                                .description("Registration number of the cooperative"),
                                        fieldWithPath("data[].contactEmail")
                                                .description("Contact email for the cooperative"),
                                        fieldWithPath("data[].contactPhone")
                                                .description("Contact phone number"),
                                        fieldWithPath("data[].websiteUrl")
                                                .description("Website URL of the cooperative"),
                                        fieldWithPath("data[].point")
                                                .description("Geographic point location")
                                                .type(JsonFieldType.OBJECT),
                                        fieldWithPath("data[].point.longitude")
                                                .description("Longitude coordinate")
                                                .type(JsonFieldType.NUMBER),
                                        fieldWithPath("data[].point.latitude")
                                                .description("Latitude coordinate")
                                                .type(JsonFieldType.NUMBER),
                                        fieldWithPath("data[].createdAt")
                                                .description("When this record was created")
                                                .type(JsonFieldType.STRING),
                                        fieldWithPath("data[].updatedAt")
                                                .description("When this record was last updated")
                                                .type(JsonFieldType.STRING),

                                        // Explicitly ignore fields that might not be present
                                        // by removing them from the documentation or marking them optional

                                        // Document meta object and its fields
                                        fieldWithPath("meta")
                                                .description("Pagination metadata"),
                                        fieldWithPath("meta.page")
                                                .description("Current page number (1-based)"),
                                        fieldWithPath("meta.size")
                                                .description("Number of items per page"),
                                        fieldWithPath("meta.totalElements")
                                                .description("Total number of elements"),
                                        fieldWithPath("meta.totalPages")
                                                .description("Total number of pages"),
                                        fieldWithPath("meta.isFirst")
                                                .description("Whether this is the first page"),
                                        fieldWithPath("meta.isLast")
                                                .description("Whether this is the last page")
                                ).andWithPrefix("data[].",
                                        // These fields might not exist in the response
                                        // Mark them as optional with explicit types
                                        fieldWithPath("translations")
                                                .description("Translations for this cooperative")
                                                .type(JsonFieldType.ARRAY)
                                                .optional(),
                                        fieldWithPath("primaryMedia")
                                                .description("Primary media items for this cooperative")
                                                .type(JsonFieldType.OBJECT)
                                                .optional()
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

    private fun getCooperativeResponseFields():
            Array<org.springframework.restdocs.payload.FieldDescriptor> {
        return arrayOf(
                fieldWithPath("id").description("Unique identifier for the cooperative"),
                fieldWithPath("code").description("Code/slug for the cooperative"),
                fieldWithPath("defaultLocale")
                        .description("Default locale for this cooperative's content"),
                fieldWithPath("establishedDate")
                        .description("Date when the cooperative was established"),
                fieldWithPath("ward").description("Ward where the cooperative is located"),
                fieldWithPath("type").description("Type of cooperative"),
                fieldWithPath("status").description("Status of the cooperative"),
                fieldWithPath("registrationNumber")
                        .description("Registration number of the cooperative"),
                fieldWithPath("point").description("Geographic point location"),
                fieldWithPath("point.longitude").description("Longitude coordinate"),
                fieldWithPath("point.latitude").description("Latitude coordinate"),
                fieldWithPath("contactEmail").description("Contact email for the cooperative"),
                fieldWithPath("contactPhone").description("Contact phone number"),
                fieldWithPath("websiteUrl").description("Website URL of the cooperative"),
                fieldWithPath("createdAt").description("When this record was created"),
                fieldWithPath("updatedAt").description("When this record was last updated"),
                fieldWithPath("translations").description("Translations for this cooperative"),
                fieldWithPath("primaryMedia")
                        .description("Primary media items for this cooperative (one per type)")
        )
    }

    private fun getPaginationFields(): Array<org.springframework.restdocs.payload.FieldDescriptor> {
        return arrayOf(
                fieldWithPath("pageable").description("Pagination information"),
                fieldWithPath("pageable.sort").description("Sort information"),
                fieldWithPath("pageable.sort.empty").description("Whether sort is empty"),
                fieldWithPath("pageable.sort.sorted").description("Whether sort is sorted"),
                fieldWithPath("pageable.sort.unsorted").description("Whether sort is unsorted"),
                fieldWithPath("pageable.offset").description("Offset of the current page"),
                fieldWithPath("pageable.pageNumber").description("Current page number"),
                fieldWithPath("pageable.pageSize").description("Size of page"),
                fieldWithPath("pageable.paged").description("Whether pagination is enabled"),
                fieldWithPath("pageable.unpaged").description("Whether pagination is disabled"),
                fieldWithPath("last").description("Whether this is the last page"),
                fieldWithPath("totalElements").description("Total number of elements"),
                fieldWithPath("totalPages").description("Total number of pages"),
                fieldWithPath("size").description("Size of page"),
                fieldWithPath("number").description("Current page number"),
                fieldWithPath("sort").description("Sort information"),
                fieldWithPath("sort.empty").description("Whether sort is empty"),
                fieldWithPath("sort.sorted").description("Whether sort is sorted"),
                fieldWithPath("sort.unsorted").description("Whether sort is unsorted"),
                fieldWithPath("first").description("Whether this is the first page"),
                fieldWithPath("numberOfElements")
                        .description("Number of elements in the current page"),
                fieldWithPath("empty").description("Whether the page is empty")
        )
    }
}
