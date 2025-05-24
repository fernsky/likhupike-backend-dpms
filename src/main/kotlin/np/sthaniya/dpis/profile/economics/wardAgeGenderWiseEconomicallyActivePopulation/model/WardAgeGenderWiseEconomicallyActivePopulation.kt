package np.sthaniya.dpis.profile.economics.wardAgeGenderWiseEconomicallyActivePopulation.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.profile.demographics.common.model.Gender
import np.sthaniya.dpis.profile.economics.common.model.EconomicallyActiveAgeGroup
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the distribution of economically active population by age group and gender in each ward.
 *
 * This entity stores data about economically active population for statistical analysis and
 * economic reporting purposes. Each record represents the number of economically active people
 * of a specific gender in a specific age group in a specific ward.
 */
@Entity
@Table(
    name = "ward_age_gender_wise_economically_active_population",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_age_gender_economically_active",
            columnNames = ["ward_number", "age_group", "gender"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_age_gender_eco_active_ward",
            columnList = "ward_number"
        ),
        Index(
            name = "idx_ward_age_gender_eco_active_age",
            columnList = "age_group"
        ),
        Index(
            name = "idx_ward_age_gender_eco_active_gender",
            columnList = "gender"
        )
    ]
)
@DynamicUpdate
class WardAgeGenderWiseEconomicallyActivePopulation : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Age group for this economically active population data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false, columnDefinition = "economically_active_age_group")
    var ageGroup: EconomicallyActiveAgeGroup? = null

    /**
     * Gender for this economically active population data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, columnDefinition = "gender")
    var gender: Gender? = null

    /**
     * The number of economically active people of the specified gender
     * in the specified age group in the specified ward.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
