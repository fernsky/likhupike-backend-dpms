package np.sthaniya.dpis.profile.economics.wardWiseAnnualIncomeSustenance.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import np.sthaniya.dpis.profile.economics.common.model.MonthsSustainedType
import org.hibernate.annotations.DynamicUpdate

/**
 * Entity representing the distribution of annual income sustenance in each ward.
 *
 * This entity stores data about how long a household's income can sustain them throughout the year
 * for economic analysis and reporting purposes. Each record represents the number of households with a
 * specific sustenance period in a specific ward.
 */
@Entity
@Table(
    name = "ward_wise_annual_income_sustenance",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ward_months_sustained",
            columnNames = ["ward_number", "months_sustained"]
        )
    ],
    indexes = [
        Index(
            name = "idx_ward_wise_annual_income_sustenance_ward",
            columnList = "ward_number"
        ),
        Index(
            name = "idx_ward_wise_annual_income_sustenance_months",
            columnList = "months_sustained"
        )
    ]
)
@DynamicUpdate
class WardWiseAnnualIncomeSustenance : UuidBaseEntity() {

    /**
     * Ward number where this data is collected from. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Category of months for which income can sustain the household.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "months_sustained", nullable = false, columnDefinition = "months_sustained")
    var monthsSustained: MonthsSustainedType? = null

    /**
     * The number of households with the specified sustenance period in the specified ward.
     */
    @Column(name = "households", nullable = false) var households: Int = 0
}
