package np.sthaniya.dpis.profile.demographics.wardAgeWiseMaritalStatus.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the distribution of population by age group and marital status in each ward.
 *
 * This entity stores demographic data about marital status for different age groups for statistical
 * analysis and reporting purposes. Each record represents the number of people with a specific marital
 * status in a specific age group in a specific ward.
 */
@Entity
@Table(
        name = "ward_age_wise_marital_status",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_age_marital_status",
                                columnNames = ["ward_number", "age_group", "marital_status"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_wise_marital_status_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_wise_marital_status_age_group",
                                columnList = "age_group"
                        ),
                        Index(
                                name = "idx_ward_wise_marital_status_marital_status",
                                columnList = "marital_status"
                        )]
)
@DynamicUpdate
class WardWiseMaritalStatus : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Age group for this demographic data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false, columnDefinition = "age_group")
    var ageGroup: MaritalAgeGroup? = null

    /**
     * Marital status for this demographic data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false, columnDefinition = "marital_status")
    var maritalStatus: MaritalStatusGroup? = null

    /**
     * The number of people with the specified marital status in the specified age group in the specified ward.
     * Must be a non-negative integer.
     */
    @Column(name = "population", nullable = false) var population: Int = 0

    /**
     * The number of males with the specified marital status in the specified age group in the specified ward.
     */
    @Column(name = "male_population") var malePopulation: Int? = null

    /**
     * The number of females with the specified marital status in the specified age group in the specified ward.
     */
    @Column(name = "female_population") var femalePopulation: Int? = null

    /**
     * The number of other gender with the specified marital status in the specified age group in the specified ward.
     */
    @Column(name = "other_population") var otherPopulation: Int? = null
}
