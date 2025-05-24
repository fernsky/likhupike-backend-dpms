package np.sthaniya.dpis.profile.economics.common.model

/**
 * Enum representing different categories of months for which income can sustain a household.
 *
 * These values directly correspond to the months_sustained enum used in the database schema.
 */
enum class MonthsSustainedType {
    UPTO_THREE_MONTHS,     // Up to 3 months of sustenance
    THREE_TO_SIX_MONTHS,   // 3-6 months of sustenance
    SIX_TO_NINE_MONTHS,    // 6-9 months of sustenance
    TWELVE_MONTHS          // Year-round sustenance
}
