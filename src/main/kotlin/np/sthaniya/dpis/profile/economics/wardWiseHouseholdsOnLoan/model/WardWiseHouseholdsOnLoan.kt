package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsOnLoan.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the households with loans in each ward.
 *
 * This entity stores data about households that have taken loans for economic analysis and
 * financial inclusion planning. Each record represents the number of households with loans in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_households_on_loan",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_households_on_loan",
            columnNames = ["ward_number"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_households_on_loan_ward",
            columnList = "ward_number"
        )
    ]
)
@DynamicUpdate
class WardWiseHouseholdsOnLoan : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * The number of households with loans in the specified ward.
     */
    @Column(name = "households", nullable = false) var households: Int = 0
}
