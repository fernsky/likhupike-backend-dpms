package np.sthaniya.dpis.profile.demographics.wardWiseReligionPopulation.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the population distribution by religion in each ward.
 *
 * This entity stores demographic data about religious populations for statistical analysis and
 * reporting purposes. Each record represents the number of people following a specific religion in
 * a specific ward.
 */
@Entity
@Table(
        name = "ward_wise_religion_population",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_religion",
                                columnNames = ["ward_number", "religion_type"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_wise_religion_population_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_wise_religion_population_religion",
                                columnList = "religion_type"
                        )]
)
@DynamicUpdate
class WardWiseReligionPopulation : UuidBaseEntity() {

    /**
     * Ward number where this population data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /** Religion type for this population data. Defined as an enum to ensure data consistency. */
    @Enumerated(EnumType.STRING)
    @Column(name = "religion_type", nullable = false, columnDefinition = "religion_type")
    var religionType: ReligionType? = null

    /**
     * The number of people following the specified religion in the specified ward. Must be a
     * non-negative integer.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
