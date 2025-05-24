package np.sthaniya.dpis.profile.economics.common.model

/**
 * Enum representing different types of household income sources.
 *
 * These values directly correspond to the income_source_type values used in the database schema.
 */
enum class IncomeSourceType {
    JOB,
    AGRICULTURE,
    BUSINESS,
    INDUSTRY,
    FOREIGN_EMPLOYMENT,
    LABOUR,
    OTHER
}
