package np.sthaniya.dpis.profile.demographics.demographicSummary.model

import jakarta.persistence.*
import np.sthaniya.dpis.common.entity.UuidBaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.math.BigDecimal

/**
 * Entity representing the overall demographic summary for the entire municipality.
 *
 * This entity stores aggregated demographic data for the municipality, including population statistics,
 * household data, and demographic ratios. It is designed as a singleton entity with only one record
 * for the entire database.
 */
@Entity
@Table(name = "demographic_summary")
@DynamicUpdate
class DemographicSummary : UuidBaseEntity() {

    /**
     * Total population in the municipality.
     */
    @Column(name = "total_population") var totalPopulation: Int? = null

    /**
     * Male population in the municipality.
     */
    @Column(name = "population_male") var populationMale: Int? = null

    /**
     * Female population in the municipality.
     */
    @Column(name = "population_female") var populationFemale: Int? = null

    /**
     * Other gender population in the municipality.
     */
    @Column(name = "population_other") var populationOther: Int? = null

    /**
     * Total absentee population in the municipality.
     */
    @Column(name = "population_absentee_total") var populationAbsenteeTotal: Int? = null

    /**
     * Male absentee population in the municipality.
     */
    @Column(name = "population_male_absentee") var populationMaleAbsentee: Int? = null

    /**
     * Female absentee population in the municipality.
     */
    @Column(name = "population_female_absentee") var populationFemaleAbsentee: Int? = null

    /**
     * Other gender absentee population in the municipality.
     */
    @Column(name = "population_other_absentee") var populationOtherAbsentee: Int? = null

    /**
     * Sex ratio (number of males per 100 females) in the municipality.
     */
    @Column(name = "sex_ratio") var sexRatio: BigDecimal? = null

    /**
     * Total number of households in the municipality.
     */
    @Column(name = "total_households") var totalHouseholds: Int? = null

    /**
     * Average household size in the municipality.
     */
    @Column(name = "average_household_size") var averageHouseholdSize: BigDecimal? = null

    /**
     * Population density (people per sq km) in the municipality.
     */
    @Column(name = "population_density") var populationDensity: BigDecimal? = null

    /**
     * Population aged 0-14 years in the municipality.
     */
    @Column(name = "population_0_to_14") var population0To14: Int? = null

    /**
     * Population aged 15-59 years in the municipality.
     */
    @Column(name = "population_15_to_59") var population15To59: Int? = null

    /**
     * Population aged 60 and above in the municipality.
     */
    @Column(name = "population_60_and_above") var population60AndAbove: Int? = null

    /**
     * Population growth rate in the municipality.
     */
    @Column(name = "growth_rate") var growthRate: BigDecimal? = null

    /**
     * Literacy rate for population above 15 years in the municipality.
     */
    @Column(name = "literacy_rate_above_15") var literacyRateAbove15: BigDecimal? = null

    /**
     * Literacy rate for population between 15-24 years in the municipality.
     */
    @Column(name = "literacy_rate_15_to_24") var literacyRate15To24: BigDecimal? = null
}
