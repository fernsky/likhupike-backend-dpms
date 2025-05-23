package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model

/**
 * Enum representing different age groups at which marriage occurred.
 *
 * These values directly correspond to the married_age_group PostgreSQL enum defined in the database
 * schema.
 */
enum class MarriedAgeGroup {
    AGE_BELOW_15,
    AGE_15_19,
    AGE_20_24,
    AGE_25_29,
    AGE_30_34,
    AGE_35_39,
    AGE_40_AND_ABOVE
}
