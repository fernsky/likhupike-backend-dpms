package np.sthaniya.dpis.profile.economics.common.model

/**
 * Enum representing different types of remittance expenses.
 *
 * These values directly correspond to the remittance_expense_type values used in the database schema.
 */
enum class RemittanceExpenseType {
    EDUCATION,
    HEALTH,
    HOUSEHOLD_USE,
    FESTIVALS,
    LOAN_PAYMENT,
    LOANED_OTHERS,
    SAVING,
    HOUSE_CONSTRUCTION,
    LAND_OWNERSHIP,
    JEWELRY_PURCHASE,
    GOODS_PURCHASE,
    BUSINESS_INVESTMENT,
    OTHER,
    UNKNOWN
}
