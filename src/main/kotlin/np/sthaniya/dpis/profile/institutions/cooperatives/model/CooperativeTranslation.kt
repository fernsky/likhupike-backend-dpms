package np.sthaniya.dpis.profile.institutions.cooperatives.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import java.time.Instant
import java.util.UUID

/**
 * Contains all localizable content for a cooperative.
 * 
 * This entity holds all text content that needs to be translated into different languages.
 */
@Entity
@Table(
    name = "cooperative_translations",
    indexes = [
        Index(name = "idx_coop_translation_coop_locale", columnList = "cooperative_id,locale", unique = true),
        Index(name = "idx_coop_translation_status", columnList = "status"),
        Index(name = "idx_coop_translation_slug_url", columnList = "slug_url")
    ]
)
class CooperativeTranslation : UuidBaseEntity() {

    /**
     * Reference to the Cooperative entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id", nullable = false)
    var cooperative: Cooperative? = null

    /**
     * Reference to the Locale entity for this translation.
     */
    @Column(name = "locale", nullable = false, length = 10)
    var locale: String? = null

    /**
     * Localized name of the cooperative.
     */
    @Column(name = "name", nullable = false)
    var name: String? = null

    /**
     * Localized detailed description of the cooperative.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null

    /**
     * Localized location description.
     */
    @Column(name = "location", columnDefinition = "TEXT")
    var location: String? = null

    /**
     * Localized description of services offered.
     */
    @Column(name = "services", columnDefinition = "TEXT")
    var services: String? = null

    /**
     * Localized description of key achievements.
     */
    @Column(name = "achievements", columnDefinition = "TEXT")
    var achievements: String? = null

    /**
     * Localized operating hours information.
     */
    @Column(name = "operating_hours")
    var operatingHours: String? = null

    /**
     * SEO-optimized title in this language.
     */
    @Column(name = "seo_title")
    var seoTitle: String? = null

    /**
     * SEO meta description in this language.
     */
    @Column(name = "seo_description", length = 500)
    var seoDescription: String? = null

    /**
     * SEO keywords in this language.
     */
    @Column(name = "seo_keywords", length = 500)
    var seoKeywords: String? = null

    /**
     * URL-friendly slug in this language.
     */
    @Column(name = "slug_url", length = 255)
    var slugUrl: String? = null

    /**
     * Content status (draft, published, archived).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: ContentStatus = ContentStatus.DRAFT

    /**
     * JSON-LD structured data specific to this language version.
     */
    @Column(name = "structured_data", columnDefinition = "TEXT")
    var structuredData: String? = null

    /**
     * Canonical URL for this language version.
     */
    @Column(name = "canonical_url")
    var canonicalUrl: String? = null

    /**
     * Array of hreflang references to other language versions.
     * Stored as JSON array.
     */
    @Column(name = "hreflang_tags", columnDefinition = "TEXT")
    var hreflangTags: String? = null

    /**
     * JSON representation of breadcrumb structure for this page.
     */
    @Column(name = "breadcrumb_structure", columnDefinition = "TEXT")
    var breadcrumbStructure: String? = null

    /**
     * Structured FAQ items for this cooperative (question/answer pairs).
     * Stored as JSON array.
     */
    @Column(name = "faq_items", columnDefinition = "TEXT")
    var faqItems: String? = null

    /**
     * Instructions for search engine crawlers (e.g., index,follow).
     */
    @Column(name = "meta_robots", length = 100)
    var metaRobots: String? = null

    /**
     * Specific image optimized for social sharing (reference to CooperativeMedia).
     */
    @Column(name = "social_share_image")
    var socialShareImage: UUID? = null

    /**
     * When this content was last reviewed for accuracy.
     */
    @Column(name = "content_last_reviewed")
    var contentLastReviewed: Instant? = null

    /**
     * Time-sensitive content with valid date ranges.
     * Stored as JSON object with start and end dates.
     */
    @Column(name = "seasonal_content", columnDefinition = "TEXT")
    var seasonalContent: String? = null

}
