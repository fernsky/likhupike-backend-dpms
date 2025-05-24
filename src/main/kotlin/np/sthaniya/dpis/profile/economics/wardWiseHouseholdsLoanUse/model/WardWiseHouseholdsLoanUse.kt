package np.sthaniya.dpis.profile.economics.wardWiseHouseholdsLoanUse.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.profile.economics.common.model.LoanUseType
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the distribution of household loan usage in each ward.
 *
 * This entity stores data about how households use loans for economic analysis and
 * reporting purposes. Each record represents the number of households using loans for a specific purpose
 * in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_households_loan_use",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_loan_use",
            columnNames = ["ward_number", "loan_use"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_households_loan_use_ward",
            columnList = "ward_number"
        ),
        Index(
            name = "idx_ward_wise_households_loan_use_use",
            columnList = "loan_use"
        )
    ]
)
@DynamicUpdate
class WardWiseHouseholdsLoanUse : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Type of loan use for this data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_use", nullable = false, columnDefinition = "loan_use_type")
    var loanUse: LoanUseType? = null

    /**
     * The number of households using loans for the specified purpose in the specified ward.
     */
    @Column(name = "households", nullable = false) var households: Int = 0
}
