package np.sthaniya.dpis.profile.demographics.wardWiseMotherTonguePopulation.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the population distribution by mother tongue in each ward.
 *
 * This entity stores demographic data about language populations for statistical analysis and
 * reporting purposes. Each record represents the number of people speaking a specific language
 * as their mother tongue in a specific ward.
 */
@Entity
@Table(
        name = "ward_wise_mother_tongue_population",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_language",
                                columnNames = ["ward_number", "language_type"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_wise_mother_tongue_population_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_wise_mother_tongue_population_language",
                                columnList = "language_type"
                        )]
)
@DynamicUpdate
class WardWiseMotherTonguePopulation : UuidBaseEntity() {

    /**
     * Ward number where this population data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /** Language type for this population data. Defined as an enum to ensure data consistency. */
    @Enumerated(EnumType.STRING)
    @Column(name = "language_type", nullable = false, columnDefinition = "language_type")
    var languageType: LanguageType? = null

    /**
     * The number of people speaking the specified language in the specified ward. Must be a
     * non-negative integer.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
