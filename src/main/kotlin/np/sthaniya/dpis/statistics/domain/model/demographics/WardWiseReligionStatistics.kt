package np.sthaniya.dpis.statistics.domain.model.demographics

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import np.sthaniya.dpis.statistics.domain.vo.demographic.ReligionType
import np.sthaniya.dpis.statistics.domain.vo.demographic.ReligionDistribution
import np.sthaniya.dpis.statistics.domain.event.demographics.ReligionStatsUpdatedEvent
import java.math.BigDecimal

/**
 * Ward-level statistics aggregating religious affiliation data.
 */
class WardWiseReligionStatistics : WardStatistics() {
    
    /**
     * Distribution of population by religion
     */
    var religionDistribution: ReligionDistribution = ReligionDistribution()
    
    /**
     * Dominant religion in the ward
     */
    var dominantReligion: ReligionType? = null
    
    /**
     * Percentage of population following the dominant religion
     */
    var dominantReligionPercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Religious diversity index (0-1, higher means more diverse)
     */
    var diversityIndex: BigDecimal = BigDecimal.ZERO
    
    /**
     * Secondary religions with significant followings
     */
    var secondaryReligions: List<ReligionType> = emptyList()
    
    /**
     * Whether the ward is religiously diverse (no religion >75%)
     */
    var isReligiouslyDiverse: Boolean = false
    
    init {
        this.statisticalGroup = "demographics"
        this.statisticalCategory = "ward_religion"
    }
    
    /**
     * Calculate the dominant religion and its percentage
     */
    fun calculateDominantReligion() {
        val percentages = religionDistribution.getPercentages()
        if (percentages.isEmpty()) return
        
        val maxEntry = percentages.maxByOrNull { it.value.toDouble() }
        if (maxEntry != null) {
            dominantReligion = maxEntry.key
            dominantReligionPercentage = maxEntry.value
        }
    }
    
    /**
     * Calculate secondary religions (>5% followers but not dominant)
     */
    fun calculateSecondaryReligions() {
        val percentages = religionDistribution.getPercentages()
        if (percentages.isEmpty()) return
        
        secondaryReligions = percentages
            .filter { it.key != dominantReligion && it.value.toDouble() >= 5.0 }
            .map { it.key }
            .sortedByDescending { percentages[it]?.toDouble() ?: 0.0 }
    }
    
    /**
     * Calculate religious diversity index using Simpson's Diversity Index
     * Formula: 1 - sum((ni/N)²) where ni is population following each religion and N is total population
     */
    fun calculateDiversityIndex() {
        val total = religionDistribution.getTotal()
        if (total <= 0) return
        
        val sumOfSquaredProportions = religionDistribution.religionMap.values.sumOf { count ->
            val proportion = count.toDouble() / total
            proportion * proportion
        }
        
        diversityIndex = BigDecimal(1 - sumOfSquaredProportions).setScale(4, BigDecimal.ROUND_HALF_UP)
    }
    
    /**
     * Calculate if the ward is religiously diverse
     */
    fun calculateReligiousDiversity() {
        isReligiouslyDiverse = dominantReligionPercentage.toDouble() < 75.0
    }

    override fun getComparisonValue(): Double? {
        // Use diversity index as comparison metric
        return diversityIndex.toDouble()
    }

    override fun getTotalWardPopulation(): Int {
        return religionDistribution.getTotal()
    }
    
    override fun getTotalWardHouseholds(): Int {
        // Not directly relevant for religion statistics, return 0 as fallback
        return 0
    }

    override fun getKeyMetrics(): Map<String, Any> {
        return mapOf(
            "totalPopulation" to religionDistribution.getTotal(),
            "dominantReligion" to (dominantReligion?.name ?: "UNKNOWN"),
            "dominantReligionPercentage" to dominantReligionPercentage,
            "secondaryReligions" to secondaryReligions.map { it.name },
            "diversityIndex" to diversityIndex,
            "isReligiouslyDiverse" to isReligiouslyDiverse
        )
    }
    
    override fun getProducedEvents(): List<Any> {
        return listOf(
            ReligionStatsUpdatedEvent(
                statisticsId = this.id!!,
                wardId = this.wardId!!,
                wardNumber = this.wardNumber!!,
                dominantReligion = this.dominantReligion?.name,
                diversityIndex = this.diversityIndex.toDouble(),
                isReligiouslyDiverse = this.isReligiouslyDiverse,
                calculationDate = this.calculationDate
            )
        )
    }
}
