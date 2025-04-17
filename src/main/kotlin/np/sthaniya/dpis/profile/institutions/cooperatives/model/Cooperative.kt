package np.sthaniya.dpis.profile.institutions.cooperatives.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon
import java.time.LocalDate
import java.time.Instant
import java.util.UUID

/**
 * Represents cooperatives within the municipality.
 *
 * This entity contains the core, non-localizable data about a cooperative.
 * All translatable content is stored in [CooperativeTranslation].
 */
@Entity
@Table(
    name = "cooperatives",
    indexes = [
        Index(name = "idx_cooperative_code", columnList = "code", unique = true),
        Index(name = "idx_cooperative_ward", columnList = "ward"),
        Index(name = "idx_cooperative_status", columnList = "status")
    ]
)
class Cooperative : UuidBaseEntity() {

    /**
     * Unique code/slug for the cooperative (used in URLs).
     * This should be URL-safe and used for generating permalinks.
     */
    @Column(name = "code", nullable = false, unique = true)
    var code: String? = null

    /**
     * Default locale for this cooperative's content (reference to Locale).
     * This specifies which translation should be used when no specific locale is requested.
     */
    @Column(name = "default_locale", nullable = false)
    var defaultLocale: String = "ne"  // Default to Nepali

    /**
     * Date when the cooperative was established.
     */
    @Column(name = "established_date")
    var establishedDate: LocalDate? = null

    /**
     * Ward where the cooperative is located.
     */
    @Column(name = "ward", nullable = false)
    var ward: Int? = null

    /**
     * Type of cooperative (reference to CooperativeType).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: CooperativeType? = null

    /**
     * Status of the cooperative (active, inactive, etc.).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: CooperativeStatus = CooperativeStatus.ACTIVE

    /**
     * Official registration number of the cooperative.
     */
    @Column(name = "registration_number")
    var registrationNumber: String? = null

    /**
     * Geographic point location (longitude, latitude).
     * Stored using PostGIS geometry type.
     */
    @Column(name = "point", columnDefinition = "geometry(Point,4326)")
    var point: Point? = null

    /**
     * Geographic boundary of the cooperative.
     * Stored using PostGIS geometry type.
     */
    @Column(name = "polygon", columnDefinition = "geometry(Polygon,4326)")
    var polygon: Polygon? = null

    /**
     * Primary contact email for the cooperative.
     */
    @Column(name = "contact_email")
    var contactEmail: String? = null

    /**
     * Primary contact phone number.
     */
    @Column(name = "contact_phone")
    var contactPhone: String? = null

    /**
     * Website URL of the cooperative (if available).
     */
    @Column(name = "website_url")
    var websiteUrl: String? = null
    
    /**
     * Translations for this cooperative.
     */
    @OneToMany(mappedBy = "cooperative", cascade = [CascadeType.ALL], orphanRemoval = true)
    var translations: MutableSet<CooperativeTranslation> = mutableSetOf()

    /**
     * Media items for this cooperative.
     */
    @OneToMany(mappedBy = "cooperative", cascade = [CascadeType.ALL], orphanRemoval = true)
    var media: MutableSet<CooperativeMedia> = mutableSetOf()

    /**
     * Adds a translation to this cooperative.
     * If a translation for the given locale already exists, it will be replaced.
     *
     * @param translation The translation to add
     * @return The added or updated translation
     */
    fun addTranslation(translation: CooperativeTranslation): CooperativeTranslation {
        // Remove existing translation for this locale if present
        translations.removeIf { it.locale == translation.locale }
        
        // Set the cooperative reference and add to collection
        translation.cooperative = this
        translations.add(translation)
        return translation
    }

    /**
     * Adds a media item to this cooperative.
     * 
     * @param media The media item to add
     * @return The added media item
     */
    fun addMedia(media: CooperativeMedia): CooperativeMedia {
        media.cooperative = this
        this.media.add(media)
        return media
    }

    /**
     * Gets the translation for the specified locale, or the default locale if not found.
     *
     * @param locale Desired locale code
     * @return The translation for the requested locale or default locale
     */
    fun getTranslation(locale: String): CooperativeTranslation? {
        return translations.find { it.locale == locale } 
            ?: translations.find { it.locale == defaultLocale }
    }
}
