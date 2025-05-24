package np.sthaniya.dpis.profile.economics.common.model

/**
 * Enum representing different categories of time spent on household chores.
 *
 * These values directly correspond to the time_spent enum used in the database schema.
 */
enum class TimeSpentType {
    LESS_THAN_1_HOUR,
    HOURS_1_TO_3,
    HOURS_4_TO_6,
    HOURS_7_TO_9,
    HOURS_10_TO_12,
    MORE_THAN_12_HOURS
}
