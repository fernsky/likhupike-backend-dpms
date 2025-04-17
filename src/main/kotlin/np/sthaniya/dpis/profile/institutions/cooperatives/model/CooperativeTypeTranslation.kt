package np.sthaniya.dpis.profile.institutions.cooperatives.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity

/**
 * Contains translations for cooperative types.
 *
 * This entity allows for localizing the cooperative type names and descriptions
 * in multiple languages.
 */
@Entity
@Table(
    name = "cooperative_type_translations",
    indexes = [
        Index(name = "idx_coop_type_transl_unique", columnList = "cooperative_type,locale", unique = true)
    ]
)
class CooperativeTypeTranslation : UuidBaseEntity() {

    /**
     * Reference to the CooperativeType enumeration.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cooperative_type", nullable = false)
    var cooperativeType: CooperativeType? = null

    /**
     * Reference to the Locale entity.
     */
    @Column(name = "locale", nullable = false, length = 10)
    var locale: String? = null

    /**
     * Localized name of the cooperative type.
     */
    @Column(name = "name", nullable = false)
    var name: String? = null

    /**
     * Localized description of this cooperative type.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null

    /**
     * Creates a new cooperative type translation.
     *
     * @param type The cooperative type
     * @param locale The locale code
     * @param name The localized name
     * @param description The localized description
     */
    constructor(type: CooperativeType, locale: String, name: String, description: String? = null) {
        this.cooperativeType = type
        this.locale = locale
        this.name = name
        this.description = description
    }
    
    /**
     * Default constructor.
     */
    constructor()
}
