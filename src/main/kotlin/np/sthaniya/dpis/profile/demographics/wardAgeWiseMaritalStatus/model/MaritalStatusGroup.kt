package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model

/**
 * Enum representing different marital status categories for demographic data collection.
 *
 * These values directly correspond to the marital_status PostgreSQL enum defined in the database
 * schema.
 */
enum class MaritalStatusGroup {
    SINGLE,
    MARRIED,
    DIVORCED,
    WIDOWED,
    SEPARATED,
    NOT_STATED
}
