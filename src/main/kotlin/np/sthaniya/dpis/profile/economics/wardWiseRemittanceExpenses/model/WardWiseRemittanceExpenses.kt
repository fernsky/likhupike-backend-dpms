package np.sthaniya.dpis.profile.economics.wardWiseRemittanceExpenses.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.profile.economics.common.model.RemittanceExpenseType
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the distribution of remittance expenses in each ward.
 *
 * This entity stores data about how households spend remittance money for economic analysis and
 * reporting purposes. Each record represents the number of households with a specific remittance
 * expense type in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_remittance_expenses",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_remittance_expense",
            columnNames = ["ward_number", "remittance_expense"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_remittance_expenses_ward",
            columnList = "ward_number"
        ),
        Index(
            name = "idx_ward_wise_remittance_expenses_expense",
            columnList = "remittance_expense"
        )
    ]
)
@DynamicUpdate
class WardWiseRemittanceExpenses : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Type of remittance expense for this data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "remittance_expense", nullable = false, columnDefinition = "remittance_expense_type")
    var remittanceExpense: RemittanceExpenseType? = null

    /**
     * The number of households with the specified remittance expense type in the specified ward.
     */
    @Column(name = "households", nullable = false) var households: Int = 0
}
