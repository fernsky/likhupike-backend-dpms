package np.sthaniya.dpis.statistics.domain.model.demographics

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import np.sthaniya.dpis.statistics.domain.vo.Gender
import np.sthaniya.dpis.statistics.domain.vo.demographic.WardAgeWisePopulationAgeGroup
import np.sthaniya.dpis.statistics.domain.vo.demographic.AgeGenderDistribution
import np.sthaniya.dpis.statistics.domain.event.demographics.AgeGenderStatsUpdatedEvent
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Ward-level statistics aggregating age and gender distribution data.
 */
class WardWiseAgeGenderStatistics : WardStatistics() {
    
    /**
     * Distribution of population by age group and gender
     */
    var ageGenderDistribution: AgeGenderDistribution = AgeGenderDistribution()
    
    /**
     * Dependency ratio: (pop 0-14 + pop 60+) / pop 15-59 × 100
     */
    var dependencyRatio: BigDecimal = BigDecimal.ZERO
    
    /**
     * Child dependency ratio: (pop 0-14) / pop 15-59 × 100
     */
    var childDependencyRatio: BigDecimal = BigDecimal.ZERO
    
    /**
     * Elderly dependency ratio: (pop 60+) / pop 15-59 × 100
     */
    var elderlyDependencyRatio: BigDecimal = BigDecimal.ZERO
    
    /**
     * Aging index: (pop 60+) / (pop 0-14) × 100
     */
    var agingIndex: BigDecimal = BigDecimal.ZERO
    
    /**
     * Proportion of youth (15-24) in the population (%)
     */
    var youthProportion: BigDecimal = BigDecimal.ZERO
    
    /**
     * Sex ratio by age group (males per 100 females)
     */
    var sexRatioByAgeGroup: Map<WardAgeWisePopulationAgeGroup, BigDecimal> = emptyMap()
    
    /**
     * Sex ratio for the entire ward (males per 100 females)
     */
    var overallSexRatio: BigDecimal = BigDecimal.ZERO
    
    /**
     * Median age of the ward population
     */
    var medianAge: BigDecimal? = null
    
    init {
        this.statisticalGroup = "demographics"
        this.statisticalCategory = "ward_age_gender"
    }
    
    /**
     * Calculate all demographic ratios and indices
     */
    fun calculateDemographicIndicators() {
        // Calculate dependency ratios
        dependencyRatio = ageGenderDistribution.getDependencyRatio()
        childDependencyRatio = ageGenderDistribution.getChildDependencyRatio()
        elderlyDependencyRatio = ageGenderDistribution.getElderlyDependencyRatio()
        
        // Calculate aging index
        agingIndex = ageGenderDistribution.getAgingIndex()
        
        // Calculate youth proportion
        calculateYouthProportion()
        
        // Calculate sex ratio by age group
        sexRatioByAgeGroup = ageGenderDistribution.getSexRatioByAgeGroup()
        
        // Calculate overall sex ratio
        calculateOverallSexRatio()
        
        // Estimate median age
        estimateMedianAge()
    }
    
    /**
     * Calculate the proportion of youth (15-24) in the population
     */
    private fun calculateYouthProportion() {
        val youthAgeGroups = listOf(
            WardAgeWisePopulationAgeGroup.AGE_15_19,
            WardAgeWisePopulationAgeGroup.AGE_20_24
        )
        
        val youthPopulation = ageGenderDistribution.distributionMap.entries
            .filter { (key, _) -> youthAgeGroups.contains(key.first) }
            .sumOf { it.value }
            
        val totalPopulation = ageGenderDistribution.getTotal()
        
        youthProportion = if (totalPopulation > 0) {
            BigDecimal(youthPopulation * 100).divide(
                BigDecimal(totalPopulation), 2, RoundingMode.HALF_UP
            )
        } else BigDecimal.ZERO
    }
    
