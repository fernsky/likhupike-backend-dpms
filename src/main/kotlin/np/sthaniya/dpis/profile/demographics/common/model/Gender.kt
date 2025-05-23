package np.sthaniya.dpis.profile.demographics.common.model

/**
 * Enum representing different gender categories for demographic data collection.
 *
 * These values directly correspond to the gender PostgreSQL enum defined in the database schema.
 */
enum class Gender {
    /** Represents male gender */
    MALE,

    /** Represents female gender */
    FEMALE,

    /** Represents other gender */
    OTHER
}
