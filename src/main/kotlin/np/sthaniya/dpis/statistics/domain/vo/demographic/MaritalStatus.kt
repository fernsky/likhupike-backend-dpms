package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Enumeration of marital status categories used for demographic classification.
 */
enum class MaritalStatus {
    UNMARRIED,       // विवाह नभएको
    ONE_MARRIAGE,    // एक विवाह
    MULTI_MARRIAGE,  // बहुविवाह
    REMARRIAGE,      // पुनर्विवाह
    WIDOWED,         // विधुर/विधवा
    DIVORCED,        // पारपाचुके
    SEPARATED;       // छुट्टिएको
    
    companion object {
        /**
         * Group marital statuses into broader categories
         */
        fun groupByCategory(): Map<String, List<MaritalStatus>> {
            return mapOf(
                "NEVER_MARRIED" to listOf(UNMARRIED),
                "CURRENTLY_MARRIED" to listOf(ONE_MARRIAGE, MULTI_MARRIAGE, REMARRIAGE),
                "FORMERLY_MARRIED" to listOf(WIDOWED, DIVORCED, SEPARATED)
            )
        }
    }
}

/**
 * Enumeration of age group categories used for marital status analysis.
 */
enum class MaritalStatusAgeGroup(val minAge: Int, val maxAge: Int?) {
    AGE_BELOW_15(0, 14),
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
        fun fromAge(age: Int): MaritalStatusAgeGroup {
            return values().find { ageGroup -> 
                age >= ageGroup.minAge && (ageGroup.maxAge == null || age <= ageGroup.maxAge)
            } ?: AGE_75_AND_ABOVE
        }
        
        /**
         * Group age groups into broader categories
         */
        fun getAgeCategories(): Map<String, List<MaritalStatusAgeGroup>> {
            return mapOf(
                "YOUTH" to listOf(AGE_BELOW_15, AGE_15_19, AGE_20_24),
                "YOUNG_ADULT" to listOf(AGE_25_29, AGE_30_34, AGE_35_39),
                "MIDDLE_AGE" to listOf(AGE_40_44, AGE_45_49, AGE_50_54, AGE_55_59),
                "SENIOR" to listOf(AGE_60_64, AGE_65_69, AGE_70_74, AGE_75_AND_ABOVE)
            )
        }
    }
}

/**
 * Value object representing distribution of population by marital status and age group
 */
data class MaritalStatusDistribution(
    val distributionMap: MutableMap<Pair<MaritalStatusAgeGroup, MaritalStatus>, Int> = mutableMapOf()
) : Serializable {
    
    /**
     * Get total population across all marital statuses and age groups
     */
    fun getTotal(): Int = distributionMap.values.sum()
    
    /**
     * Get distribution by age group
     */
    fun getByAgeGroup(): Map<MaritalStatusAgeGroup, Int> {
        return distributionMap.entries
            .groupBy { it.key.first }
            .mapValues { entry -> entry.value.sumOf { it.value } }
    }
    
    /**
     * Get distribution by marital status
     */
    fun getByMaritalStatus(): Map<MaritalStatus, Int> {
        return distributionMap.entries
            .groupBy { it.key.second }
            .mapValues { entry -> entry.value.sumOf { it.value } }
    }
    
    /**
     * Get percentage of population that is currently married
     */
    fun getMarriedPercentage(): BigDecimal {
        val total = getTotal()
        if (total == 0) return BigDecimal.ZERO
        
        val marriedStatuses = listOf(
            MaritalStatus.ONE_MARRIAGE, 
            MaritalStatus.MULTI_MARRIAGE, 
            MaritalStatus.REMARRIAGE
        )
        
        val marriedCount = distributionMap.entries
            .filter { marriedStatuses.contains(it.key.second) }
            .sumOf { it.value }
        
        return BigDecimal(marriedCount * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
    }
    
    /**
     * Add population count for a specific age group and marital status
     */
    fun add(ageGroup: MaritalStatusAgeGroup, status: MaritalStatus, count: Int) {
        val key = Pair(ageGroup, status)
        distributionMap[key] = (distributionMap[key] ?: 0) + count
    }
    
    /**
     * Get early marriage rate (married below age 20)
     */
    fun getEarlyMarriageRate(): BigDecimal {
        val earlyMarriedCount = distributionMap.entries
            .filter { 
                it.key.first in listOf(MaritalStatusAgeGroup.AGE_BELOW_15, MaritalStatusAgeGroup.AGE_15_19) &&
                it.key.second in listOf(MaritalStatus.ONE_MARRIAGE, MaritalStatus.MULTI_MARRIAGE, MaritalStatus.REMARRIAGE)
            }
            .sumOf { it.value }
        
        val totalInAgeGroup = distributionMap.entries
            .filter { it.key.first in listOf(MaritalStatusAgeGroup.AGE_BELOW_15, MaritalStatusAgeGroup.AGE_15_19) }
            .sumOf { it.value }
        
        return if (totalInAgeGroup > 0) {
            BigDecimal(earlyMarriedCount * 100).divide(BigDecimal(totalInAgeGroup), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
}
