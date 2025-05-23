package np.sthaniya.dpis.profile.demographics.wardWiseAbsenteeEducationalLevel.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate
import np.sthaniya.dpis.profile.demographics.common.model.EducationalLevelType

/**
 * Entity representing the distribution of absentee population by educational level in each ward.
 *
 * This entity stores demographic data about the educational levels of absentee populations for
 * statistical analysis and reporting purposes. Each record represents the number of absentee people
 * with a specific educational level in a specific ward.
 */
@Entity
@Table(
        name = "ward_wise_absentee_educational_level",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_educational_level",
                                columnNames = ["ward_number", "educational_level"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_wise_absentee_educational_level_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_wise_absentee_educational_level_educational_level",
                                columnList = "educational_level"
                        )]
)
@DynamicUpdate
class WardWiseAbsenteeEducationalLevel : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Educational level for this demographic data. Defined as an enum to ensure data consistency.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "educational_level", nullable = false, columnDefinition = "educational_level")
    var educationalLevel: EducationalLevelType? = null

    /**
     * The number of absentee people with the specified educational level in the specified ward.
     * Must be a non-negative integer.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
