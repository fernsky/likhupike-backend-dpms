package np.sthaniya.dpis.profile.economics.wardWiseHouseholdLandPossessions.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the households with land possessions in each ward.
 *
 * This entity stores data about households that own land for economic analysis and
 * land management planning. Each record represents the number of households with land possessions in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_household_land_possessions",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_household_land_possessions",
            columnNames = ["ward_number"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_household_land_possessions_ward",
            columnList = "ward_number"
        )
    ]
)
@DynamicUpdate
class WardWiseHouseholdLandPossessions : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * The number of households with land possessions in the specified ward.
     */
    @Column(name = "households", nullable = false) var households: Int = 0
}
