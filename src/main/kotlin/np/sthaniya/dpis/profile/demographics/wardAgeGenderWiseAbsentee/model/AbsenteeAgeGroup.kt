package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model

/**
 * Enum representing different age groups for absentee population analysis.
 *
 * These values directly correspond to the absentee_age_group PostgreSQL enum defined in the database
 * schema.
 */
enum class AbsenteeAgeGroup {
    AGE_0_4,
    AGE_5_9,
    AGE_10_14,
    AGE_15_19,
    AGE_20_24,
    AGE_25_29,
    AGE_30_34,
    AGE_35_39,
    AGE_40_44,
    AGE_45_49,
    AGE_50_AND_ABOVE
}
