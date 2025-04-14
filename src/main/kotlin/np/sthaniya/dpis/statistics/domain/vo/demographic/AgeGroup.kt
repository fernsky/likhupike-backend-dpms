package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import np.sthaniya.dpis.statistics.domain.vo.Gender

/**
 * Enumeration of age group categories used for demographic analysis.
 */
enum class WardAgeWisePopulationAgeGroup(val minAge: Int, val maxAge: Int?) {
    AGE_0_4(0, 4),
    AGE_5_9(5, 9),
    AGE_10_14(10, 14),
    AGE_15_19(15, 19),
    AGE_20_24(20, 24),
    AGE_25_29(25, 29),
    AGE_30_34(30, 34),
    AGE_35_39(35, 39),
    AGE_40_44(40, 44),
    AGE_45_49(45, 49),
    AGE_50_54(50, 54),
    AGE_55_59(55, 59),
    AGE_60_64(60, 64),
    AGE_65_69(65, 69),
    AGE_70_74(70, 74),
    AGE_75_AND_ABOVE(75, null);
    
    companion object {
        /**
         * Find the appropriate age group for a given age
         */
        fun fromAge(age: Int): WardAgeWisePopulationAgeGroup {
            return values().find { ageGroup -> 
                age >= ageGroup.minAge && (ageGroup.maxAge == null || age <= ageGroup.maxAge)
            } ?: AGE_75_AND_ABOVE
        }
        
        /**
         * Get major age categories for demographic analysis
         */
        fun getMajorAgeCategories(): Map<String, List<WardAgeWisePopulationAgeGroup>> {
            return mapOf(
                "CHILDREN" to listOf(AGE_0_4, AGE_5_9, AGE_10_14),
                "YOUTH" to listOf(AGE_15_19, AGE_20_24),
                "WORKING_AGE" to listOf(
                    AGE_25_29, AGE_30_34, AGE_35_39, AGE_40_44, 
                    AGE_45_49, AGE_50_54, AGE_55_59
                ),
                "ELDERLY" to listOf(
                    AGE_60_64, AGE_65_69, AGE_70_74, AGE_75_AND_ABOVE
                )
            )
        }
        
        /**
         * Get economic age categories (typically used for dependency ratio)
         */
        fun getEconomicAgeCategories(): Map<String, List<WardAgeWisePopulationAgeGroup>> {
            return mapOf(
                "PRE_WORKING" to listOf(AGE_0_4, AGE_5_9, AGE_10_14),
                "WORKING" to listOf(
                    AGE_15_19, AGE_20_24, AGE_25_29, AGE_30_34, AGE_35_39,
                    AGE_40_44, AGE_45_49, AGE_50_54, AGE_55_59
                ),
                "POST_WORKING" to listOf(
                    AGE_60_64, AGE_65_69, AGE_70_74, AGE_75_AND_ABOVE
                )
            )
        }
    }
}

/**
 * Value object representing age-gender distribution of population
 */
