package np.sthaniya.dpis.profile.demographics.wardTimeSeriesPopulation.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.math.BigDecimal

/**
 * Entity representing the time series population data for each ward across different census years.
 *
 * This entity stores historical demographic data for a ward, allowing for trend analysis and
 * projection. Each record represents the demographic data for a specific ward in a specific year.
 */
@Entity
@Table(
        name = "ward_time_series_population",
        uniqueConstraints =
                [
                        UniqueConstraint(
                                name = "uq_ward_year",
                                columnNames = ["ward_number", "year"]
                        )],
        indexes =
                [
                        Index(
                                name = "idx_ward_time_series_population_ward",
                                columnList = "ward_number"
                        ),
                        Index(
                                name = "idx_ward_time_series_population_year",
                                columnList = "year"
                        )]
)
@DynamicUpdate
class WardTimeSeriesPopulation : UuidBaseEntity() {

    /**
     * Ward number for which this data is collected. Represents the administrative ward
     * within the municipality.
     */
    @Column(name = "ward_number", nullable = false) var wardNumber: Int? = null

    /**
     * Ward name for reference purposes.
     */
    @Column(name = "ward_name", columnDefinition = "text") var wardName: String? = null

    /**
     * Census year for which the data is collected (e.g., 2068, 2078).
     */
    @Column(name = "year", nullable = false) var year: Int? = null

    /**
     * Total population in the ward for the specific year.
     */
    @Column(name = "total_population") var totalPopulation: Int? = null

    /**
     * Male population in the ward for the specific year.
     */
    @Column(name = "male_population") var malePopulation: Int? = null

    /**
     * Female population in the ward for the specific year.
     */
    @Column(name = "female_population") var femalePopulation: Int? = null

    /**
     * Other gender population in the ward for the specific year.
     */
    @Column(name = "other_population") var otherPopulation: Int? = null

    /**
     * Total number of households in the ward for the specific year.
     */
    @Column(name = "total_households") var totalHouseholds: Int? = null

    /**
     * Average household size in the ward for the specific year.
     */
    @Column(name = "average_household_size") var averageHouseholdSize: BigDecimal? = null

    /**
     * Population aged 0-14 years in the ward for the specific year.
     */
    @Column(name = "population_0_to_14") var population0To14: Int? = null

    /**
     * Population aged 15-59 years in the ward for the specific year.
     */
    @Column(name = "population_15_to_59") var population15To59: Int? = null

    /**
     * Population aged 60 and above in the ward for the specific year.
     */
    @Column(name = "population_60_and_above") var population60AndAbove: Int? = null

    /**
     * Literacy rate in the ward for the specific year.
     */
    @Column(name = "literacy_rate") var literacyRate: BigDecimal? = null

    /**
     * Male literacy rate in the ward for the specific year.
     */
    @Column(name = "male_literacy_rate") var maleLiteracyRate: BigDecimal? = null

    /**
     * Female literacy rate in the ward for the specific year.
     */
    @Column(name = "female_literacy_rate") var femaleLiteracyRate: BigDecimal? = null

    /**
     * Population growth rate in the ward for the specific year.
     */
    @Column(name = "growth_rate") var growthRate: BigDecimal? = null

    /**
     * Area of the ward in square kilometers.
     */
    @Column(name = "area_sq_km") var areaSqKm: BigDecimal? = null

    /**
     * Population density (people per square kilometer) in the ward for the specific year.
     */
    @Column(name = "population_density") var populationDensity: BigDecimal? = null

    /**
     * Sex ratio (number of males per 100 females) in the ward for the specific year.
     */
    @Column(name = "sex_ratio") var sexRatio: BigDecimal? = null
}
