package np.sthaniya.dpis.profile.economics.common.model

/**
 * Enum representing different purposes for which loans are used.
 *
 * These values directly correspond to the loan_use_type values used in the database schema.
 */
enum class LoanUseType {
    AGRICULTURE,
    BUSINESS,
    HOUSEHOLD_EXPENSES,
    FOREIGN_EMPLOYMENT,
    EDUCATION,
    HEALTH_TREATMENT,
    HOME_CONSTRUCTION,
    VEHICLE_PURCHASE,
    CEREMONY,
    OTHER
}
