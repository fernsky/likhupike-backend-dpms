package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import np.sthaniya.dpis.profile.institutions.cooperatives.model.ContentStatus
import java.time.Instant
import java.util.UUID

/**
 * Response DTO for cooperative translation.
 */
@Schema(description = "Cooperative translation data")
data class CooperativeTranslationResponse(
    @Schema(description = "Unique identifier for the translation")
    val id: UUID,

    @Schema(description = "Locale for this translation", example = "ne")
    val locale: String,

    @Schema(description = "Localized name of the cooperative", example = "उपाली कृषि सहकारी संस्था लिमिटेड")
    val name: String,

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

    @Schema(description = "SEO-optimized title in this language")
    val seoTitle: String?,

    @Schema(description = "SEO meta description in this language")
    val seoDescription: String?,

    @Schema(description = "SEO keywords in this language")
    val seoKeywords: String?,

    @Schema(description = "URL-friendly slug in this language", example = "upali-krishi-sahakari")
    val slugUrl: String?,

    @Schema(description = "Content status")
    val status: ContentStatus,

    @Schema(description = "JSON-LD structured data specific to this language version")
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
    val socialShareImage: UUID?,

    @Schema(description = "When this content was last reviewed for accuracy")
    val contentLastReviewed: Instant?,

    @Schema(description = "Version tracking for content changes")
    val version: Long
)