    /**
     * Calculate the overall sex ratio for the ward
     */
    private fun calculateOverallSexRatio() {
        val genderTotals = ageGenderDistribution.getTotalByGender()
        val maleCount = genderTotals[Gender.MALE] ?: 0
        val femaleCount = genderTotals[Gender.FEMALE] ?: 0
        
        overallSexRatio = if (femaleCount > 0) {
            BigDecimal(maleCount * 100).divide(
                BigDecimal(femaleCount), 2, RoundingMode.HALF_UP
            )
        } else BigDecimal.ZERO
    }
    
    /**
     * Estimate the median age of the population
     * This is a simple estimation using age group midpoints
     */
    private fun estimateMedianAge() {
        val totalPopulation = ageGenderDistribution.getTotal()
        if (totalPopulation == 0) {
            medianAge = null
            return
        }
        
        // Define midpoints for age groups
        val ageMidpoints = mapOf(
            WardAgeWisePopulationAgeGroup.AGE_0_4 to 2.5,
            WardAgeWisePopulationAgeGroup.AGE_5_9 to 7.5,
            WardAgeWisePopulationAgeGroup.AGE_10_14 to 12.5,
            WardAgeWisePopulationAgeGroup.AGE_15_19 to 17.5,
            WardAgeWisePopulationAgeGroup.AGE_20_24 to 22.5,
            WardAgeWisePopulationAgeGroup.AGE_25_29 to 27.5,
            WardAgeWisePopulationAgeGroup.AGE_30_34 to 32.5,
            WardAgeWisePopulationAgeGroup.AGE_35_39 to 37.5,
            WardAgeWisePopulationAgeGroup.AGE_40_44 to 42.5,
            WardAgeWisePopulationAgeGroup.AGE_45_49 to 47.5,
            WardAgeWisePopulationAgeGroup.AGE_50_54 to 52.5,
            WardAgeWisePopulationAgeGroup.AGE_55_59 to 57.5,
            WardAgeWisePopulationAgeGroup.AGE_60_64 to 62.5,
            WardAgeWisePopulationAgeGroup.AGE_65_69 to 67.5,
            WardAgeWisePopulationAgeGroup.AGE_70_74 to 72.5,
            WardAgeWisePopulationAgeGroup.AGE_75_AND_ABOVE to 80.0
        )
        
        // Get population by age group
        val populationByAgeGroup = ageGenderDistribution.getTotalByAgeGroup()
        
        // Sort by age group ordinal to ensure correct order
        val sortedAgeGroups = populationByAgeGroup.keys.sortedBy { it.ordinal }
        
        // Calculate the cumulative population
        var cumulativePopulation = 0
        val halfPopulation = totalPopulation / 2
        
        // Find the age group containing the median
        var medianAgeGroup: WardAgeWisePopulationAgeGroup? = null
        var previousCumulative = 0
        
        for (ageGroup in sortedAgeGroups) {
            val groupPopulation = populationByAgeGroup[ageGroup] ?: 0
            cumulativePopulation += groupPopulation
            
            if (cumulativePopulation >= halfPopulation) {
                medianAgeGroup = ageGroup
                previousCumulative = cumulativePopulation - groupPopulation
                break
            }
        }
        
        // If a median age group was found, estimate the median age
        if (medianAgeGroup != null) {
            val ageGroupPopulation = populationByAgeGroup[medianAgeGroup] ?: 0
            val ageGroupMidpoint = ageMidpoints[medianAgeGroup] ?: 0.0
            
            // Simple linear interpolation within the age group
            if (ageGroupPopulation > 0) {
                val positionInGroup = (halfPopulation - previousCumulative).toDouble() / ageGroupPopulation
                val ageWidth = when (medianAgeGroup) {
                    WardAgeWisePopulationAgeGroup.AGE_75_AND_ABOVE -> 10.0 // Assumed width for 75+ group
                    else -> (medianAgeGroup.maxAge ?: 0) - medianAgeGroup.minAge + 1.0
                }
                
                val estimatedAge = medianAgeGroup.minAge + (ageWidth * positionInGroup)
                medianAge = BigDecimal(estimatedAge).setScale(1, RoundingMode.HALF_UP)
            } else {
                medianAge = BigDecimal(ageGroupMidpoint).setScale(1, RoundingMode.HALF_UP)
            }
        } else {
            medianAge = null
        }
    }
    
