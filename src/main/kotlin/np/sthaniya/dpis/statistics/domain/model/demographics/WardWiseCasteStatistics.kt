package np.sthaniya.dpis.statistics.domain.model.demographics

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import np.sthaniya.dpis.statistics.domain.vo.demographic.CasteType
import np.sthaniya.dpis.statistics.domain.vo.demographic.CasteDistribution
import np.sthaniya.dpis.statistics.domain.event.demographics.CasteStatsUpdatedEvent
import java.math.BigDecimal

/**
 * Ward-level statistics aggregating caste and ethnicity data.
 */
class WardWiseCasteStatistics : WardStatistics() {
    
    /**
     * Distribution of population by caste/ethnicity
     */
    var casteDistribution: CasteDistribution = CasteDistribution()
    
    /**
     * Dominant caste/ethnicity in the ward
     */
    var dominantCaste: CasteType? = null
    
    /**
     * Percentage of population belonging to the dominant caste
     */
    var dominantCastePercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Ethnicity diversity index (0-1, higher means more diverse)
     */
    var diversityIndex: BigDecimal = BigDecimal.ZERO
    
    init {
        this.statisticalGroup = "demographics"
        this.statisticalCategory = "ward_caste_ethnicity"
    }
    
    /**
     * Calculate the dominant caste and its percentage
     */
    fun calculateDominantCaste() {
        val percentages = casteDistribution.getPercentages()
        if (percentages.isEmpty()) return
        
        val maxEntry = percentages.maxByOrNull { it.value }
        if (maxEntry != null) {
            dominantCaste = maxEntry.key
            dominantCastePercentage = BigDecimal(maxEntry.value.toString())
        }
    }
    
    /**
     * Calculate ethnicity diversity index using Simpson's Diversity Index
     * Formula: 1 - sum((ni/N)²) where ni is population of each caste and N is total population
     */
    fun calculateDiversityIndex() {
        val total = casteDistribution.getTotal()
        if (total <= 0) return
        
        val sumOfSquaredProportions = casteDistribution.casteMap.values.sumOf { count ->
            val proportion = count.toDouble() / total
            proportion * proportion
        }
        
        diversityIndex = BigDecimal(1 - sumOfSquaredProportions).setScale(4, BigDecimal.ROUND_HALF_UP)
    }
    
    /**
     * Calculate aggregated caste category statistics
     */
    fun getAggregatedCasteStatistics(): Map<String, Any> {
        val aggregatedCounts = casteDistribution.getAggregatedByCategory()
        val total = casteDistribution.getTotal()
        
        // Calculate percentages for each category
        val percentages = if (total > 0) {
            aggregatedCounts.mapValues { (_, count) ->
                BigDecimal((count.toDouble() / total) * 100).setScale(2, BigDecimal.ROUND_HALF_UP)
            }
        } else {
            aggregatedCounts.mapValues { BigDecimal.ZERO }
        }
        
        return mapOf(
            "counts" to aggregatedCounts,
            "percentages" to percentages
        )
    }

    override fun getComparisonValue(): Double? {
        // Use diversity index as comparison metric
        return diversityIndex.toDouble()
    }

    override fun getTotalWardPopulation(): Int {
        return casteDistribution.getTotal()
    }
    
    override fun getTotalWardHouseholds(): Int {
        // Not directly relevant for demographics statistics, return 0 as fallback
        return 0
    }

    override fun getKeyMetrics(): Map<String, Any> {
        val aggregatedStats = getAggregatedCasteStatistics()
        
        return mapOf(
            "totalPopulation" to casteDistribution.getTotal(),
            "dominantCaste" to (dominantCaste?.name ?: "UNKNOWN"),
            "dominantCastePercentage" to dominantCastePercentage,
            "diversityIndex" to diversityIndex,
            "majorCasteGroups" to CasteType.getMajorGroups().map { it.name },
            "aggregatedCasteCounts" to (aggregatedStats["counts"] ?: emptyMap<String, Int>()),
            "aggregatedCastePercentages" to (aggregatedStats["percentages"] ?: emptyMap<String, BigDecimal>())
        )
    }
    
    override fun getProducedEvents(): List<Any> {
        return listOf(
            CasteStatsUpdatedEvent(
                statisticsId = this.id!!,
                wardId = this.wardId!!,
                wardNumber = this.wardNumber!!,
                dominantCaste = this.dominantCaste?.name,
                diversityIndex = this.diversityIndex.toDouble(),
                calculationDate = this.calculationDate
            )
        )
    }
}
