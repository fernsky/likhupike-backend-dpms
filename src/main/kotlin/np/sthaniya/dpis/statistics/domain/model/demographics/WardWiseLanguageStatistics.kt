package np.sthaniya.dpis.statistics.domain.model.demographics

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import np.sthaniya.dpis.statistics.domain.vo.demographic.LanguageType
import np.sthaniya.dpis.statistics.domain.vo.demographic.LanguageDistribution
import np.sthaniya.dpis.statistics.domain.event.demographics.LanguageStatsUpdatedEvent
import java.math.BigDecimal

/**
 * Ward-level statistics aggregating language and linguistic data.
 */
class WardWiseLanguageStatistics : WardStatistics() {
    
    /**
     * Distribution of population by mother tongue
     */
    var languageDistribution: LanguageDistribution = LanguageDistribution()
    
    /**
     * Dominant language in the ward
     */
    var dominantLanguage: LanguageType? = null
    
    /**
     * Percentage of population speaking the dominant language
     */
    var dominantLanguagePercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Linguistic diversity index (0-1, higher means more diverse)
     */
    var diversityIndex: BigDecimal = BigDecimal.ZERO
    
    /**
     * Secondary languages spoken by significant populations
     */
    var secondaryLanguages: List<LanguageType> = emptyList()
    
    init {
        this.statisticalGroup = "demographics"
        this.statisticalCategory = "ward_language"
    }
    
    /**
     * Calculate the dominant language and its percentage
     */
    fun calculateDominantLanguage() {
        val percentages = languageDistribution.getPercentages()
        if (percentages.isEmpty()) return
        
        val maxEntry = percentages.maxByOrNull { it.value.toDouble() }
        if (maxEntry != null) {
            dominantLanguage = maxEntry.key
            dominantLanguagePercentage = maxEntry.value
        }
    }
    
    /**
     * Calculate secondary languages (>10% speakers but not dominant)
     */
    fun calculateSecondaryLanguages() {
        val percentages = languageDistribution.getPercentages()
        if (percentages.isEmpty()) return
        
        secondaryLanguages = percentages
            .filter { it.key != dominantLanguage && it.value.toDouble() >= 10.0 }
            .map { it.key }
            .sortedByDescending { percentages[it]?.toDouble() ?: 0.0 }
    }
    
    /**
     * Calculate linguistic diversity index using Simpson's Diversity Index
     * Formula: 1 - sum((ni/N)²) where ni is population speaking each language and N is total population
     */
    fun calculateDiversityIndex() {
        val total = languageDistribution.getTotal()
        if (total <= 0) return
        
        val sumOfSquaredProportions = languageDistribution.languageMap.values.sumOf { count ->
            val proportion = count.toDouble() / total
            proportion * proportion
        }
        
        diversityIndex = BigDecimal(1 - sumOfSquaredProportions).setScale(4, BigDecimal.ROUND_HALF_UP)
    }

    override fun getComparisonValue(): Double? {
        // Use diversity index as comparison metric
        return diversityIndex.toDouble()
    }

    override fun getTotalWardPopulation(): Int {
        return languageDistribution.getTotal()
    }
    
    override fun getTotalWardHouseholds(): Int {
        // Not directly relevant for demographics statistics, return 0 as fallback
        return 0
    }

    override fun getKeyMetrics(): Map<String, Any> {
        return mapOf(
            "totalPopulation" to languageDistribution.getTotal(),
            "dominantLanguage" to (dominantLanguage?.name ?: "UNKNOWN"),
            "dominantLanguagePercentage" to dominantLanguagePercentage,
            "secondaryLanguages" to secondaryLanguages.map { it.name },
            "diversityIndex" to diversityIndex,
            "majorLanguages" to LanguageType.getMajorLanguages().map { it.name }
        )
    }
    
    override fun getProducedEvents(): List<Any> {
        return listOf(
            LanguageStatsUpdatedEvent(
                statisticsId = this.id!!,
                wardId = this.wardId!!,
                wardNumber = this.wardNumber!!,
                dominantLanguage = this.dominantLanguage?.name,
                diversityIndex = this.diversityIndex.toDouble(),
                secondaryLanguagesCount = this.secondaryLanguages.size,
                calculationDate = this.calculationDate
            )
        )
    }
}