    /**
     * Get age distribution percentages
     */
    fun getAgeDistributionPercentages(): Map<WardAgeWisePopulationAgeGroup, BigDecimal> {
        val totalPopulation = ageGenderDistribution.getTotal()
        if (totalPopulation == 0) return emptyMap()
        
        val populationByAge = ageGenderDistribution.getTotalByAgeGroup()
        
        return populationByAge.mapValues { (_, count) ->
            BigDecimal(count * 100).divide(BigDecimal(totalPopulation), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Get gender distribution percentages
     */
    fun getGenderDistributionPercentages(): Map<Gender, BigDecimal> {
        val totalPopulation = ageGenderDistribution.getTotal()
        if (totalPopulation == 0) return emptyMap()
        
        val populationByGender = ageGenderDistribution.getTotalByGender()
        
        return populationByGender.mapValues { (_, count) ->
            BigDecimal(count * 100).divide(BigDecimal(totalPopulation), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Get the broad age category distribution (children, youth, working-age, elderly)
     */
    fun getBroadAgeCategoryDistribution(): Map<String, Int> {
        val categories = WardAgeWisePopulationAgeGroup.getMajorAgeCategories()
        val result = mutableMapOf<String, Int>()
        
        categories.forEach { (category, ageGroups) ->
            val categoryPopulation = ageGenderDistribution.distributionMap.entries
                .filter { (key, _) -> ageGroups.contains(key.first) }
                .sumOf { it.value }
            
            result[category] = categoryPopulation
        }
        
        return result
    }

    override fun getComparisonValue(): Double? {
        // Use median age as comparison metric
        return medianAge?.toDouble()
    }

    override fun getTotalWardPopulation(): Int {
        return ageGenderDistribution.getTotal()
    }
    
    override fun getTotalWardHouseholds(): Int {
        // Not directly relevant for demographics statistics, return 0 as fallback
        return 0
    }

    override fun getKeyMetrics(): Map<String, Any> {
        val broadCategories = getBroadAgeCategoryDistribution()
        val genderDistribution = ageGenderDistribution.getTotalByGender()
        
        return mapOf(
            "totalPopulation" to ageGenderDistribution.getTotal(),
            "malePopulation" to (genderDistribution[Gender.MALE] ?: 0),
            "femalePopulation" to (genderDistribution[Gender.FEMALE] ?: 0),
            "otherPopulation" to (genderDistribution[Gender.OTHER] ?: 0),
            "childrenPopulation" to (broadCategories["CHILDREN"] ?: 0),
            "youthPopulation" to (broadCategories["YOUTH"] ?: 0),
            "workingAgePopulation" to (broadCategories["WORKING_AGE"] ?: 0),
            "elderlyPopulation" to (broadCategories["ELDERLY"] ?: 0),
            "dependencyRatio" to dependencyRatio,
            "childDependencyRatio" to childDependencyRatio,
            "elderlyDependencyRatio" to elderlyDependencyRatio,
            "agingIndex" to agingIndex,
            "youthProportion" to youthProportion,
            "overallSexRatio" to overallSexRatio,
            "medianAge" to (medianAge ?: BigDecimal.ZERO)
        )
    }
    
    override fun getProducedEvents(): List<Any> {
        val genderDistribution = ageGenderDistribution.getTotalByGender()
        
        return listOf(
            AgeGenderStatsUpdatedEvent(
                statisticsId = this.id!!,
                wardId = this.wardId!!,
                wardNumber = this.wardNumber!!,
                totalPopulation = ageGenderDistribution.getTotal(),
                malePopulation = genderDistribution[Gender.MALE] ?: 0,
                femalePopulation = genderDistribution[Gender.FEMALE] ?: 0,
                otherPopulation = genderDistribution[Gender.OTHER] ?: 0,
                dependencyRatio = this.dependencyRatio.toDouble(),
                sexRatio = this.overallSexRatio.toDouble(),
                medianAge = this.medianAge?.toDouble(),
                calculationDate = this.calculationDate
            )
        )
    }
}
