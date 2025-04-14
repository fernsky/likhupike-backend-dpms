package np.sthaniya.dpis.statistics.domain.model.demographics

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import np.sthaniya.dpis.statistics.domain.vo.Gender
import np.sthaniya.dpis.statistics.domain.vo.PopulationBreakdown
import np.sthaniya.dpis.statistics.domain.vo.HouseholdMetrics
import np.sthaniya.dpis.statistics.domain.event.demographics.DemographicStatsUpdatedEvent
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Ward-level demographic statistics aggregating population data.
 */
class WardWiseDemographicStatistics : WardStatistics() {
    
    /**
     * Total population in the ward
     */
    var totalPopulation: Int = 0
    
    /**
     * Population breakdown by gender
     */
    var populationByGender: PopulationBreakdown = PopulationBreakdown()
    
    /**
     * Number of households in this ward
     */
    var totalHouseholds: Int = 0
    
    /**
     * Average household size
     */
    var averageHouseholdSize: BigDecimal = BigDecimal.ZERO
    
    /**
     * Sex ratio (males per 100 females)
     */
    var sexRatio: BigDecimal = BigDecimal.ZERO
    
    /**
     * Various household metrics
     */
    var householdMetrics: HouseholdMetrics = HouseholdMetrics()
    
    /**
     * Population density (people per square km)
     */
    var populationDensity: BigDecimal = BigDecimal.ZERO
    
    /**
     * Ward area in square kilometers
     */
    var areaInSquareKm: BigDecimal = BigDecimal.ZERO
    
    /**
     * Annual population growth rate (percentage)
     */
    var growthRate: BigDecimal? = null
    
    init {
        this.statisticalGroup = "demographics"
        this.statisticalCategory = "ward_population"
    }
    
    /**
     * Calculate sex ratio based on gender breakdown
     */
    fun calculateSexRatio() {
        if (populationByGender.female > 0) {
            sexRatio = BigDecimal(populationByGender.male * 100)
                .divide(BigDecimal(populationByGender.female), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Calculate average household size
     */
    fun calculateAverageHouseholdSize() {
        if (totalHouseholds > 0) {
            averageHouseholdSize = BigDecimal(totalPopulation)
                .divide(BigDecimal(totalHouseholds), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Calculate population density
     */
    fun calculatePopulationDensity() {
        if (areaInSquareKm.compareTo(BigDecimal.ZERO) > 0) {
            populationDensity = BigDecimal(totalPopulation)
                .divide(areaInSquareKm, 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Update the total population based on gender breakdown
     */
    fun updateTotalFromGenderBreakdown() {
        totalPopulation = populationByGender.getTotal()
    }

    override fun getComparisonValue(): Double {
        // Use population density as the standard comparison metric
        return populationDensity.toDouble()
    }

    override fun getTotalWardPopulation(): Int {
        return totalPopulation
    }

    override fun getTotalWardHouseholds(): Int {
        // Not directly relevant for demographics statistics, return 0 as fallback
        return 0
    }

    override fun getKeyMetrics(): Map<String, Any> {
        return mapOf(
            "totalPopulation" to totalPopulation,
            "totalHouseholds" to totalHouseholds,
            "malePopulation" to populationByGender.male,
            "femalePopulation" to populationByGender.female,
            "otherPopulation" to populationByGender.other,
            "averageHouseholdSize" to averageHouseholdSize,
            "sexRatio" to sexRatio,
            "populationDensity" to populationDensity
        )
    }
    
    override fun getProducedEvents(): List<Any> {
        return listOf(
            DemographicStatsUpdatedEvent(
                statisticsId = this.id!!,
                wardId = this.wardId!!,
                wardNumber = this.wardNumber!!,
                totalPopulation = this.totalPopulation,
                malePopulation = this.populationByGender.male,
                femalePopulation = this.populationByGender.female,
                otherPopulation = this.populationByGender.other,
                totalHouseholds = this.totalHouseholds,
                calculationDate = this.calculationDate
            )
        )
    }
}
