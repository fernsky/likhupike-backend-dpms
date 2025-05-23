package np.sthaniya.dpis.profile.demographics.wardAgeGenderWiseMarriedAge.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate
import np.sthaniya.dpis.profile.demographics.common.model.Gender

/**
 * Entity representing the distribution of married population by age at marriage and gender in each ward.
 *
 * This entity stores demographic data about the age at which people got married, broken down by gender,
 * for statistical analysis and reporting purposes. Each record represents the number of people of a specific
 * gender who got married at a specific age in a specific ward.
 */
@Entity
@Table(
        name = "ward_age_gender_wise_married_age",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_married_age_gender",
                                columnNames = ["ward_number", "age_group", "gender"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_age_gender_married_age_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_age_gender_married_age_age",
                                columnList = "age_group"
                        ),
                        Index(
                                name = "idx_ward_age_gender_married_age_gender",
                                columnList = "gender"
                        )]
)
@DynamicUpdate
class WardAgeGenderWiseMarriedAge : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Age group at which marriage occurred for this demographic data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false, columnDefinition = "married_age_group")
    var ageGroup: MarriedAgeGroup? = null

    /**
     * Gender for this demographic data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, columnDefinition = "gender")
    var gender: Gender? = null

    /**
     * The number of people of the specified gender who got married at the specified age range in the specified ward.
     * Must be a non-negative integer.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