data class AgeGenderDistribution(
    val distributionMap: MutableMap<Pair<WardAgeWisePopulationAgeGroup, Gender>, Int> = mutableMapOf()
) : Serializable {
    
    /**
     * Get total population across all age groups and genders
     */
    fun getTotal(): Int = distributionMap.values.sum()
    
    /**
     * Get total population by gender
     */
    fun getTotalByGender(): Map<Gender, Int> {
        return distributionMap.entries
            .groupBy { it.key.second }
            .mapValues { entry -> entry.value.sumOf { it.value } }
    }
    
    /**
     * Get total population by age group
     */
    fun getTotalByAgeGroup(): Map<WardAgeWisePopulationAgeGroup, Int> {
        return distributionMap.entries
            .groupBy { it.key.first }
            .mapValues { entry -> entry.value.sumOf { it.value } }
    }
    
    /**
     * Calculate dependency ratio: (population aged 0-14 + population aged 60+) / population aged 15-59
     */
    fun getDependencyRatio(): BigDecimal {
        val ageCategories = WardAgeWisePopulationAgeGroup.getEconomicAgeCategories()
        
        val preWorkingAges = ageCategories["PRE_WORKING"] ?: emptyList()
        val postWorkingAges = ageCategories["POST_WORKING"] ?: emptyList()
        val workingAges = ageCategories["WORKING"] ?: emptyList()
        
        val dependentPopulation = distributionMap.entries
            .filter { (key, _) -> 
                preWorkingAges.contains(key.first) || postWorkingAges.contains(key.first) 
            }
            .sumOf { it.value }
        
        val workingPopulation = distributionMap.entries
            .filter { (key, _) -> workingAges.contains(key.first) }
            .sumOf { it.value }
        
        return if (workingPopulation > 0) {
            BigDecimal(dependentPopulation * 100).divide(BigDecimal(workingPopulation), 2, RoundingMode.HALF_UP)
        } else BigDecimal("999.99") // Indicates invalid ratio (no working population)
    }
    
    /**
     * Calculate child dependency ratio: (population aged 0-14) / population aged 15-59
     */
    fun getChildDependencyRatio(): BigDecimal {
        val ageCategories = WardAgeWisePopulationAgeGroup.getEconomicAgeCategories()
        
        val preWorkingAges = ageCategories["PRE_WORKING"] ?: emptyList()
        val workingAges = ageCategories["WORKING"] ?: emptyList()
        
        val childPopulation = distributionMap.entries
            .filter { (key, _) -> preWorkingAges.contains(key.first) }
            .sumOf { it.value }
        
        val workingPopulation = distributionMap.entries
            .filter { (key, _) -> workingAges.contains(key.first) }
            .sumOf { it.value }
        
        return if (workingPopulation > 0) {
            BigDecimal(childPopulation * 100).divide(BigDecimal(workingPopulation), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Calculate elderly dependency ratio: (population aged 60+) / population aged 15-59
     */
    fun getElderlyDependencyRatio(): BigDecimal {
        val ageCategories = WardAgeWisePopulationAgeGroup.getEconomicAgeCategories()
        
        val postWorkingAges = ageCategories["POST_WORKING"] ?: emptyList()
        val workingAges = ageCategories["WORKING"] ?: emptyList()
        
        val elderlyPopulation = distributionMap.entries
            .filter { (key, _) -> postWorkingAges.contains(key.first) }
            .sumOf { it.value }
        
        val workingPopulation = distributionMap.entries
            .filter { (key, _) -> workingAges.contains(key.first) }
            .sumOf { it.value }
        
        return if (workingPopulation > 0) {
            BigDecimal(elderlyPopulation * 100).divide(BigDecimal(workingPopulation), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Calculate aging index: (population aged 60+) / (population aged 0-14) × 100
     */
    fun getAgingIndex(): BigDecimal {
        val ageCategories = WardAgeWisePopulationAgeGroup.getEconomicAgeCategories()
        
        val preWorkingAges = ageCategories["PRE_WORKING"] ?: emptyList()
        val postWorkingAges = ageCategories["POST_WORKING"] ?: emptyList()
        
        val elderlyPopulation = distributionMap.entries
            .filter { (key, _) -> postWorkingAges.contains(key.first) }
            .sumOf { it.value }
        
        val childPopulation = distributionMap.entries
            .filter { (key, _) -> preWorkingAges.contains(key.first) }
            .sumOf { it.value }
        
        return if (childPopulation > 0) {
            BigDecimal(elderlyPopulation * 100).divide(BigDecimal(childPopulation), 2, RoundingMode.HALF_UP)
        } else BigDecimal("999.99") // Indicates invalid index (no child population)
    }
    
    /**
     * Calculate sex ratio by age group
     */
    fun getSexRatioByAgeGroup(): Map<WardAgeWisePopulationAgeGroup, BigDecimal> {
        val result = mutableMapOf<WardAgeWisePopulationAgeGroup, BigDecimal>()
        
        // Group by age
        val byAge = distributionMap.entries.groupBy { it.key.first }
        
        byAge.forEach { (ageGroup, entries) ->
            // Get male and female counts for this age group
            val maleCount = entries.find { it.key.second == Gender.MALE }?.value ?: 0
            val femaleCount = entries.find { it.key.second == Gender.FEMALE }?.value ?: 0
            
            // Calculate sex ratio (males per 100 females)
            if (femaleCount > 0) {
                val ratio = BigDecimal(maleCount * 100).divide(BigDecimal(femaleCount), 2, RoundingMode.HALF_UP)
                result[ageGroup] = ratio
            }
        }
        
        return result
    }
    
    /**
     * Add population count for a specific age group and gender
     */
    fun add(ageGroup: WardAgeWisePopulationAgeGroup, gender: Gender, count: Int) {
        val key = Pair(ageGroup, gender)
        distributionMap[key] = (distributionMap[key] ?: 0) + count
    }
}
