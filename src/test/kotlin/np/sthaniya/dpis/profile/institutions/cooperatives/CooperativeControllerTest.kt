package np.sthaniya.dpis.profile.institutions.cooperatives

import np.sthaniya.dpis.profile.institutions.cooperatives.base.BaseCooperativeTestSupport
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.GeoPointDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.UpdateCooperativeDto
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

class CooperativeControllerTest : BaseCooperativeTestSupport() {

    @Autowired
    private lateinit var cooperativeService: CooperativeService
    
    @BeforeEach
    fun setup() {
        // Create a user with all cooperative permissions for the tests
        setupTestUserWithAllCooperativePermissions()
    }

    @Test
    fun `should create cooperative successfully`() {
        val translation = CreateCooperativeTranslationDto(
            locale = "ne",
            name = "उपाली कृषि सहकारी संस्था लिमिटेड",
            description = "यो एक कृषि सहकारी हो",
            location = "वडा नं. ५, लिखुपिके गाउँपालिका",
            slugUrl = "upali-krishi-sahakari",
            status = ContentStatus.DRAFT,
            services = null,
            achievements = null,
            operatingHours = null,
            seoTitle = null,
            seoDescription = null,
            seoKeywords = null,
            structuredData = null,
            metaRobots = null
        )

        val request = CreateCooperativeDto(
            code = "upali-agriculture-coop",
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
            contactEmail = "contact@upali-coop.np",
            contactPhone = "+977 9812345678",
            websiteUrl = "https://upali-coop.np",
            translation = translation
        )

        mockMvc.perform(
            post("/api/v1/cooperatives")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.code").value("upali-agriculture-coop"))
            .andDo(
                document(
                    "cooperative-create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("code")
                            .description(
                                "Unique code/slug for the cooperative"
                            ),
                        fieldWithPath("defaultLocale")
                            .description(
                                "Default locale for this cooperative's content"
                            ),
                        fieldWithPath("establishedDate")
                            .description(
                                "Date when the cooperative was established"
                            ),
                        fieldWithPath("ward")
                            .description(
                                "Ward where the cooperative is located"
                            ),
                        fieldWithPath("type").description("Type of cooperative"),
                        fieldWithPath("status")
                            .description("Status of the cooperative"),
                        fieldWithPath("registrationNumber")
                            .description(
                                "Official registration number of the cooperative"
                            ),
                        fieldWithPath("point")
                            .description(
                                "Geographic point location (longitude, latitude)"
                            ),
                        fieldWithPath("point.longitude")
                            .description("Longitude coordinate"),
                        fieldWithPath("point.latitude")
                            .description("Latitude coordinate"),
                        fieldWithPath("contactEmail")
                            .description(
                                "Primary contact email for the cooperative"
                            ),
                        fieldWithPath("contactPhone")
                            .description("Primary contact phone number"),
                        fieldWithPath("websiteUrl")
                            .description("Website URL of the cooperative"),
                        fieldWithPath("translation")
                            .description(
                                "Initial translation for the cooperative"
                            ),
                        fieldWithPath("translation.locale")
                            .description("Locale for this translation"),
                        fieldWithPath("translation.name")
                            .description("Localized name of the cooperative"),
                        fieldWithPath("translation.description")
                            .description(
                                "Localized detailed description of the cooperative"
                            ),
                        fieldWithPath("translation.location")
                            .description("Localized location description"),
                        fieldWithPath("translation.slugUrl")
                            .description("URL-friendly slug in this language"),
                        fieldWithPath("translation.status")
                            .description("Content status"),
                        fieldWithPath("translation.services")
                            .optional()
                            .description(
                                "Localized description of services offered"
                            ),
                        fieldWithPath("translation.achievements")
                            .optional()
                            .description(
                                "Localized description of key achievements"
                            ),
                        fieldWithPath("translation.operatingHours")
                            .optional()
                            .description(
                                "Localized operating hours information"
                            ),
                        fieldWithPath("translation.seoTitle")
                            .optional()
                            .description(
                                "SEO-optimized title in this language"
                            ),
                        fieldWithPath("translation.seoDescription")
                            .optional()
                            .description(
                                "SEO meta description in this language"
                            ),
                        fieldWithPath("translation.seoKeywords")
                            .optional()
                            .description("SEO keywords in this language"),
                        fieldWithPath("translation.structuredData")
                            .optional()
                            .description(
                                "JSON-LD structured data for this language version"
                            ),
                        fieldWithPath("translation.metaRobots")
                            .optional()
                            .description(
                                "Instructions for search engine crawlers"
                            )
                    ),
                    responseFields(
                        fieldWithPath("success")
                            .description(
                                "Indicates if the request was successful"
                            ),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Cooperative data"),
                        fieldWithPath("data.id")
                            .description(
                                "Unique identifier for the cooperative"
                            ),
                        fieldWithPath("data.code")
                            .description(
                                "Unique code/slug for the cooperative"
                            ),
                        fieldWithPath("data.defaultLocale")
                            .description(
                                "Default locale for this cooperative's content"
                            ),
                        fieldWithPath("data.establishedDate")
                            .description(
                                "Date when the cooperative was established"
                            ),
                        fieldWithPath("data.ward")
                            .description(
                                "Ward where the cooperative is located"
                            ),
                        fieldWithPath("data.type")
                            .description("Type of cooperative"),
                        fieldWithPath("data.status")
                            .description("Status of the cooperative"),
                        fieldWithPath("data.registrationNumber")
                            .description(
                                "Official registration number of the cooperative"
                            ),
                        fieldWithPath("data.point")
                            .description(
                                "Geographic point location (longitude, latitude)"
                            ),
                        fieldWithPath("data.point.longitude")
                            .description("Longitude coordinate"),
                        fieldWithPath("data.point.latitude")
                            .description("Latitude coordinate"),
                        fieldWithPath("data.contactEmail")
                            .description(
                                "Primary contact email for the cooperative"
                            ),
                        fieldWithPath("data.contactPhone")
                            .description("Primary contact phone number"),
                        fieldWithPath("data.websiteUrl")
                            .description("Website URL of the cooperative"),
                        fieldWithPath("data.createdAt")
                            .description("When this record was created"),
                        fieldWithPath("data.updatedAt")
                            .description("When this record was last updated"),
                        fieldWithPath("data.translations")
                            .description("Translations for this cooperative"),
                        fieldWithPath("data.translations[].id")
                            .description(
                                "Unique identifier for the translation"
                            ),
                        fieldWithPath("data.translations[].locale")
                            .description("Locale for this translation"),
                        fieldWithPath("data.translations[].name")
                            .description("Localized name of the cooperative"),
                        fieldWithPath("data.translations[].description")
                            .description(
                                "Localized detailed description of the cooperative"
                            ),
                        fieldWithPath("data.translations[].location")
                            .description("Localized location description"),
                        fieldWithPath("data.translations[].services")
                            .description(
                                "Localized description of services offered"
                            ),
                        fieldWithPath("data.translations[].achievements")
                            .description(
                                "Localized description of key achievements"
                            ),
                        fieldWithPath("data.translations[].operatingHours")
                            .description(
                                "Localized operating hours information"
                            ),
                        fieldWithPath("data.translations[].seoTitle")
                            .description(
                                "SEO-optimized title in this language"
                            ),
                        fieldWithPath("data.translations[].seoDescription")
                            .description(
                                "SEO meta description in this language"
                            ),
                        fieldWithPath("data.translations[].seoKeywords")
                            .description("SEO keywords in this language"),
                        fieldWithPath("data.translations[].slugUrl")
                            .description("URL-friendly slug in this language"),
                        fieldWithPath("data.translations[].status")
                            .description("Content status"),
                        fieldWithPath("data.translations[].structuredData")
                            .description(
                                "JSON-LD structured data specific to this language version"
                            ),
                        fieldWithPath("data.translations[].canonicalUrl")
                            .description(
                                "Canonical URL for this language version"
                            ),
                        fieldWithPath("data.translations[].hreflangTags")
                            .description(
                                "Array of hreflang references to other language versions"
                            ),
                        fieldWithPath("data.translations[].breadcrumbStructure")
                            .description(
                                "JSON representation of breadcrumb structure for this page"
                            ),
                        fieldWithPath("data.translations[].faqItems")
                            .description(
                                "Structured FAQ items for this cooperative"
                            ),
                        fieldWithPath("data.translations[].metaRobots")
                            .description(
                                "Instructions for search engine crawlers"
                            ),
                        fieldWithPath("data.translations[].socialShareImage")
                            .description(
                                "Specific image optimized for social sharing"
                            ),
                        fieldWithPath("data.translations[].contentLastReviewed")
                            .description(
                                "When this content was last reviewed for accuracy"
                            ),
                        fieldWithPath("data.translations[].version")
                            .description(
                                "Version tracking for content changes"
                            ),
                        fieldWithPath("data.primaryMedia")
                            .description(
                                "Primary media items for this cooperative (one per type)"
                            )
                    )
                )
            )
    }

