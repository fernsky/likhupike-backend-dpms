package np.sthaniya.dpis.profile.economics.wardWiseHouseholdIncomeSource.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.profile.economics.common.model.IncomeSourceType
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the distribution of household income sources in each ward.
 *
 * This entity stores data about the primary sources of income for households for economic analysis and
 * reporting purposes. Each record represents the number of households with a specific income source
 * in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_household_income_source",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_income_source",
            columnNames = ["ward_number", "income_source"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_household_income_source_ward",
            columnList = "ward_number"
        ),
        Index(
            name = "idx_ward_wise_household_income_source_source",
            columnList = "income_source"
        )
    ]
)
@DynamicUpdate
class WardWiseHouseholdIncomeSource : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Type of income source for this data.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "income_source", nullable = false, columnDefinition = "income_source_type")
    var incomeSource: IncomeSourceType? = null

    /**
     * The number of households with the specified income source in the specified ward.
     */
    @Column(name = "households", nullable = false) var households: Int = 0
}
