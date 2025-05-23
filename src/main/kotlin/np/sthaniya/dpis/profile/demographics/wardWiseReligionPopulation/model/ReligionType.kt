package np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model

/**
 * Enum representing different religion types for demographic data collection.
 *
 * These values directly correspond to the religion_type PostgreSQL enum defined in the database
 * schema.
 */
enum class ReligionType {
    /** Represents the Hindu religion */
    HINDU,

    /** Represents the Buddhist religion */
    BUDDHIST,

    /** Represents the Kirant religion */
    KIRANT,

    /** Represents the Christian religion */
    CHRISTIAN,

    /** Represents the Islam religion */
    ISLAM,

    /** Represents Nature worship */
    NATURE,

    /** Represents the Bon religion */
    BON,

    /** Represents the Jain religion */
    JAIN,

    /** Represents the Bahai religion */
    BAHAI,

    /** Represents the Sikh religion */
    SIKH,

    /** Represents other religions not explicitly listed */
    OTHER
}
