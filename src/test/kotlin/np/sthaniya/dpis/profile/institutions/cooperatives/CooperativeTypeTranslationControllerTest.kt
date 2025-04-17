package np.sthaniya.dpis.profile.institutions.cooperatives

import np.sthaniya.dpis.profile.institutions.cooperatives.base.BaseCooperativeTestSupport
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CooperativeTypeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeTypeTranslationService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CooperativeTypeTranslationControllerTest : BaseCooperativeTestSupport() {

    @Autowired
    private lateinit var cooperativeTypeTranslationService: CooperativeTypeTranslationService

    @BeforeEach
    fun setup() {
        // Create a user with appropriate permissions
        setupTestUserWithAllCooperativePermissions()
    }

    @Test
    fun `should create or update type translation successfully`() {
        val createDto = CooperativeTypeTranslationDto(
            cooperativeType = CooperativeType.AGRICULTURE,
            locale = "ne",
            name = "कृषि सहकारी",
            description = "कृषि क्षेत्रमा काम गर्ने सहकारी संस्था"
        )

        mockMvc.perform(
            post("/api/v1/cooperative-types/translations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.cooperativeType").value("AGRICULTURE"))
            .andExpect(jsonPath("$.data.locale").value("ne"))
            .andExpect(jsonPath("$.data.name").value("कृषि सहकारी"))
            .andDo(
                document(
                    "cooperative-type-translation-create-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("cooperativeType").description("Type of cooperative"),
                        fieldWithPath("locale").description("Locale code for this translation"),
                        fieldWithPath("name").description("Localized name of the cooperative type"),
                        fieldWithPath("description").description("Localized description of this cooperative type").optional()
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Translation data"),
                        fieldWithPath("data.id").description("Unique identifier for the translation"),
                        fieldWithPath("data.cooperativeType").description("Type of cooperative"),
                        fieldWithPath("data.locale").description("Locale for this translation"),
                        fieldWithPath("data.name").description("Localized name of the cooperative type"),
                        fieldWithPath("data.description").description("Localized description of this cooperative type")
                    )
                )
            )
    }

    @Test
    fun `should get type translation by id`() {
        // First create a translation to retrieve
        val translationId = createTestTypeTranslation(CooperativeType.DAIRY, "ne").id

        mockMvc.perform(
            get("/api/v1/cooperative-types/translations/{translationId}", translationId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").value(translationId.toString()))
            .andDo(
                document(
                    "cooperative-type-translation-get-by-id",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("translationId").description("The ID of the translation to retrieve")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Translation data"),
                        fieldWithPath("data.id").description("Unique identifier for the translation"),
                        fieldWithPath("data.cooperativeType").description("Type of cooperative"),
                        fieldWithPath("data.locale").description("Locale for this translation"),
                        fieldWithPath("data.name").description("Localized name of the cooperative type"),
                        fieldWithPath("data.description").description("Localized description of this cooperative type")
                    )
                )
            )
    }

    @Test
    fun `should get type translation by type and locale`() {
        // First create a translation
        createTestTypeTranslation(CooperativeType.DAIRY, "ne")

        mockMvc.perform(
            get("/api/v1/cooperative-types/translations/type/{type}/locale/{locale}", "DAIRY", "ne")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.cooperativeType").value("DAIRY"))
            .andExpect(jsonPath("$.data.locale").value("ne"))
            .andDo(
                document(
                    "cooperative-type-translation-get-by-type-and-locale",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("type").description("The type of cooperative"),
                        parameterWithName("locale").description("The locale code of the translation")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Translation data"),
                        fieldWithPath("data.id").description("Unique identifier for the translation"),
                        fieldWithPath("data.cooperativeType").description("Type of cooperative"),
                        fieldWithPath("data.locale").description("Locale for this translation"),
                        fieldWithPath("data.name").description("Localized name of the cooperative type"),
                        fieldWithPath("data.description").description("Localized description of this cooperative type")
                    )
                )
            )
    }

    @Test
    fun `should get all translations for type`() {
        // Create two translations for the same type
        createTestTypeTranslation(CooperativeType.DAIRY, "ne")
        createTestTypeTranslation(CooperativeType.DAIRY, "en")

        mockMvc.perform(
            get("/api/v1/cooperative-types/translations/type/{type}", "DAIRY")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].cooperativeType").value("DAIRY"))
            .andDo(
                document(
                    "cooperative-type-translation-get-all-for-type",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("type").description("The type of cooperative")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of translations"),
                        fieldWithPath("data[].id").description("Unique identifier for the translation"),
                        fieldWithPath("data[].cooperativeType").description("Type of cooperative"),
                        fieldWithPath("data[].locale").description("Locale for this translation"),
                        fieldWithPath("data[].name").description("Localized name of the cooperative type"),
                        fieldWithPath("data[].description").description("Localized description of this cooperative type")
                    )
                )
            )
    }

    @Test
    fun `should get translations by locale`() {
        // Create translations for different types with the same locale
        createTestTypeTranslation(CooperativeType.DAIRY, "en")
        createTestTypeTranslation(CooperativeType.AGRICULTURE, "en")

        mockMvc.perform(
            get("/api/v1/cooperative-types/translations/locale/{locale}", "en")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isArray)
            .andExpect(jsonPath("$.data.content.length()").value(2))
            .andDo(
                document(
                    "cooperative-type-translation-get-by-locale",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("locale").description("The locale code")
                    ),
                    queryParameters(
                        parameterWithName("page").description("Page number (zero-based)"),
                        parameterWithName("size").description("Page size")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data.content").description("List of translations"),
                        fieldWithPath("data.content[].id").description("Unique identifier for the translation"),
                        fieldWithPath("data.content[].cooperativeType").description("Type of cooperative"),
                        fieldWithPath("data.content[].locale").description("Locale for this translation"),
                        fieldWithPath("data.content[].name").description("Localized name of the cooperative type"),
                        fieldWithPath("data.content[].description").description("Localized description of this cooperative type"),
                        fieldWithPath("data.pageable").description("Pagination information"),
                        fieldWithPath("data.pageable.sort").description("Sort information"),
                        fieldWithPath("data.pageable.sort.empty").description("Whether sort is empty"),
                        fieldWithPath("data.pageable.sort.sorted").description("Whether sort is sorted"),
                        fieldWithPath("data.pageable.sort.unsorted").description("Whether sort is unsorted"),
                        fieldWithPath("data.pageable.offset").description("Offset of the current page"),
                        fieldWithPath("data.pageable.pageNumber").description("Current page number"),
                        fieldWithPath("data.pageable.pageSize").description("Size of page"),
                        fieldWithPath("data.pageable.paged").description("Whether pagination is enabled"),
                        fieldWithPath("data.pageable.unpaged").description("Whether pagination is disabled"),
                        fieldWithPath("data.last").description("Whether this is the last page"),
                        fieldWithPath("data.totalElements").description("Total number of elements"),
                        fieldWithPath("data.totalPages").description("Total number of pages"),
                        fieldWithPath("data.size").description("Size of page"),
                        fieldWithPath("data.number").description("Current page number"),
                        fieldWithPath("data.sort").description("Sort information"),
                        fieldWithPath("data.sort.empty").description("Whether sort is empty"),
                        fieldWithPath("data.sort.sorted").description("Whether sort is sorted"),
                        fieldWithPath("data.sort.unsorted").description("Whether sort is unsorted"),
                        fieldWithPath("data.first").description("Whether this is the first page"),
                        fieldWithPath("data.numberOfElements").description("Number of elements in the current page"),
                        fieldWithPath("data.empty").description("Whether the page is empty")
                    )
                )
            )
    }

    @Test
    fun `should delete type translation`() {
        // First create a translation to delete
        createTestTypeTranslation(CooperativeType.SAVINGS_AND_CREDIT, "ne")

        mockMvc.perform(
            delete("/api/v1/cooperative-types/translations/type/{type}/locale/{locale}", "SAVINGS_AND_CREDIT", "ne")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "cooperative-type-translation-delete",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("type").description("The type of cooperative"),
                        parameterWithName("locale").description("The locale code of the translation")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message indicating translation was deleted")
                    )
                )
            )
    }

    @Test
    fun `should get all translations for locale`() {
        // Create translations for different types with the same locale
        createTestTypeTranslation(CooperativeType.DAIRY, "ne")
        createTestTypeTranslation(CooperativeType.AGRICULTURE, "ne")

        mockMvc.perform(
            get("/api/v1/cooperative-types/translations/all-types/locale/{locale}", "ne")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.DAIRY").exists())
            .andExpect(jsonPath("$.data.AGRICULTURE").exists())
            .andDo(
                document(
                    "cooperative-type-translation-get-all-for-locale",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("locale").description("The locale code")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Map of cooperative types to their translations"),
                        fieldWithPath("data.DAIRY.id").description("Unique identifier for the DAIRY translation"),
                        fieldWithPath("data.DAIRY.cooperativeType").description("Type of cooperative (DAIRY)"),
                        fieldWithPath("data.DAIRY.locale").description("Locale for this translation"),
                        fieldWithPath("data.DAIRY.name").description("Localized name of the DAIRY type"),
                        fieldWithPath("data.DAIRY.description").description("Localized description of the DAIRY type"),
                        fieldWithPath("data.AGRICULTURE.id").description("Unique identifier for the AGRICULTURE translation"),
                        fieldWithPath("data.AGRICULTURE.cooperativeType").description("Type of cooperative (AGRICULTURE)"),
                        fieldWithPath("data.AGRICULTURE.locale").description("Locale for this translation"),
                        fieldWithPath("data.AGRICULTURE.name").description("Localized name of the AGRICULTURE type"),
                        fieldWithPath("data.AGRICULTURE.description").description("Localized description of the AGRICULTURE type")
                    )
                )
            )
    }

    @Test
    fun `should return 401 when creating translation without authentication`() {
        val createDto = CooperativeTypeTranslationDto(
            cooperativeType = CooperativeType.AGRICULTURE,
            locale = "ne",
            name = "कृषि सहकारी",
            description = "कृषि क्षेत्रमा काम गर्ने सहकारी संस्था"
        )

        mockMvc.perform(
            post("/api/v1/cooperative-types/translations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return 403 when creating translation without proper permission`() {
        val createDto = CooperativeTypeTranslationDto(
            cooperativeType = CooperativeType.AGRICULTURE,
            locale = "ne",
            name = "कृषि सहकारी",
            description = "कृषि क्षेत्रमा काम गर्ने सहकारी संस्था"
        )

        mockMvc.perform(
            post("/api/v1/cooperative-types/translations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto))
        )
            .andExpect(status().isForbidden)
    }

    // Helper method to create a test type translation
    private fun createTestTypeTranslation(type: CooperativeType, locale: String) = 
        cooperativeTypeTranslationService.createOrUpdateTypeTranslation(
            CooperativeTypeTranslationDto(
                cooperativeType = type,
                locale = locale,
                name = "Test ${type.name} in $locale",
                description = "Test description for ${type.name} in $locale"
            )
        )
}
