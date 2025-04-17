package np.sthaniya.dpis.profile.institutions.cooperatives

import np.sthaniya.dpis.auth.controller.base.BaseRestDocsTest
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.CreateCooperativeTranslationDto
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.request.GeoPointDto
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

class CooperativeSearchControllerTest : BaseRestDocsTest() {

    @Autowired
    private lateinit var cooperativeService: CooperativeService

    @BeforeEach
    fun setup() {
        // Create test cooperatives
        createTestCooperative(
            "dairy-coop-1",
            "Dairy Cooperative 1",
            CooperativeType.DAIRY,
            5
        )
        
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
    @WithMockUser(authorities = ["PERMISSION_VIEW_COOPERATIVE"])
    fun `should search cooperatives by name`() {
        mockMvc.perform(
            get("/api/v1/cooperatives/search/by-name")
                .param("nameQuery", "Dairy")
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
                    "cooperative-search-by-name",
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("nameQuery").description("Search query for cooperative name"),
                        parameterWithName("page").description("Page number (zero-based)"),
                        parameterWithName("size").description("Page size")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data.content").description("List of cooperatives matching the search"),
                        fieldWithPath("data.content[].id").description("Unique identifier for the cooperative"),
                        fieldWithPath("data.content[].code").description("Code/slug for the cooperative"),
                        fieldWithPath("data.content[].defaultLocale").description("Default locale for this cooperative's content"),
                        fieldWithPath("data.content[].establishedDate").description("Date when the cooperative was established"),
                        fieldWithPath("data.content[].ward").description("Ward where the cooperative is located"),
                        fieldWithPath("data.content[].type").description("Type of cooperative"),
                        fieldWithPath("data.content[].status").description("Status of the cooperative"),
                        fieldWithPath("data.content[].registrationNumber").description("Registration number of the cooperative"),
                        fieldWithPath("data.content[].point").description("Geographic point location"),
                        fieldWithPath("data.content[].point.longitude").description("Longitude coordinate"),
                        fieldWithPath("data.content[].point.latitude").description("Latitude coordinate"),
                        fieldWithPath("data.content[].contactEmail").description("Contact email for the cooperative"),
                        fieldWithPath("data.content[].contactPhone").description("Contact phone number"),
                        fieldWithPath("data.content[].websiteUrl").description("Website URL of the cooperative"),
                        fieldWithPath("data.content[].createdAt").description("When this record was created"),
                        fieldWithPath("data.content[].updatedAt").description("When this record was last updated"),
                        fieldWithPath("data.content[].translations").description("Translations for this cooperative"),
                        fieldWithPath("data.content[].translations[].id").description("Unique identifier for the translation"),
                        fieldWithPath("data.content[].translations[].locale").description("Locale for this translation"),
                        fieldWithPath("data.content[].translations[].name").description("Localized name of the cooperative"),
                        fieldWithPath("data.content[].translations[].description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.content[].translations[].location").description("Localized location description"),
                        fieldWithPath("data.content[].translations[].services").description("Localized description of services offered"),
                        fieldWithPath("data.content[].translations[].achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.content[].translations[].operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.content[].translations[].seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.content[].translations[].seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.content[].translations[].seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.content[].translations[].slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.content[].translations[].status").description("Content status"),
                        fieldWithPath("data.content[].translations[].structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.content[].translations[].canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.content[].translations[].hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.content[].translations[].breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.content[].translations[].faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.content[].translations[].metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.content[].translations[].socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.content[].translations[].contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.content[].translations[].version").description("Version tracking for content changes"),
                        fieldWithPath("data.content[].primaryMedia").description("Primary media items for this cooperative (one per type)"),
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
    @WithMockUser(authorities = ["PERMISSION_VIEW_COOPERATIVE"])
    fun `should get cooperatives by type`() {
        mockMvc.perform(
            get("/api/v1/cooperatives/search/by-type/{type}", "DAIRY")
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
                    "cooperative-get-by-type",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("type").description("Type of cooperative to filter by")
                    ),
                    queryParameters(
                        parameterWithName("page").description("Page number (zero-based)"),
                        parameterWithName("size").description("Page size")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data.content").description("List of cooperatives of the specified type"),
                        fieldWithPath("data.content[].id").description("Unique identifier for the cooperative"),
                        fieldWithPath("data.content[].code").description("Code/slug for the cooperative"),
                        fieldWithPath("data.content[].defaultLocale").description("Default locale for this cooperative's content"),
                        fieldWithPath("data.content[].establishedDate").description("Date when the cooperative was established"),
                        fieldWithPath("data.content[].ward").description("Ward where the cooperative is located"),
                        fieldWithPath("data.content[].type").description("Type of cooperative"),
                        fieldWithPath("data.content[].status").description("Status of the cooperative"),
                        fieldWithPath("data.content[].registrationNumber").description("Registration number of the cooperative"),
                        fieldWithPath("data.content[].point").description("Geographic point location"),
                        fieldWithPath("data.content[].point.longitude").description("Longitude coordinate"),
                        fieldWithPath("data.content[].point.latitude").description("Latitude coordinate"),
                        fieldWithPath("data.content[].contactEmail").description("Contact email for the cooperative"),
                        fieldWithPath("data.content[].contactPhone").description("Contact phone number"),
                        fieldWithPath("data.content[].websiteUrl").description("Website URL of the cooperative"),
                        fieldWithPath("data.content[].createdAt").description("When this record was created"),
                        fieldWithPath("data.content[].updatedAt").description("When this record was last updated"),
                        fieldWithPath("data.content[].translations").description("Translations for this cooperative"),
                        fieldWithPath("data.content[].translations[].id").description("Unique identifier for the translation"),
                        fieldWithPath("data.content[].translations[].locale").description("Locale for this translation"),
                        fieldWithPath("data.content[].translations[].name").description("Localized name of the cooperative"),
                        fieldWithPath("data.content[].translations[].description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.content[].translations[].location").description("Localized location description"),
                        fieldWithPath("data.content[].translations[].services").description("Localized description of services offered"),
                        fieldWithPath("data.content[].translations[].achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.content[].translations[].operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.content[].translations[].seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.content[].translations[].seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.content[].translations[].seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.content[].translations[].slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.content[].translations[].status").description("Content status"),
                        fieldWithPath("data.content[].translations[].structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.content[].translations[].canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.content[].translations[].hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.content[].translations[].breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.content[].translations[].faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.content[].translations[].metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.content[].translations[].socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.content[].translations[].contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.content[].translations[].version").description("Version tracking for content changes"),
                        fieldWithPath("data.content[].primaryMedia").description("Primary media items for this cooperative (one per type)"),
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
    @WithMockUser(authorities = ["PERMISSION_VIEW_COOPERATIVE"])
    fun `should get cooperatives by status`() {
        mockMvc.perform(
            get("/api/v1/cooperatives/search/by-status/{status}", "INACTIVE")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isArray)
            .andExpect(jsonPath("$.data.content.length()").value(1))
            .andDo(
                document(
                    "cooperative-get-by-status",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("status").description("Status of cooperatives to filter by")
                    ),
                    queryParameters(
                        parameterWithName("page").description("Page number (zero-based)"),
                        parameterWithName("size").description("Page size")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data.content").description("List of cooperatives with the specified status"),
                        fieldWithPath("data.content[].id").description("Unique identifier for the cooperative"),
                        fieldWithPath("data.content[].code").description("Code/slug for the cooperative"),
                        fieldWithPath("data.content[].defaultLocale").description("Default locale for this cooperative's content"),
                        fieldWithPath("data.content[].establishedDate").description("Date when the cooperative was established"),
                        fieldWithPath("data.content[].ward").description("Ward where the cooperative is located"),
                        fieldWithPath("data.content[].type").description("Type of cooperative"),
                        fieldWithPath("data.content[].status").description("Status of the cooperative"),
                        fieldWithPath("data.content[].registrationNumber").description("Registration number of the cooperative"),
                        fieldWithPath("data.content[].point").description("Geographic point location"),
                        fieldWithPath("data.content[].point.longitude").description("Longitude coordinate"),
                        fieldWithPath("data.content[].point.latitude").description("Latitude coordinate"),
                        fieldWithPath("data.content[].contactEmail").description("Contact email for the cooperative"),
                        fieldWithPath("data.content[].contactPhone").description("Contact phone number"),
                        fieldWithPath("data.content[].websiteUrl").description("Website URL of the cooperative"),
                        fieldWithPath("data.content[].createdAt").description("When this record was created"),
                        fieldWithPath("data.content[].updatedAt").description("When this record was last updated"),
                        fieldWithPath("data.content[].translations").description("Translations for this cooperative"),
                        fieldWithPath("data.content[].translations[].id").description("Unique identifier for the translation"),
                        fieldWithPath("data.content[].translations[].locale").description("Locale for this translation"),
                        fieldWithPath("data.content[].translations[].name").description("Localized name of the cooperative"),
                        fieldWithPath("data.content[].translations[].description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.content[].translations[].location").description("Localized location description"),
                        fieldWithPath("data.content[].translations[].services").description("Localized description of services offered"),
                        fieldWithPath("data.content[].translations[].achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.content[].translations[].operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.content[].translations[].seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.content[].translations[].seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.content[].translations[].seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.content[].translations[].slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.content[].translations[].status").description("Content status"),
                        fieldWithPath("data.content[].translations[].structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.content[].translations[].canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.content[].translations[].hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.content[].translations[].breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.content[].translations[].faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.content[].translations[].metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.content[].translations[].socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.content[].translations[].contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.content[].translations[].version").description("Version tracking for content changes"),
                        fieldWithPath("data.content[].primaryMedia").description("Primary media items for this cooperative (one per type)"),
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
    @WithMockUser(authorities = ["PERMISSION_VIEW_COOPERATIVE"])
    fun `should get cooperatives by ward`() {
        mockMvc.perform(
            get("/api/v1/cooperatives/search/by-ward/{ward}", 5)
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
                    "cooperative-get-by-ward",
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("ward").description("Ward number to filter by")
                    ),
                    queryParameters(
                        parameterWithName("page").description("Page number (zero-based)"),
                        parameterWithName("size").description("Page size")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data.content").description("List of cooperatives with the specified status"),
                        fieldWithPath("data.content[].id").description("Unique identifier for the cooperative"),
                        fieldWithPath("data.content[].code").description("Code/slug for the cooperative"),
                        fieldWithPath("data.content[].defaultLocale").description("Default locale for this cooperative's content"),
                        fieldWithPath("data.content[].establishedDate").description("Date when the cooperative was established"),
                        fieldWithPath("data.content[].ward").description("Ward where the cooperative is located"),
                        fieldWithPath("data.content[].type").description("Type of cooperative"),
                        fieldWithPath("data.content[].status").description("Status of the cooperative"),
                        fieldWithPath("data.content[].registrationNumber").description("Registration number of the cooperative"),
                        fieldWithPath("data.content[].point").description("Geographic point location"),
                        fieldWithPath("data.content[].point.longitude").description("Longitude coordinate"),
                        fieldWithPath("data.content[].point.latitude").description("Latitude coordinate"),
                        fieldWithPath("data.content[].contactEmail").description("Contact email for the cooperative"),
                        fieldWithPath("data.content[].contactPhone").description("Contact phone number"),
                        fieldWithPath("data.content[].websiteUrl").description("Website URL of the cooperative"),
                        fieldWithPath("data.content[].createdAt").description("When this record was created"),
                        fieldWithPath("data.content[].updatedAt").description("When this record was last updated"),
                        fieldWithPath("data.content[].translations").description("Translations for this cooperative"),
                        fieldWithPath("data.content[].translations[].id").description("Unique identifier for the translation"),
                        fieldWithPath("data.content[].translations[].locale").description("Locale for this translation"),
                        fieldWithPath("data.content[].translations[].name").description("Localized name of the cooperative"),
                        fieldWithPath("data.content[].translations[].description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.content[].translations[].location").description("Localized location description"),
                        fieldWithPath("data.content[].translations[].services").description("Localized description of services offered"),
                        fieldWithPath("data.content[].translations[].achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.content[].translations[].operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.content[].translations[].seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.content[].translations[].seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.content[].translations[].seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.content[].translations[].slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.content[].translations[].status").description("Content status"),
                        fieldWithPath("data.content[].translations[].structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.content[].translations[].canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.content[].translations[].hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.content[].translations[].breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.content[].translations[].faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.content[].translations[].metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.content[].translations[].socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.content[].translations[].contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.content[].translations[].version").description("Version tracking for content changes"),
                        fieldWithPath("data.content[].primaryMedia").description("Primary media items for this cooperative (one per type)"),
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
    @WithMockUser(authorities = ["PERMISSION_VIEW_COOPERATIVE"])
    fun `should find cooperatives near location`() {
        // This test simulates the geographic search functionality
        // In a real test, we need actual geo data in the database with spatial indexes
        mockMvc.perform(
            get("/api/v1/cooperatives/search/near")
                .param("longitude", "85.3240")
                .param("latitude", "27.7172")
                .param("distanceInMeters", "5000")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andDo(
                document(
                    "cooperative-find-near",
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("longitude").description("Longitude coordinate of the center point"),
                        parameterWithName("latitude").description("Latitude coordinate of the center point"),
                        parameterWithName("distanceInMeters").description("Radius in meters to search within"),
                        parameterWithName("page").description("Page number (zero-based)"),
                        parameterWithName("size").description("Page size")
                    ),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data.content").description("List of cooperatives with the specified status"),
                        fieldWithPath("data.content[].id").description("Unique identifier for the cooperative"),
                        fieldWithPath("data.content[].code").description("Code/slug for the cooperative"),
                        fieldWithPath("data.content[].defaultLocale").description("Default locale for this cooperative's content"),
                        fieldWithPath("data.content[].establishedDate").description("Date when the cooperative was established"),
                        fieldWithPath("data.content[].ward").description("Ward where the cooperative is located"),
                        fieldWithPath("data.content[].type").description("Type of cooperative"),
                        fieldWithPath("data.content[].status").description("Status of the cooperative"),
                        fieldWithPath("data.content[].registrationNumber").description("Registration number of the cooperative"),
                        fieldWithPath("data.content[].point").description("Geographic point location"),
                        fieldWithPath("data.content[].point.longitude").description("Longitude coordinate"),
                        fieldWithPath("data.content[].point.latitude").description("Latitude coordinate"),
                        fieldWithPath("data.content[].contactEmail").description("Contact email for the cooperative"),
                        fieldWithPath("data.content[].contactPhone").description("Contact phone number"),
                        fieldWithPath("data.content[].websiteUrl").description("Website URL of the cooperative"),
                        fieldWithPath("data.content[].createdAt").description("When this record was created"),
                        fieldWithPath("data.content[].updatedAt").description("When this record was last updated"),
                        fieldWithPath("data.content[].translations").description("Translations for this cooperative"),
                        fieldWithPath("data.content[].translations[].id").description("Unique identifier for the translation"),
                        fieldWithPath("data.content[].translations[].locale").description("Locale for this translation"),
                        fieldWithPath("data.content[].translations[].name").description("Localized name of the cooperative"),
                        fieldWithPath("data.content[].translations[].description").description("Localized detailed description of the cooperative"),
                        fieldWithPath("data.content[].translations[].location").description("Localized location description"),
                        fieldWithPath("data.content[].translations[].services").description("Localized description of services offered"),
                        fieldWithPath("data.content[].translations[].achievements").description("Localized description of key achievements"),
                        fieldWithPath("data.content[].translations[].operatingHours").description("Localized operating hours information"),
                        fieldWithPath("data.content[].translations[].seoTitle").description("SEO-optimized title in this language"),
                        fieldWithPath("data.content[].translations[].seoDescription").description("SEO meta description in this language"),
                        fieldWithPath("data.content[].translations[].seoKeywords").description("SEO keywords in this language"),
                        fieldWithPath("data.content[].translations[].slugUrl").description("URL-friendly slug in this language"),
                        fieldWithPath("data.content[].translations[].status").description("Content status"),
                        fieldWithPath("data.content[].translations[].structuredData").description("JSON-LD structured data specific to this language version"),
                        fieldWithPath("data.content[].translations[].canonicalUrl").description("Canonical URL for this language version"),
                        fieldWithPath("data.content[].translations[].hreflangTags").description("Array of hreflang references to other language versions"),
                        fieldWithPath("data.content[].translations[].breadcrumbStructure").description("JSON representation of breadcrumb structure for this page"),
                        fieldWithPath("data.content[].translations[].faqItems").description("Structured FAQ items for this cooperative"),
                        fieldWithPath("data.content[].translations[].metaRobots").description("Instructions for search engine crawlers"),
                        fieldWithPath("data.content[].translations[].socialShareImage").description("Specific image optimized for social sharing"),
                        fieldWithPath("data.content[].translations[].contentLastReviewed").description("When this content was last reviewed for accuracy"),
                        fieldWithPath("data.content[].translations[].version").description("Version tracking for content changes"),
                        fieldWithPath("data.content[].primaryMedia").description("Primary media items for this cooperative (one per type)"),
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
    @WithMockUser(authorities = ["PERMISSION_VIEW_COOPERATIVE"])
    fun `should get cooperative statistics by type`() {
        mockMvc.perform(
            get("/api/v1/cooperatives/search/statistics/by-type")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.DAIRY").exists())
            .andExpect(jsonPath("$.data.AGRICULTURE").exists())
            .andDo(
                document(
                    "cooperative-statistics-by-type",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Map of cooperative types to counts"),
                        fieldWithPath("data.DAIRY").description("Count of dairy cooperatives").optional(),
                        fieldWithPath("data.AGRICULTURE").description("Count of agriculture cooperatives").optional(),
                        fieldWithPath("data.SAVINGS_AND_CREDIT").description("Count of savings and credit cooperatives").optional(),
                        fieldWithPath("data.MULTIPURPOSE").description("Count of multipurpose cooperatives").optional()
                        // Note: Only the types present in the test data will be in the response
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_VIEW_COOPERATIVE"])
    fun `should get cooperative statistics by ward`() {
        mockMvc.perform(
            get("/api/v1/cooperatives/search/statistics/by-ward")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.5").exists())
            .andExpect(jsonPath("$.data.6").exists())
            .andDo(
                document(
                    "cooperative-statistics-by-ward",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").description("Indicates if the request was successful"),
                        fieldWithPath("message").description("Success message"),
                        fieldWithPath("data").description("Map of ward numbers to counts"),
                        fieldWithPath("data.5").description("Count of cooperatives in ward 5").optional(),
                        fieldWithPath("data.6").description("Count of cooperatives in ward 6").optional()
                        // Note: Only the wards present in the test data will be in the response
                    )
                )
            )
    }

    @Test
    @WithMockUser(authorities = ["WRONG_PERMISSION"])
    fun `should return 403 when searching without proper permission`() {
        mockMvc.perform(
            get("/api/v1/cooperatives/search/by-name")
                .param("nameQuery", "Dairy")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `should return 401 when searching without authentication`() {
        mockMvc.perform(
            get("/api/v1/cooperatives/search/by-name")
                .param("nameQuery", "Dairy")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized)
    }

    // Helper methods

    private fun createTestCooperative(
        code: String,
        name: String,
        type: CooperativeType,
        ward: Int,
        status: CooperativeStatus = CooperativeStatus.ACTIVE
    ) = cooperativeService.createCooperative(
        CreateCooperativeDto(
            code = code,
            defaultLocale = "ne",
            establishedDate = LocalDate.of(2018, 3, 15),
            ward = ward,
            type = type,
            status = status,
            registrationNumber = "REG-$code",
            point = GeoPointDto(
                longitude = 85.3240,
                latitude = 27.7172
            ),
            contactEmail = "contact@$code.np",
            contactPhone = "+977 9812345678",
            websiteUrl = "https://$code.np",
            translation = CreateCooperativeTranslationDto(
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

    private fun getCooperativeResponseFields(): Array<org.springframework.restdocs.payload.FieldDescriptor> {
        return arrayOf(
            fieldWithPath("id").description("Unique identifier for the cooperative"),
            fieldWithPath("code").description("Code/slug for the cooperative"),
            fieldWithPath("defaultLocale").description("Default locale for this cooperative's content"),
            fieldWithPath("establishedDate").description("Date when the cooperative was established"),
            fieldWithPath("ward").description("Ward where the cooperative is located"),
            fieldWithPath("type").description("Type of cooperative"),
            fieldWithPath("status").description("Status of the cooperative"),
            fieldWithPath("registrationNumber").description("Registration number of the cooperative"),
            fieldWithPath("point").description("Geographic point location"),
            fieldWithPath("point.longitude").description("Longitude coordinate"),
            fieldWithPath("point.latitude").description("Latitude coordinate"),
            fieldWithPath("contactEmail").description("Contact email for the cooperative"),
            fieldWithPath("contactPhone").description("Contact phone number"),
            fieldWithPath("websiteUrl").description("Website URL of the cooperative"),
            fieldWithPath("createdAt").description("When this record was created"),
            fieldWithPath("updatedAt").description("When this record was last updated"),
            fieldWithPath("translations").description("Translations for this cooperative"),
            fieldWithPath("primaryMedia").description("Primary media items for this cooperative (one per type)")
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
            fieldWithPath("numberOfElements").description("Number of elements in the current page"),
            fieldWithPath("empty").description("Whether the page is empty")
        )
    }
}
