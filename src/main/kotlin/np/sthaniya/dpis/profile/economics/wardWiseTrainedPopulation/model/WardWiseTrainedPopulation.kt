package np.sthaniya.dpis.profile.economics.wardWiseTrainedPopulation.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the trained population in each ward.
 *
 * This entity stores data about people who have received vocational or professional training for economic
 * analysis and planning purposes. Each record represents the number of trained people in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_trained_population",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_trained_population",
            columnNames = ["ward_number"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_trained_population_ward",
            columnList = "ward_number"
        )
    ]
)
@DynamicUpdate
class WardWiseTrainedPopulation : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * The number of trained people in the specified ward.
     */
    @Column(name = "trained_population", nullable = false) var trainedPopulation: Int = 0
}
