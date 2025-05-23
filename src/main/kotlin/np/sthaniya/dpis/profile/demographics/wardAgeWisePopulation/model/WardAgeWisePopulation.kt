package np.sthaniya.dpis.profile.demographics.wardAgeWisePopulation.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate
import np.sthaniya.dpis.profile.demographics.common.model.Gender


/**
 * Entity representing the population distribution by age group and gender in each ward.
 *
 * This entity stores demographic data about age and gender distribution for statistical analysis and
 * reporting purposes. Each record represents the number of people of a specific gender in a specific
 * age group in a specific ward.
 */
@Entity
@Table(
        name = "ward_age_wise_population",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_age_gender",
                                columnNames = ["ward_number", "age_group", "gender"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_age_wise_population_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_age_wise_population_age_group",
                                columnList = "age_group"
                        ),
                        Index(
                                name = "idx_ward_age_wise_population_gender",
                                columnList = "gender"
                        )]
)
@DynamicUpdate
class WardAgeWisePopulation : UuidBaseEntity() {

    /**
     * Ward number where this population data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Age group for this demographic data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false)
    var ageGroup: AgeGroup? = null

    /**
     * Gender for this demographic data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    var gender: Gender? = null

    /**
     * The number of people of the specified gender in the specified age group in the specified ward.
     * Must be a non-negative integer.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
