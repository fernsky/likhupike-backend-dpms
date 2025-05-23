package np.sthaniya.dpis.profile.demographics.wardWiseDemographicSummary.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.math.BigDecimal

/**
 * Entity representing the demographic summary for each ward.
 *
 * This entity stores aggregated demographic data for a ward, including population statistics,
 * household data, and demographic ratios. Each record represents the demographic summary for
 * a specific ward.
 */
@Entity
@Table(
        name = "ward_wise_demographic_summary",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_number",
                                columnNames = ["ward_number"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_wise_demographic_summary_ward",
                                columnList = "ward_number"
                        )]
)
@DynamicUpdate
class WardWiseDemographicSummary : UuidBaseEntity() {

    /**
     * Ward number for which this summary is collected. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Ward name for reference purposes.
     */
    @Column(name = "ward_name", columnDefinition = "text") var wardName: String? = null

    /**
     * Total population in the ward.
     */
    @Column(name = "total_population") var totalPopulation: Int? = null

    /**
     * Male population in the ward.
     */
    @Column(name = "population_male") var populationMale: Int? = null

    /**
     * Female population in the ward.
     */
    @Column(name = "population_female") var populationFemale: Int? = null

    /**
     * Other gender population in the ward.
     */
    @Column(name = "population_other") var populationOther: Int? = null

    /**
     * Total number of households in the ward.
     */
    @Column(name = "total_households") var totalHouseholds: Int? = null

    /**
     * Average household size in the ward.
     */
    @Column(name = "average_household_size") var averageHouseholdSize: BigDecimal? = null

    /**
     * Sex ratio (number of males per 100 females) in the ward.
     */
    @Column(name = "sex_ratio") var sexRatio: BigDecimal? = null
}
