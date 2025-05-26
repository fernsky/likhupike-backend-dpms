package np.sthaniya.dpis.profile.economics.common.model

/**
 * Enum representing different types of occupations for economic data collection.
 *
 * These values directly correspond to the occupation_type values used in the database schema.
 */
enum class OccupationType {
    GOVERNMENTAL_JOB,
    NON_GOVERNMENTAL_JOB,
    LABOUR,
    FOREIGN_EMPLOYMENT,
    BUSINESS,
    OTHER_EMPLOYMENT,
    STUDENT,
    HOUSEHOLDER,
    OTHER_UNEMPLOYMENT,
    INDUSTRY,
    ANIMAL_HUSBANDRY,
    OTHER_SELF_EMPLOYMENT
}
