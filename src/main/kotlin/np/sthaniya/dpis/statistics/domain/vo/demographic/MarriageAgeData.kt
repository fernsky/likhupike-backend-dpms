package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import np.sthaniya.dpis.statistics.domain.vo.Gender

/**
 * Enumeration of age group categories at which marriage occurred.
 */
enum class MarriedAgeGroup(val minAge: Int, val maxAge: Int?) {
    AGE_BELOW_15(0, 14),
    AGE_15_19(15, 19),
    AGE_20_24(20, 24),
    AGE_25_29(25, 29),
    AGE_30_34(30, 34),
    AGE_35_39(35, 39),
    AGE_40_AND_ABOVE(40, null);
    
    companion object {
        /**
         * Find the appropriate married age group for a given age
         */
        fun fromAge(age: Int): MarriedAgeGroup {
            return values().find { ageGroup -> 
                age >= ageGroup.minAge && (ageGroup.maxAge == null || age <= ageGroup.maxAge)
            } ?: AGE_40_AND_ABOVE
        }
    }
}

/**
 * Value object representing marriage age distribution data
 */
data class MarriageAgeData(
    val distributionMap: MutableMap<Pair<MarriedAgeGroup, Gender>, Int> = mutableMapOf()
) : Serializable {
    
    /**
     * Get total married population
     */
    fun getTotal(): Int = distributionMap.values.sum()
    
    /**
     * Get distribution by gender
     */
    fun getByGender(): Map<Gender, Int> {
        return distributionMap.entries
            .groupBy { it.key.second }
            .mapValues { entry -> entry.value.sumOf { it.value } }
    }
    
    /**
     * Get distribution by marriage age group
     */
    fun getByAgeGroup(): Map<MarriedAgeGroup, Int> {
        return distributionMap.entries
            .groupBy { it.key.first }
            .mapValues { entry -> entry.value.sumOf { it.value } }
    }
    
    /**
     * Calculate percentage of early marriages (below age 20)
     */
    fun getEarlyMarriagePercentage(): BigDecimal {
        val earlyMarriageGroups = listOf(MarriedAgeGroup.AGE_BELOW_15, MarriedAgeGroup.AGE_15_19)
        
        val earlyMarriageCount = distributionMap.entries
            .filter { (key, _) -> earlyMarriageGroups.contains(key.first) }
            .sumOf { it.value }
            
        val total = getTotal()
        
        return if (total > 0) {
            BigDecimal(earlyMarriageCount * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Calculate child marriage percentage (below age 15)
     */
    fun getChildMarriagePercentage(): BigDecimal {
        val childMarriageCount = distributionMap.entries
            .filter { (key, _) -> key.first == MarriedAgeGroup.AGE_BELOW_15 }
            .sumOf { it.value }
            
        val total = getTotal()
        
        return if (total > 0) {
            BigDecimal(childMarriageCount * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Get average age at marriage by gender
     * Using midpoint of each age group
     */
    fun getAverageMarriageAge(): Map<Gender, BigDecimal> {
        val result = mutableMapOf<Gender, BigDecimal>()
        
        // Define midpoints for age groups
        val ageMidpoints = mapOf(
            MarriedAgeGroup.AGE_BELOW_15 to 12.5,
            MarriedAgeGroup.AGE_15_19 to 17.5,
            MarriedAgeGroup.AGE_20_24 to 22.5,
            MarriedAgeGroup.AGE_25_29 to 27.5,
            MarriedAgeGroup.AGE_30_34 to 32.5,
            MarriedAgeGroup.AGE_35_39 to 37.5,
            MarriedAgeGroup.AGE_40_AND_ABOVE to 45.0
        )
        
        // Group by gender
        val byGender = distributionMap.entries.groupBy { it.key.second }
        
        byGender.forEach { (gender, entries) ->
            val totalForGender = entries.sumOf { it.value }
            
            if (totalForGender > 0) {
                // Calculate weighted average
                val weightedSum = entries.sumOf { (key, count) -> 
                    count * (ageMidpoints[key.first] ?: 0.0)
                }
                
                val avgAge = BigDecimal(weightedSum).divide(
                    BigDecimal(totalForGender), 2, RoundingMode.HALF_UP
                )
                
                result[gender] = avgAge
            }
        }
        
        return result
    }
    
    /**
     * Add population count for a specific marriage age group and gender
     */
    fun add(marriageAgeGroup: MarriedAgeGroup, gender: Gender, count: Int) {
        val key = Pair(marriageAgeGroup, gender)
        distributionMap[key] = (distributionMap[key] ?: 0) + count
    }
}
