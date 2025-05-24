package np.sthaniya.dpis.profile.economics.wardTimeWiseHouseholdChores.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.profile.economics.common.model.TimeSpentType
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the distribution of time spent on household chores in each ward.
 *
 * This entity stores data about how much time people spend on household chores for economic and
 * social analysis. Each record represents the number of people spending a specific amount of time
 * on household chores in a specific ward.
 */
@Entity
@Table(
    name = "ward_time_wise_household_chores",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_time_spent",
            columnNames = ["ward_number", "time_spent"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_time_wise_household_chores_ward",
            columnList = "ward_number"
        ),
        Index(
            name = "idx_ward_time_wise_household_chores_time",
            columnList = "time_spent"
        )
    ]
)
@DynamicUpdate
class WardTimeWiseHouseholdChores : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Category of time spent on household chores.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "time_spent", nullable = false, columnDefinition = "time_spent")
    var timeSpent: TimeSpentType? = null

    /**
     * The number of people spending the specified amount of time on household chores in the specified ward.
     */
    @Column(name = "population", nullable = false) var population: Int = 0
}
