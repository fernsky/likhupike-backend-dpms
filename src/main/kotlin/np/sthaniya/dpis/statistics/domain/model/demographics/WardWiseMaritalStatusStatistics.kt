package np.sthaniya.dpis.statistics.domain.model.demographics

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import np.sthaniya.dpis.statistics.domain.vo.demographic.MaritalStatus
import np.sthaniya.dpis.statistics.domain.vo.demographic.MaritalStatusAgeGroup
import np.sthaniya.dpis.statistics.domain.vo.demographic.MaritalStatusDistribution
import np.sthaniya.dpis.statistics.domain.event.demographics.MaritalStatsUpdatedEvent
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Ward-level statistics aggregating marital status data.
 */
class WardWiseMaritalStatusStatistics : WardStatistics() {
    
    /**
     * Distribution of population by marital status and age group
     */
    var maritalStatusDistribution: MaritalStatusDistribution = MaritalStatusDistribution()
    
    /**
     * Percentage of population that is currently married
     */
    var marriedPercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Percentage of population that is never married
     */
    var neverMarriedPercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Percentage of population that was formerly married (widowed, divorced, separated)
     */
    var formerlyMarriedPercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Early marriage rate - percentage of people under 20 who are married
     */
    var earlyMarriageRate: BigDecimal = BigDecimal.ZERO
    
    /**
     * Percentage of single-parent households (estimated from marital status)
     */
    var singleParentPercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Distribution of marital status by gender
     */
    var maritalStatusByGender: Map<Pair<MaritalStatus, Gender>, Int> = emptyMap()
    
    init {
        this.statisticalGroup = "demographics"
        this.statisticalCategory = "ward_marital_status"
    }
    
    /**
     * Calculate all marital status indicators
     */
    fun calculateMaritalStatusIndicators() {
        calculateOverallPercentages()
        calculateEarlyMarriageRate()
        calculateMaritalStatusByGender()
        estimateSingleParentPercentage()
    }
    
