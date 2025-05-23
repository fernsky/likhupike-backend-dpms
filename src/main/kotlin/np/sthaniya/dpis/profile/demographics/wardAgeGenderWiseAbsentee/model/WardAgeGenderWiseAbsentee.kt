package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseAbsentee.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * Entity representing the distribution of absentee population by age group and gender in each ward.
 *
 * This entity stores demographic data about absentee populations for statistical analysis and
 * reporting purposes. Each record represents the number of absentee people of a specific gender
 * in a specific age group in a specific ward.
 */
@Entity
@Table(
        name = "ward_age_gender_wise_absentee",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_absentee_age_gender",
                                columnNames = ["ward_number", "age_group", "gender"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_age_gender_absentee_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_age_gender_absentee_age",
                                columnList = "age_group"
                        ),
                        Index(
                                name = "idx_ward_age_gender_absentee_gender",
                                columnList = "gender"
                        )]
)
@DynamicUpdate
class WardAgeGenderWiseAbsentee : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Age group for this absentee demographic data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false, columnDefinition = "absentee_age_group")
    var ageGroup: AbsenteeAgeGroup? = null

    /**
     * Gender for this absentee demographic data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, columnDefinition = "gender")
    var gender: Gender? = null

    /**
     * The number of absentee people of the specified gender in the specified age group in the specified ward.
     * Must be a non-negative integer.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
