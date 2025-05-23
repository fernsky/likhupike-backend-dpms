package np.sthaniya.dpis.profile.demographics.wardWiseCastePopulation.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the population distribution by caste in each ward.
 *
 * This entity stores demographic data about caste populations for statistical analysis and
 * reporting purposes. Each record represents the number of people belonging to a specific caste
 * in a specific ward.
 */
@Entity
@Table(
        name = "ward_wise_caste_population",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_caste",
                                columnNames = ["ward_number", "caste_type"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_wise_caste_population_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_wise_caste_population_caste",
                                columnList = "caste_type"
                        )]
)
@DynamicUpdate
class WardWiseCastePopulation : UuidBaseEntity() {

    /**
     * Ward number where this population data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /** Caste type for this population data. Defined as an enum to ensure data consistency. */
    @Enumerated(EnumType.STRING)
    @Column(name = "caste_type", nullable = false, length = 100)
    var casteType: CasteType? = null

    /**
     * The number of people belonging to the specified caste in the specified ward.
     */
    @Column(name = "population") var population: Int? = null
}