    /**
     * Calculate overall percentages for major marital status categories
     */
    private fun calculateOverallPercentages() {
        // Get distribution by marital status
        val distributionByStatus = maritalStatusDistribution.getByMaritalStatus()
        val total = maritalStatusDistribution.getTotal()
        
        if (total == 0) {
            marriedPercentage = BigDecimal.ZERO
            neverMarriedPercentage = BigDecimal.ZERO
            formerlyMarriedPercentage = BigDecimal.ZERO
            return
        }
        
        // Group by major categories
        val statusCategories = MaritalStatus.groupByCategory()
        
        // Calculate married percentage
        val marriedCount = statusCategories["CURRENTLY_MARRIED"]?.sumOf { distributionByStatus[it] ?: 0 } ?: 0
        marriedPercentage = BigDecimal(marriedCount * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        
        // Calculate never married percentage
        val neverMarriedCount = statusCategories["NEVER_MARRIED"]?.sumOf { distributionByStatus[it] ?: 0 } ?: 0
        neverMarriedPercentage = BigDecimal(neverMarriedCount * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        
        // Calculate formerly married percentage
        val formerlyMarriedCount = statusCategories["FORMERLY_MARRIED"]?.sumOf { distributionByStatus[it] ?: 0 } ?: 0
        formerlyMarriedPercentage = BigDecimal(formerlyMarriedCount * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
    }
    
    /**
     * Calculate early marriage rate - percentage of people under 20 who are married
     */
    private fun calculateEarlyMarriageRate() {
        earlyMarriageRate = maritalStatusDistribution.getEarlyMarriageRate()
    }
    
    /**
     * Calculate distribution of marital status by gender
     */
    private fun calculateMaritalStatusByGender() {
        // This requires additional data not directly available in MaritalStatusDistribution
        // For now, we'll leave this as placeholder implementation
        maritalStatusByGender = emptyMap()
    }
    
    /**
     * Estimate percentage of households likely to be single-parent
     * This is a rough estimation based on marital status data
     */
    private fun estimateSingleParentPercentage() {
        // For now, we'll use a simple estimation based on widowed, divorced and separated
        // individuals who are in prime parenting age groups
        val parentingAgeGroups = MaritalStatusAgeGroup.values().filter { ageGroup ->
            ageGroup.minAge >= 25 && (ageGroup.maxAge == null || ageGroup.maxAge <= 49)
        }
        
        val singleParentStatuses = listOf(
            MaritalStatus.WIDOWED,
            MaritalStatus.DIVORCED,
            MaritalStatus.SEPARATED
        )
        
        val likelySingleParents = maritalStatusDistribution.distributionMap.entries
            .filter { (key, _) -> 
                parentingAgeGroups.contains(key.first) && singleParentStatuses.contains(key.second)
            }
            .sumOf { it.value }
        
        // Estimate against total households
        // Since we don't have direct household data, we'll use a rough estimate that about
        // 70% of adults in the parenting age groups represent households
        val totalInParentingAges = maritalStatusDistribution.distributionMap.entries
            .filter { (key, _) -> parentingAgeGroups.contains(key.first) }
            .sumOf { it.value }
            
        val estimatedHouseholds = (totalInParentingAges * 0.7).toInt()
        
        singleParentPercentage = if (estimatedHouseholds > 0) {
            BigDecimal(likelySingleParents * 100).divide(BigDecimal(estimatedHouseholds), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Get distribution of marital status by age group as percentages
     */
    fun getMaritalStatusByAgePercentages(): Map<MaritalStatusAgeGroup, Map<MaritalStatus, BigDecimal>> {
        val result = mutableMapOf<MaritalStatusAgeGroup, Map<MaritalStatus, BigDecimal>>()
        
        // Get distribution by age group
        val byAgeGroup = maritalStatusDistribution.distributionMap.entries
            .groupBy { it.key.first }
        
        // Calculate percentages for each age group
        byAgeGroup.forEach { (ageGroup, entries) ->
            val totalForAgeGroup = entries.sumOf { it.value }
            
            if (totalForAgeGroup > 0) {
                val percentagesForAgeGroup = entries
                    .groupBy { it.key.second }
                    .mapValues { (_, statusEntries) -> 
                        val statusCount = statusEntries.sumOf { it.value }
                        BigDecimal(statusCount * 100).divide(BigDecimal(totalForAgeGroup), 2, RoundingMode.HALF_UP)
                    }
                
                result[ageGroup] = percentagesForAgeGroup
            }
        }
        
        return result
    }

    override fun getComparisonValue(): Double? {
        // Use married percentage as comparison metric
        return marriedPercentage.toDouble()
    }

    override fun getTotalWardPopulation(): Int {
        return maritalStatusDistribution.getTotal()
    }
    
    override fun getTotalWardHouseholds(): Int {
        // Not directly relevant for demographics statistics, return 0 as fallback
        return 0
    }

    override fun getKeyMetrics(): Map<String, Any> {
        return mapOf(
            "totalPopulation" to maritalStatusDistribution.getTotal(),
            "marriedPercentage" to marriedPercentage,
            "neverMarriedPercentage" to neverMarriedPercentage,
            "formerlyMarriedPercentage" to formerlyMarriedPercentage,
            "earlyMarriageRate" to earlyMarriageRate,
            "singleParentPercentage" to singleParentPercentage
        )
    }
    
    override fun getProducedEvents(): List<Any> {
        return listOf(
            MaritalStatsUpdatedEvent(
                statisticsId = this.id!!,
                wardId = this.wardId!!,
                wardNumber = this.wardNumber!!,
                marriedPercentage = this.marriedPercentage.toDouble(),
                earlyMarriageRate = this.earlyMarriageRate.toDouble(),
                calculationDate = this.calculationDate
            )
        )
    }
}
