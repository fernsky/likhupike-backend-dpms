package np.sthaniya.dpis.profile.institutions.cooperatives

import np.sthaniya.dpis.profile.institutions.cooperatives.base.BaseCooperativeTestSupport
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.GeoPointDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeService
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
import java.time.LocalDate
import java.util.UUID

class CooperativeTranslationControllerTest : BaseCooperativeTestSupport() {

    @Autowired
    private lateinit var cooperativeService: CooperativeService

    private lateinit var testCooperativeId: UUID

    @BeforeEach
    fun setup() {
        // Create a user with all permissions
        setupTestUserWithAllCooperativePermissions()
        
        // Create a test cooperative to use in the tests
        val cooperative = createTestCooperative()
        testCooperativeId = cooperative.id
    }

    @Test
    fun `should create cooperative translation successfully`() {
        val createDto = CreateCooperativeTranslationDto(
            locale = "en",
            name = "Agricultural Cooperative of Likhupike",
            description = "This cooperative focuses on agricultural development in Likhupike",
            location = "Ward 5, Likhupike Rural Municipality",
            slugUrl = "agri-coop-likhupike-en",
            status = ContentStatus.DRAFT,
            services = "Seed distribution, Agricultural training, Equipment rental",
            achievements = "Increased crop yield by 15% in 2022",
            operatingHours = "Sunday to Friday: 10 AM - 5 PM",
            seoTitle = "Agricultural Cooperative of Likhupike | Supporting Local Farmers",
            seoDescription = "Official agricultural cooperative helping local farmers with seeds, training and equipment in Likhupike",
            seoKeywords = "agriculture, cooperative, farming, Likhupike, Nepal",
            metaRobots = "index, follow",
            structuredData = null
        )

        mockMvc.perform(
            post("/api/v1/cooperatives/{cooperativeId}/translations", testCooperativeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.locale").value("en"))
            .andExpect(jsonPath("$.data.name").value("Agricultural Cooperative of Likhupike"))
            .andDo(
                document(
                    "cooperative-translation-create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("cooperativeId").description("The ID of the cooperative to add translation for")
                    ),
                    requestFields(
                        fieldWithPath("locale").description("Locale code for this translation"),
                        fieldWithPath("name").description("Localized name of the cooperative"),
                        fieldWithPath("description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("location").description("Localized location description"),
                        fieldWithPath("slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("status").description("Content status"),
                        fieldWithPath("services").description("Localized description of services offered"),
                        fieldWithPath("achievements").description("Localized description of key achievements"),
                        fieldWithPath("operatingHours").description("Localized operating hours information"),
                        fieldWithPath("seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("structuredData").optional().description("JSON-LD structured data for this language version")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Translation data"),
                        fieldWithPath("data.id").description("Unique identifier for the translation"),
                        fieldWithPath("data.locale").description("Locale for this translation"),
                        fieldWithPath("data.name").description("Localized name of the cooperative"),
                        fieldWithPath("data.description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.location").description("Localized location description"),
                        fieldWithPath("data.services").description("Localized description of services offered"),
                        fieldWithPath("data.achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.status").description("Content status"),
                        fieldWithPath("data.structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.version").description("Version tracking for content changes")
                    )
                )
            )
    }

    @Test
    fun `should update cooperative translation successfully`() {
        // First create a translation to update
        val translationId = createTestTranslation(testCooperativeId)

        val updateDto = UpdateCooperativeTranslationDto(
            name = "Updated Agricultural Cooperative",
            description = "Updated description for the cooperative",
            location = "Updated location in Ward 5",
            services = "Updated services list",
            achievements = "Updated achievements for the cooperative",
            operatingHours = "Updated operating hours",
            seoTitle = "Updated SEO title",
            seoDescription = "Updated SEO description",
            seoKeywords = "Updated SEO keywords",
            slugUrl = "updated-slug-url",
            structuredData = null,
            canonicalUrl = "https://updated-canonical-url.com",
            hreflangTags = "en,fr",
            breadcrumbStructure = null,
            faqItems = null,
            metaRobots = "index, follow",
            socialShareImage = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            status = ContentStatus.PUBLISHED
        )

        mockMvc.perform(
            put("/api/v1/cooperatives/{cooperativeId}/translations/{translationId}", testCooperativeId, translationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.name").value("Updated Agricultural Cooperative"))
            .andExpect(jsonPath("$.data.status").value("PUBLISHED"))
            .andDo(
                document(
                    "cooperative-translation-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("cooperativeId").description("The ID of the cooperative"),
                        parameterWithName("translationId").description("The ID of the translation to update")
                    ),
                    requestFields(
                        fieldWithPath("name").description("Updated name of the cooperative").optional(),
                        fieldWithPath("description").description("Updated description of the cooperative").optional(),
                        fieldWithPath("location").description("Updated location description").optional(),
                        fieldWithPath("services").description("Updated services offered").optional(),
                        fieldWithPath("achievements").description("Updated achievements").optional(),
                        fieldWithPath("operatingHours").description("Updated operating hours").optional(),
                        fieldWithPath("seoTitle").description("Updated SEO title").optional(),
                        fieldWithPath("seoDescription").description("Updated SEO description").optional(),
                        fieldWithPath("seoKeywords").description("Updated SEO keywords").optional(),
                        fieldWithPath("slugUrl").description("Updated URL slug").optional(),
                        fieldWithPath("status").description("Updated content status").optional(),
                        fieldWithPath("structuredData").description("Updated structured data").optional(),
                        fieldWithPath("canonicalUrl").description("Updated canonical URL").optional(),
                        fieldWithPath("hreflangTags").description("Updated hreflang tags").optional(),
                        fieldWithPath("breadcrumbStructure").description("Updated breadcrumb structure").optional(),
                        fieldWithPath("faqItems").description("Updated FAQ items").optional(),
                        fieldWithPath("metaRobots").description("Updated meta robots").optional(),
                        fieldWithPath("socialShareImage").description("Updated social share image").optional()
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Updated translation data"),
                        fieldWithPath("data.id").description("Unique identifier for the translation"),
                        fieldWithPath("data.locale").description("Locale for this translation"),
                        fieldWithPath("data.name").description("Localized name of the cooperative"),
                        fieldWithPath("data.description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.location").description("Localized location description"),
                        fieldWithPath("data.services").description("Localized description of services offered"),
                        fieldWithPath("data.achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.status").description("Content status"),
                        fieldWithPath("data.structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.version").description("Version tracking for content changes")
                    )
                )
            )
    }

    @Test
    fun `should get translation by id`() {
        // First create a translation to retrieve
        val translationId = createTestTranslation(testCooperativeId)

        mockMvc.perform(
            get("/api/v1/cooperatives/{cooperativeId}/translations/{translationId}", testCooperativeId, translationId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").value(translationId.toString()))
            .andDo(
                document(
                    "cooperative-translation-get-by-id",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("cooperativeId").description("The ID of the cooperative"),
                        parameterWithName("translationId").description("The ID of the translation to retrieve")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Translation data"),
                        fieldWithPath("data.id").description("Unique identifier for the translation"),
                        fieldWithPath("data.locale").description("Locale for this translation"),
                        fieldWithPath("data.name").description("Localized name of the cooperative"),
                        fieldWithPath("data.description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.location").description("Localized location description"),
                        fieldWithPath("data.services").description("Localized description of services offered"),
                        fieldWithPath("data.achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.status").description("Content status"),
                        fieldWithPath("data.structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.version").description("Version tracking for content changes")
                    )
                )
            )
    }

    @Test
    fun `should get translation by locale`() {
        // Create a translation with a specific locale
        createTestTranslation(testCooperativeId, "en")

        mockMvc.perform(
            get("/api/v1/cooperatives/{cooperativeId}/translations/locale/{locale}", testCooperativeId, "en")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.locale").value("en"))
            .andDo(
                document(
                    "cooperative-translation-get-by-locale",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("cooperativeId").description("The ID of the cooperative"),
                        parameterWithName("locale").description("The locale code of the translation to retrieve")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Translation data"),
                        fieldWithPath("data.id").description("Unique identifier for the translation"),
                        fieldWithPath("data.locale").description("Locale for this translation"),
                        fieldWithPath("data.name").description("Localized name of the cooperative"),
                        fieldWithPath("data.description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.location").description("Localized location description"),
                        fieldWithPath("data.services").description("Localized description of services offered"),
                        fieldWithPath("data.achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.status").description("Content status"),
                        fieldWithPath("data.structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.version").description("Version tracking for content changes")
                    )
                )
            )
    }

    @Test
    fun `should get all translations for cooperative`() {
        // Create a couple translations for testing
        createTestTranslation(testCooperativeId, "en")
        createTestTranslation(testCooperativeId, "zh")

        mockMvc.perform(
            get("/api/v1/cooperatives/{cooperativeId}/translations", testCooperativeId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").isArray)
            .andExpect(jsonPath("$.data.length()").value(3)) // 2 new ones + 1 from cooperative creation
            .andDo(
                document(
                    "cooperative-translation-get-all",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("cooperativeId").description("The ID of the cooperative")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("List of translations"),
                        fieldWithPath("data[].id").description("Unique identifier for the translation"),
                        fieldWithPath("data[].locale").description("Locale for this translation"),
                        fieldWithPath("data[].name").description("Localized name of the cooperative"),
                        fieldWithPath("data[].description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data[].location").description("Localized location description"),
                        fieldWithPath("data[].services").description("Localized description of services offered"),
                        fieldWithPath("data[].achievements").description("Localized description of key achievements"),
                        fieldWithPath("data[].operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data[].seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data[].seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data[].seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data[].slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data[].status").description("Content status"),
                        fieldWithPath("data[].structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data[].canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data[].hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data[].breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data[].faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data[].metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data[].socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data[].contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data[].version").description("Version tracking for content changes")
                    )
                )
            )
    }

    @Test
    fun `should delete cooperative translation`() {
        // Create a translation to delete
        val translationId = createTestTranslation(testCooperativeId)

        mockMvc.perform(
            delete("/api/v1/cooperatives/{cooperativeId}/translations/{translationId}", testCooperativeId, translationId)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "cooperative-translation-delete",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("cooperativeId").description("The ID of the cooperative"),
                        parameterWithName("translationId").description("The ID of the translation to delete")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message indicating translation was deleted")
                    )
                )
            )
    }

    @Test
    fun `should update translation status`() {
        // Create a translation to update status
        val translationId = createTestTranslation(testCooperativeId)

        mockMvc.perform(
            patch("/api/v1/cooperatives/{cooperativeId}/translations/{translationId}/status", testCooperativeId, translationId)
                .contentType(MediaType.APPLICATION_JSON)
                .param("status", "PUBLISHED")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.status").value("PUBLISHED"))
            .andDo(
                document(
                    "cooperative-translation-update-status",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("cooperativeId").description("The ID of the cooperative"),
                        parameterWithName("translationId").description("The ID of the translation to update status")
                    ),
                    queryParameters(
                        parameterWithName("status").description("The new status for the translation")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Updated translation data"),
                        fieldWithPath("data.id").description("Unique identifier for the translation"),
                        fieldWithPath("data.locale").description("Locale for this translation"),
                        fieldWithPath("data.name").description("Localized name of the cooperative"),
                        fieldWithPath("data.description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.location").description("Localized location description"),
                        fieldWithPath("data.services").description("Localized description of services offered"),
                        fieldWithPath("data.achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.status").description("Content status"),
                        fieldWithPath("data.structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.version").description("Version tracking for content changes")
                    )
                )
            )
    }
    
    @Test
    fun `should return 401 when creating translation without authentication`() {
        val createDto = CreateCooperativeTranslationDto(
            locale = "en",
            name = "Agricultural Cooperative of Likhupike",
            description = "This cooperative focuses on agricultural development in Likhupike",
            location = "Ward 5, Likhupike Rural Municipality",
            slugUrl = "agri-coop-likhupike-en",
            services = "Default services description",
            achievements = "Default achievements description",
            operatingHours = "Default operating hours",
            seoTitle = "Default SEO title",
            seoDescription = "Default SEO description",
            seoKeywords = "Default SEO keywords",
            structuredData = null,
            metaRobots = "index, follow",
            status = ContentStatus.DRAFT
        )

        mockMvc.perform(
            post("/api/v1/cooperatives/{cooperativeId}/translations", testCooperativeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return 403 when creating translation without proper permission`() {
        val createDto = CreateCooperativeTranslationDto(
            locale = "en",
            name = "Agricultural Cooperative of Likhupike",
            description = "This cooperative focuses on agricultural development in Likhupike",
            location = "Ward 5, Likhupike Rural Municipality",
            slugUrl = "agri-coop-likhupike-en",
            services = "Default services description",
            achievements = "Default achievements description",
            operatingHours = "Default operating hours",
            seoTitle = "Default SEO title",
            seoDescription = "Default SEO description",
            seoKeywords = "Default SEO keywords",
            structuredData = null,
            metaRobots = "index, follow",
            status = ContentStatus.DRAFT
        )

        mockMvc.perform(
            post("/api/v1/cooperatives/{cooperativeId}/translations", testCooperativeId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getAuthForUserWithPermission("WRONG_PERMISSION"))
                .content(objectMapper.writeValueAsString(createDto))
        )
            .andExpect(status().isForbidden)
    }

    // Helper method to create a test cooperative
    private fun createTestCooperative() = cooperativeService.createCooperative(
        CreateCooperativeDto(
            code = "test-coop",
            defaultLocale = "ne",
            establishedDate = LocalDate.of(2018, 3, 15),
            ward = 5,
            type = CooperativeType.AGRICULTURE,
            status = CooperativeStatus.ACTIVE,
            registrationNumber = "REG-2075-123",
            point = GeoPointDto(
                longitude = 85.3240,
                latitude = 27.7172
            ),
            contactEmail = "contact@test-coop.np",
            contactPhone = "+977 9812345678",
            websiteUrl = "https://test-coop.np",
            translation = CreateCooperativeTranslationDto(
                locale = "ne",
                name = "परीक्षण सहकारी",
                description = "यो एक परीक्षण सहकारी हो",
                location = "वडा नं. ५, लिखुपिके गाउँपालिका",
                slugUrl = "test-coop-ne",
                services = "Default services description",
                achievements = "Default achievements description",
                operatingHours = "Default operating hours",
                seoTitle = "Default SEO title",
                seoDescription = "Default SEO description",
                seoKeywords = "Default SEO keywords",
                structuredData = null,
                metaRobots = "index, follow",
                status = ContentStatus.PUBLISHED
            )
        )
    )
    
    // Helper method to create a test translation
    private fun createTestTranslation(cooperativeId: UUID, locale: String = "fr"): UUID {
        // Create a new translation for the cooperative
        val response = mockMvc.perform(
            post("/api/v1/cooperatives/{cooperativeId}/translations", cooperativeId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getAuthForUser())  // Use our auth helper
                .content(objectMapper.writeValueAsString(
                    CreateCooperativeTranslationDto(
                        locale = locale,
                        name = "Test Cooperative - $locale",
                        description = "Test description in $locale",
                        location = "Test location in $locale",
                        slugUrl = "test-coop-$locale",
                        services = "Default services description",
                        achievements = "Default achievements description",
                        operatingHours = "Default operating hours",
                        seoTitle = "Default SEO title",
                        seoDescription = "Default SEO description",
                        seoKeywords = "Default SEO keywords",
                        structuredData = null,
                        metaRobots = "index, follow",
                        status = ContentStatus.DRAFT
                    )
                ))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val responseContent = response.response.contentAsString
        val responseJson = objectMapper.readTree(responseContent)
        return UUID.fromString(responseJson.at("/data/id").asText())
    }
}
