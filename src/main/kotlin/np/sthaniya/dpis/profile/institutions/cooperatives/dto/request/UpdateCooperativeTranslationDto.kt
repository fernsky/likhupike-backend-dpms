package np.sthaniya.dpis.profile.institutions.cooperatives.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import java.util.UUID

/**
 * DTO for updating an existing cooperative translation.
 */
@Schema(description = "Request for updating an existing cooperative translation")
data class UpdateCooperativeTranslationDto(
    @field:Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    @Schema(description = "Localized name of the cooperative", example = "उपाली कृषि सहकारी संस्था लिमिटेड")
    val name: String?,

    @Schema(description = "Localized detailed description of the cooperative")
    val description: String?,

    @Schema(description = "Localized location description", example = "वडा नं. ५, लिखुपिके गाउँपालिका")
    val location: String?,

    @Schema(description = "Localized description of services offered")
    val services: String?,

    @Schema(description = "Localized description of key achievements")
    val achievements: String?,

    @Schema(description = "Localized operating hours information", example = "आइतबार - शुक्रबार: ९ बजे - ५ बजे")
    val operatingHours: String?,

    @field:Size(max = 255, message = "SEO title must be less than 255 characters")
    @Schema(description = "SEO-optimized title in this language")
    val seoTitle: String?,

    @field:Size(max = 500, message = "SEO description must be less than 500 characters")
    @Schema(description = "SEO meta description in this language")
    val seoDescription: String?,

    @field:Size(max = 500, message = "SEO keywords must be less than 500 characters")
    @Schema(description = "SEO keywords in this language")
    val seoKeywords: String?,

    @field:Pattern(regexp = "^[a-z0-9-]+$", message = "Slug URL must contain only lowercase letters, numbers, and hyphens")
    @field:Size(min = 3, max = 255, message = "Slug URL must be between 3 and 255 characters")
    @Schema(description = "URL-friendly slug in this language", example = "upali-krishi-sahakari")
    val slugUrl: String?,

    @Schema(description = "Content status")
    val status: ContentStatus?,

    @Schema(description = "JSON-LD structured data for this language version")
    val structuredData: String?,

    @Schema(description = "Canonical URL for this language version")
    val canonicalUrl: String?,

    @Schema(description = "Array of hreflang references to other language versions")
    val hreflangTags: String?,

    @Schema(description = "JSON representation of breadcrumb structure for this page")
    val breadcrumbStructure: String?,

    @Schema(description = "Structured FAQ items for this cooperative")
    val faqItems: String?,

    @Schema(description = "Instructions for search engine crawlers", example = "index,follow")
    val metaRobots: String?,

    @Schema(description = "Specific image optimized for social sharing")
    val socialShareImage: UUID?
)