    @Test
    fun `should update cooperative successfully`() {
        // First create a cooperative to update
        val cooperativeId = createTestCooperative().id!!

        val request = UpdateCooperativeDto(
            code = "updated-coop-code",
            defaultLocale = "en",
            establishedDate = LocalDate.of(2020, 5, 15),
            ward = 6,
            type = CooperativeType.DAIRY,
            status = CooperativeStatus.INACTIVE,
            registrationNumber = "REG-UPD-123",
            point = GeoPointDto(
                longitude = 85.4000,
                latitude = 27.8000
            ),
            contactEmail = "updated@coop.np",
            contactPhone = "+977 9876543210",
            websiteUrl = "https://updated-coop.np"
        )

        mockMvc.perform(
            put("/api/v1/cooperatives/{id}", cooperativeId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value("updated-coop-code"))
            .andExpect(jsonPath("$.data.defaultLocale").value("en"))
            .andDo(
                document(
                    "cooperative-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id")
                            .description("The ID of the cooperative to update")
                    ),
                    requestFields(
                        fieldWithPath("code")
                            .description(
                                "Updated code/slug for the cooperative"
                            ),
                        fieldWithPath("defaultLocale")
                            .description(
                                "Updated default locale for this cooperative's content"
                            ),
                        fieldWithPath("establishedDate")
                            .description(
                                "Updated date when the cooperative was established"
                            ),
                        fieldWithPath("ward")
                            .description(
                                "Updated ward where the cooperative is located"
                            ),
                        fieldWithPath("type")
                            .description("Updated type of cooperative"),
                        fieldWithPath("status")
                            .description("Updated status of the cooperative"),
                        fieldWithPath("registrationNumber")
                            .description(
                                "Updated registration number of the cooperative"
                            ),
                        fieldWithPath("point")
                            .description("Updated geographic point location"),
                        fieldWithPath("point.longitude")
                            .description("Updated longitude coordinate"),
                        fieldWithPath("point.latitude")
                            .description("Updated latitude coordinate"),
                        fieldWithPath("contactEmail")
                            .description(
                                "Updated contact email for the cooperative"
                            ),
                        fieldWithPath("contactPhone")
                            .description("Updated contact phone number"),
                        fieldWithPath("websiteUrl")
                            .description(
                                "Updated website URL of the cooperative"
                            )
                    ),
                    responseFields(
                        fieldWithPath("success")
                            .description(
                                "Indicates if the request was successful"
                            ),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data")
                            .description("Updated cooperative data"),
                        fieldWithPath("data.id")
                            .description(
                                "Unique identifier for the cooperative"
                            ),
                        fieldWithPath("data.code")
                            .description(
                                "Updated code/slug for the cooperative"
                            ),
                        fieldWithPath("data.defaultLocale")
                            .description(
                                "Updated default locale for this cooperative's content"
                            ),
                        fieldWithPath("data.establishedDate")
                            .description(
                                "Updated date when the cooperative was established"
                            ),
                        fieldWithPath("data.ward")
                            .description(
                                "Updated ward where the cooperative is located"
                            ),
                        fieldWithPath("data.type")
                            .description("Updated type of cooperative"),
                        fieldWithPath("data.status")
                            .description("Updated status of the cooperative"),
                        fieldWithPath("data.registrationNumber")
                            .description(
                                "Updated registration number of the cooperative"
                            ),
                        fieldWithPath("data.point")
                            .description("Updated geographic point location"),
                        fieldWithPath("data.point.longitude")
                            .description("Updated longitude coordinate"),
                        fieldWithPath("data.point.latitude")
                            .description("Updated latitude coordinate"),
                        fieldWithPath("data.contactEmail")
                            .description(
                                "Updated contact email for the cooperative"
                            ),
                        fieldWithPath("data.contactPhone")
                            .description("Updated contact phone number"),
                        fieldWithPath("data.websiteUrl")
                            .description(
                                "Updated website URL of the cooperative"
                            ),
                        fieldWithPath("data.createdAt")
                            .description("When this record was created"),
                        fieldWithPath("data.updatedAt")
                            .description("When this record was last updated"),
                        fieldWithPath("data.translations")
                            .description("Translations for this cooperative"),
                        fieldWithPath("data.translations[].id")
                            .description(
                                "Unique identifier for the translation"
                            ),
                        fieldWithPath("data.translations[].locale")
                            .description("Locale for this translation"),
                        fieldWithPath("data.translations[].name")
                            .description("Localized name of the cooperative"),
                        fieldWithPath("data.translations[].description")
                            .description(
                                "Localized detailed description of the cooperative"
                            ),
                        fieldWithPath("data.translations[].location")
                            .description("Localized location description"),
                        fieldWithPath("data.translations[].services")
                            .description(
                                "Localized description of services offered"
                            ),
                        fieldWithPath("data.translations[].achievements")
                            .description(
                                "Localized description of key achievements"
                            ),
                        fieldWithPath("data.translations[].operatingHours")
                            .description(
                                "Localized operating hours information"
                            ),
                        fieldWithPath("data.translations[].seoTitle")
                            .description(
                                "SEO-optimized title in this language"
                            ),
                        fieldWithPath("data.translations[].seoDescription")
                            .description(
                                "SEO meta description in this language"
                            ),
                        fieldWithPath("data.translations[].seoKeywords")
                            .description("SEO keywords in this language"),
                        fieldWithPath("data.translations[].slugUrl")
                            .description("URL-friendly slug in this language"),
                        fieldWithPath("data.translations[].status")
                            .description("Content status"),
                        fieldWithPath("data.translations[].structuredData")
                            .description(
                                "JSON-LD structured data specific to this language version"
                            ),
                        fieldWithPath("data.translations[].canonicalUrl")
                            .description(
                                "Canonical URL for this language version"
                            ),
                        fieldWithPath("data.translations[].hreflangTags")
                            .description(
                                "Array of hreflang references to other language versions"
                            ),
                        fieldWithPath("data.translations[].breadcrumbStructure")
                            .description(
                                "JSON representation of breadcrumb structure for this page"
                            ),
                        fieldWithPath("data.translations[].faqItems")
                            .description(
                                "Structured FAQ items for this cooperative"
                            ),
                        fieldWithPath("data.translations[].metaRobots")
                            .description(
                                "Instructions for search engine crawlers"
                            ),
                        fieldWithPath("data.translations[].socialShareImage")
                            .description(
                                "Specific image optimized for social sharing"
                            ),
                        fieldWithPath("data.translations[].contentLastReviewed")
                            .description(
                                "When this content was last reviewed for accuracy"
                            ),
                        fieldWithPath("data.translations[].version")
                            .description(
                                "Version tracking for content changes"
                            ),
                        fieldWithPath("data.primaryMedia")
                            .description(
                                "Primary media items for this cooperative (one per type)"
                            )
                    )
                )
            )
    }

    @Test
    fun `should get cooperative by id`() {
        // First create a cooperative to retrieve
        val cooperativeId = createTestCooperative().id!!

        mockMvc.perform(
            get("/api/v1/cooperatives/{id}", cooperativeId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").value(cooperativeId.toString()))
            .andDo(
                document(
                    "cooperative-get-by-id",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id")
                            .description(
                                "The ID of the cooperative to retrieve"
                            )
                    ),
                    responseFields(
                        fieldWithPath("success")
                            .description(
                                "Indicates if the request was successful"
                            ),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Cooperative data"),
                        fieldWithPath("data.id")
                            .description(
                                "Unique identifier for the cooperative"
                            ),
                        fieldWithPath("data.code")
                            .description("Code/slug for the cooperative"),
                        fieldWithPath("data.defaultLocale")
                            .description(
                                "Default locale for this cooperative's content"
                            ),
                        fieldWithPath("data.establishedDate")
                            .description(
                                "Date when the cooperative was established"
                            ),
                        fieldWithPath("data.ward")
                            .description(
                                "Ward where the cooperative is located"
                            ),
                        fieldWithPath("data.type")
                            .description("Type of cooperative"),
                        fieldWithPath("data.status")
                            .description("Status of the cooperative"),
                        fieldWithPath("data.registrationNumber")
                            .description(
                                "Registration number of the cooperative"
                            ),
                        fieldWithPath("data.point")
                            .description("Geographic point location"),
                        fieldWithPath("data.point.longitude")
                            .description("Longitude coordinate"),
                        fieldWithPath("data.point.latitude")
                            .description("Latitude coordinate"),
                        fieldWithPath("data.contactEmail")
                            .description("Contact email for the cooperative"),
                        fieldWithPath("data.contactPhone")
                            .description("Contact phone number"),
                        fieldWithPath("data.websiteUrl")
                            .description("Website URL of the cooperative"),
                        fieldWithPath("data.createdAt")
                            .description("When this record was created"),
                        fieldWithPath("data.updatedAt")
                            .description("When this record was last updated"),
                        fieldWithPath("data.translations")
                            .description("Translations for this cooperative"),
                        fieldWithPath("data.translations[].id")
                            .description(
                                "Unique identifier for the translation"
                            ),
                        fieldWithPath("data.translations[].locale")
                            .description("Locale for this translation"),
                        fieldWithPath("data.translations[].name")
                            .description("Localized name of the cooperative"),
                        fieldWithPath("data.translations[].description")
                            .description(
                                "Localized detailed description of the cooperative"
                            ),
                        fieldWithPath("data.translations[].location")
                            .description("Localized location description"),
                        fieldWithPath("data.translations[].services")
                            .description(
                                "Localized description of services offered"
                            ),
                        fieldWithPath("data.translations[].achievements")
                            .description(
                                "Localized description of key achievements"
                            ),
                        fieldWithPath("data.translations[].operatingHours")
                            .description(
                                "Localized operating hours information"
                            ),
                        fieldWithPath("data.translations[].seoTitle")
                            .description(
                                "SEO-optimized title in this language"
                            ),
                        fieldWithPath("data.translations[].seoDescription")
                            .description(
                                "SEO meta description in this language"
                            ),
                        fieldWithPath("data.translations[].seoKeywords")
                            .description("SEO keywords in this language"),
                        fieldWithPath("data.translations[].slugUrl")
                            .description("URL-friendly slug in this language"),
                        fieldWithPath("data.translations[].status")
                            .description("Content status"),
                        fieldWithPath("data.translations[].structuredData")
                            .description(
                                "JSON-LD structured data specific to this language version"
                            ),
                        fieldWithPath("data.translations[].canonicalUrl")
                            .description(
                                "Canonical URL for this language version"
                            ),
                        fieldWithPath("data.translations[].hreflangTags")
                            .description(
                                "Array of hreflang references to other language versions"
                            ),
                        fieldWithPath("data.translations[].breadcrumbStructure")
                            .description(
                                "JSON representation of breadcrumb structure for this page"
                            ),
                        fieldWithPath("data.translations[].faqItems")
                            .description(
                                "Structured FAQ items for this cooperative"
                            ),
                        fieldWithPath("data.translations[].metaRobots")
                            .description(
                                "Instructions for search engine crawlers"
                            ),
                        fieldWithPath("data.translations[].socialShareImage")
                            .description(
                                "Specific image optimized for social sharing"
                            ),
                        fieldWithPath("data.translations[].contentLastReviewed")
                            .description(
                                "When this content was last reviewed for accuracy"
                            ),
                        fieldWithPath("data.translations[].version")
                            .description(
                                "Version tracking for content changes"
                            ),
                        fieldWithPath("data.primaryMedia")
                            .description(
                                "Primary media items for this cooperative (one per type)"
                            )
                    )
                )
            )
    }

    @Test
    fun `should get cooperative by code`() {
        // First create a cooperative to retrieve
        createTestCooperative()

        mockMvc.perform(
            get("/api/v1/cooperatives/code/{code}", "test-coop-code")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.code").value("test-coop-code"))
            .andDo(
                document(
                    "cooperative-get-by-code",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("code")
                            .description(
                                "The code of the cooperative to retrieve"
                            )
                    ),
                    responseFields(
                        fieldWithPath("success")
                            .description(
                                "Indicates if the request was successful"
                            ),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Cooperative data"),
                        fieldWithPath("data.id")
                            .description(
                                "Unique identifier for the cooperative"
                            ),
                        fieldWithPath("data.code")
                            .description("Code/slug for the cooperative"),
                        fieldWithPath("data.defaultLocale")
                            .description(
                                "Default locale for this cooperative's content"
                            ),
                        fieldWithPath("data.establishedDate")
                            .description(
                                "Date when the cooperative was established"
                            ),
                        fieldWithPath("data.ward")
                            .description(
                                "Ward where the cooperative is located"
                            ),
                        fieldWithPath("data.type")
                            .description("Type of cooperative"),
                        fieldWithPath("data.status")
                            .description("Status of the cooperative"),
                        fieldWithPath("data.registrationNumber")
                            .description(
                                "Registration number of the cooperative"
                            ),
                        fieldWithPath("data.point")
                            .description("Geographic point location"),
                        fieldWithPath("data.point.longitude")
                            .description("Longitude coordinate"),
                        fieldWithPath("data.point.latitude")
                            .description("Latitude coordinate"),
                        fieldWithPath("data.contactEmail")
                            .description("Contact email for the cooperative"),
                        fieldWithPath("data.contactPhone")
                            .description("Contact phone number"),
                        fieldWithPath("data.websiteUrl")
                            .description("Website URL of the cooperative"),
                        fieldWithPath("data.createdAt")
                            .description("When this record was created"),
                        fieldWithPath("data.updatedAt")
                            .description("When this record was last updated"),
                        fieldWithPath("data.translations")
                            .description("Translations for this cooperative"),
                        fieldWithPath("data.translations[].id")
                            .description(
                                "Unique identifier for the translation"
                            ),
                        fieldWithPath("data.translations[].locale")
                            .description("Locale for this translation"),
                        fieldWithPath("data.translations[].name")
                            .description("Localized name of the cooperative"),
                        fieldWithPath("data.translations[].description")
                            .description(
                                "Localized detailed description of the cooperative"
                            ),
                        fieldWithPath("data.translations[].location")
                            .description("Localized location description"),
                        fieldWithPath("data.translations[].services")
                            .description(
                                "Localized description of services offered"
                            ),
                        fieldWithPath("data.translations[].achievements")
                            .description(
                                "Localized description of key achievements"
                            ),
                        fieldWithPath("data.translations[].operatingHours")
                            .description(
                                "Localized operating hours information"
                            ),
                        fieldWithPath("data.translations[].seoTitle")
                            .description(
                                "SEO-optimized title in this language"
                            ),
                        fieldWithPath("data.translations[].seoDescription")
                            .description(
                                "SEO meta description in this language"
                            ),
                        fieldWithPath("data.translations[].seoKeywords")
                            .description("SEO keywords in this language"),
                        fieldWithPath("data.translations[].slugUrl")
                            .description("URL-friendly slug in this language"),
                        fieldWithPath("data.translations[].status")
                            .description("Content status"),
                        fieldWithPath("data.translations[].structuredData")
                            .description(
                                "JSON-LD structured data specific to this language version"
                            ),
                        fieldWithPath("data.translations[].canonicalUrl")
                            .description(
                                "Canonical URL for this language version"
                            ),
                        fieldWithPath("data.translations[].hreflangTags")
                            .description(
                                "Array of hreflang references to other language versions"
                            ),
                        fieldWithPath("data.translations[].breadcrumbStructure")
                            .description(
                                "JSON representation of breadcrumb structure for this page"
                            ),
                        fieldWithPath("data.translations[].faqItems")
                            .description(
                                "Structured FAQ items for this cooperative"
                            ),
                        fieldWithPath("data.translations[].metaRobots")
                            .description(
                                "Instructions for search engine crawlers"
                            ),
                        fieldWithPath("data.translations[].socialShareImage")
                            .description(
                                "Specific image optimized for social sharing"
                            ),
                        fieldWithPath("data.translations[].contentLastReviewed")
                            .description(
                                "When this content was last reviewed for accuracy"
                            ),
                        fieldWithPath("data.translations[].version")
                            .description(
                                "Version tracking for content changes"
                            ),
                        fieldWithPath("data.primaryMedia")
                            .description(
                                "Primary media items for this cooperative (one per type)"
                            )
                    )
                )
            )
    }

    @Test
    fun `should get all cooperatives`() {
        // First create a cooperative
        createTestCooperative()

        mockMvc.perform(
            get("/api/v1/cooperatives")
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isArray)
            .andDo(
                document(
                    "cooperative-get-all",
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("page")
                            .description("Page number (zero-based)"),
                        parameterWithName("size").description("Page size")
                    ),
                    responseFields(
                        fieldWithPath("success")
                            .description(
                                "Indicates if the request was successful"
                            ),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data.content")
                            .description("List of cooperatives"),
                        fieldWithPath("data.content[].id")
                            .description(
                                "Unique identifier for the cooperative"
                            ),
                        fieldWithPath("data.content[].code")
                            .description("Code/slug for the cooperative"),
                        fieldWithPath("data.content[].defaultLocale")
                            .description(
                                "Default locale for this cooperative's content"
                            ),
                        fieldWithPath("data.content[].establishedDate")
                            .description(
                                "Date when the cooperative was established"
                            ),
                        fieldWithPath("data.content[].ward")
                            .description(
                                "Ward where the cooperative is located"
                            ),
                        fieldWithPath("data.content[].type")
                            .description("Type of cooperative"),
                        fieldWithPath("data.content[].status")
                            .description("Status of the cooperative"),
                        fieldWithPath("data.content[].registrationNumber")
                            .description(
                                "Registration number of the cooperative"
                            ),
                        fieldWithPath("data.content[].point")
                            .description("Geographic point location"),
                        fieldWithPath("data.content[].point.longitude")
                            .description("Longitude coordinate"),
                        fieldWithPath("data.content[].point.latitude")
                            .description("Latitude coordinate"),
                        fieldWithPath("data.content[].contactEmail")
                            .description("Contact email for the cooperative"),
                        fieldWithPath("data.content[].contactPhone")
                            .description("Contact phone number"),
                        fieldWithPath("data.content[].websiteUrl")
                            .description("Website URL of the cooperative"),
                        fieldWithPath("data.content[].createdAt")
                            .description("When this record was created"),
                        fieldWithPath("data.content[].updatedAt")
                            .description("When this record was last updated"),
                        fieldWithPath("data.content[].translations")
                            .description("Translations for this cooperative"),
                        fieldWithPath("data.content[].translations[].id")
                            .description(
                                "Unique identifier for the translation"
                            ),
                        fieldWithPath("data.content[].translations[].locale")
                            .description("Locale for this translation"),
                        fieldWithPath("data.content[].translations[].name")
                            .description("Localized name of the cooperative"),
                        fieldWithPath("data.content[].translations[].description")
                            .description(
                                "Localized detailed description of the cooperative"
                            ),
                        fieldWithPath("data.content[].translations[].location")
                            .description("Localized location description"),
                        fieldWithPath("data.content[].translations[].services")
                            .description(
                                "Localized description of services offered"
                            ),
                        fieldWithPath("data.content[].translations[].achievements")
                            .description(
                                "Localized description of key achievements"
                            ),
                        fieldWithPath(
                            "data.content[].translations[].operatingHours"
                        )
                            .description(
                                "Localized operating hours information"
                            ),
                        fieldWithPath("data.content[].translations[].seoTitle")
                            .description(
                                "SEO-optimized title in this language"
                            ),
                        fieldWithPath(
                            "data.content[].translations[].seoDescription"
                        )
                            .description(
                                "SEO meta description in this language"
                            ),
                        fieldWithPath("data.content[].translations[].seoKeywords")
                            .description("SEO keywords in this language"),
                        fieldWithPath("data.content[].translations[].slugUrl")
                            .description("URL-friendly slug in this language"),
                        fieldWithPath("data.content[].translations[].status")
                            .description("Content status"),
                        fieldWithPath(
                            "data.content[].translations[].structuredData"
                        )
                            .description(
                                "JSON-LD structured data specific to this language version"
                            ),
                        fieldWithPath("data.content[].translations[].canonicalUrl")
                            .description(
                                "Canonical URL for this language version"
                            ),
                        fieldWithPath("data.content[].translations[].hreflangTags")
                            .description(
                                "Array of hreflang references to other language versions"
                            ),
                        fieldWithPath(
                            "data.content[].translations[].breadcrumbStructure"
                        )
                            .description(
                                "JSON representation of breadcrumb structure for this page"
                            ),
                        fieldWithPath("data.content[].translations[].faqItems")
                            .description(
                                "Structured FAQ items for this cooperative"
                            ),
                        fieldWithPath("data.content[].translations[].metaRobots")
                            .description(
                                "Instructions for search engine crawlers"
                            ),
                        fieldWithPath(
                            "data.content[].translations[].socialShareImage"
                        )
                            .description(
                                "Specific image optimized for social sharing"
                            ),
                        fieldWithPath(
                            "data.content[].translations[].contentLastReviewed"
                        )
                            .description(
                                "When this content was last reviewed for accuracy"
                            ),
                        fieldWithPath("data.content[].translations[].version")
                            .description(
                                "Version tracking for content changes"
                            ),
                        fieldWithPath("data.content[].primaryMedia")
                            .description(
                                "Primary media items for this cooperative (one per type)"
                            ),
                        fieldWithPath("data.pageable")
                            .description("Pagination information"),
                        fieldWithPath("data.pageable.sort")
                            .description("Sort information"),
                        fieldWithPath("data.pageable.sort.empty")
                            .description("Whether sort is empty"),
                        fieldWithPath("data.pageable.sort.sorted")
                            .description("Whether sort is sorted"),
                        fieldWithPath("data.pageable.sort.unsorted")
                            .description("Whether sort is unsorted"),
                        fieldWithPath("data.pageable.offset")
                            .description("Offset of the current page"),
                        fieldWithPath("data.pageable.pageNumber")
                            .description("Current page number"),
                        fieldWithPath("data.pageable.pageSize")
                            .description("Size of page"),
                        fieldWithPath("data.pageable.paged")
                            .description("Whether pagination is enabled"),
                        fieldWithPath("data.pageable.unpaged")
                            .description("Whether pagination is disabled"),
                        fieldWithPath("data.last")
                            .description("Whether this is the last page"),
                        fieldWithPath("data.totalElements")
                            .description("Total number of elements"),
                        fieldWithPath("data.totalPages")
                            .description("Total number of pages"),
                        fieldWithPath("data.size").description("Size of page"),
                        fieldWithPath("data.number")
                            .description("Current page number"),
                        fieldWithPath("data.sort").description("Sort information"),
                        fieldWithPath("data.sort.empty")
                            .description("Whether sort is empty"),
                        fieldWithPath("data.sort.sorted")
                            .description("Whether sort is sorted"),
                        fieldWithPath("data.sort.unsorted")
                            .description("Whether sort is unsorted"),
                        fieldWithPath("data.first")
                            .description("Whether this is the first page"),
                        fieldWithPath("data.numberOfElements")
                            .description(
                                "Number of elements in the current page"
                            ),
                        fieldWithPath("data.empty")
                            .description("Whether the page is empty")
                    )
                )
            )
    }

    @Test
    fun `should delete cooperative`() {
        // First create a cooperative to delete
        val cooperativeId = createTestCooperative().id!!

        mockMvc.perform(
            delete("/api/v1/cooperatives/{id}", cooperativeId)
                .with(getAuthForUser())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "cooperative-delete",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("id")
                            .description("The ID of the cooperative to delete")
                    ),
                    responseFields(
                        fieldWithPath("success")
                            .description(
                                "Indicates if the request was successful"
                            ),
                        fieldWithPath("message")
                            .description(
                                "Success message indicating cooperative was deleted"
                            )
                    )
                )
            )
    }



    @Test
    fun `should return 403 when creating cooperative without proper permission`() {
        // Create a user with wrong permission
        val userWithWrongPermission = createTestUserWithPermissions(
            email = "wrong-permission-user@example.com",
            permissions = listOf("WRONG_PERMISSION")
        )
        
        val translation = CreateCooperativeTranslationDto(
            locale = "ne",
            name = "उपाली कृषि सहकारी संस्था लिमिटेड",
            description = "यो एक कृषि सहकारी हो",
            location = "वडा नं. ५, लिखुपिके गाउँपालिका",
            slugUrl = "upali-krishi-sahakari",
            status = ContentStatus.DRAFT,
            services = null,
            achievements = null,
            operatingHours = null,
            seoTitle = null,
            seoDescription = null,
            seoKeywords = null,
            structuredData = null,
            metaRobots = null
        )

        val request = CreateCooperativeDto(
            code = "upali-agriculture-coop",
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
            contactEmail = "contact@upali-coop.np",
            contactPhone = "+977 9812345678",
            websiteUrl = "https://upali-coop.np",
            translation = translation
        )

        mockMvc.perform(
            post("/api/v1/cooperatives")
                .with(getAuthForUser(userWithWrongPermission))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isForbidden)
    }

    // Helper method to create a test cooperative
    private fun createTestCooperative() = cooperativeService.createCooperative(
        CreateCooperativeDto(
            code = "test-coop-code",
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
                name = "टेस्ट सहकारी संस्था लिमिटेड",
                description = "यो एक परिक्षण सहकारी हो",
                location = "वडा नं. ५, लिखुपिके गाउँपालिका",
                slugUrl = "test-sahakari",
                status = ContentStatus.DRAFT,
                services = null,
                achievements = null,
                operatingHours = null,
                seoTitle = null,
                seoDescription = null,
                seoKeywords = null,
                structuredData = null,
                metaRobots = null
            )
        )
    )
}
