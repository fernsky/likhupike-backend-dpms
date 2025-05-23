package np.sthaniya.dpis.profile.demographics.common.model

/**
 * Enum representing different educational levels for demographic data collection.
 *
 * These values directly correspond to the educational_level PostgreSQL enum defined in the database
 * schema.
 */
enum class EducationalLevelType {
    CHILD_DEVELOPMENT_CENTER,
    NURSERY,
    CLASS_1,
    CLASS_2,
    CLASS_3,
    CLASS_4,
    CLASS_5,
    CLASS_6,
    CLASS_7,
    CLASS_8,
    CLASS_9,
    CLASS_10,
    SLC_LEVEL,
    CLASS_12_LEVEL,
    BACHELOR_LEVEL,
    MASTERS_LEVEL,
    PHD_LEVEL,
    OTHER,
    INFORMAL_EDUCATION,
    EDUCATED,
    UNKNOWN
}
