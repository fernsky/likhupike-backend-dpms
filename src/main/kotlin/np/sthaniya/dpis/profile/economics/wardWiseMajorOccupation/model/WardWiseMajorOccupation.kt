package np.sthaniya.dpis.profile.economics.wardWiseMajorOccupation.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.profile.economics.common.model.OccupationType
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the major occupations of the population in each ward.
 *
 * This entity stores data about the occupations that people are engaged in for economic analysis and
 * employment planning purposes. Each record represents the number of people with a specific occupation
 * in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_major_occupation",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_occupation",
            columnNames = ["ward_number", "occupation"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_major_occupation_ward",
            columnList = "ward_number"
        ),
        Index(
            name = "idx_ward_wise_major_occupation_occupation",
            columnList = "occupation"
        )
    ]
)
@DynamicUpdate
class WardWiseMajorOccupation : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Type of occupation. Defined as an enum to ensure data consistency.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "occupation", nullable = false, length = 100)
    var occupation: OccupationType? = null

    /**
     * The number of people with the specified occupation in the specified ward.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
