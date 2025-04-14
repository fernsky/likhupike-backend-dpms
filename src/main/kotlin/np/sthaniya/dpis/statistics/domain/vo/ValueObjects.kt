package np.sthaniya.dpis.statistics.domain.vo

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Gender enum for population classification
 */
enum class Gender {
    MALE, FEMALE, OTHER;
}

/**
 * Represents a population breakdown by gender
 */
data class PopulationBreakdown(
    var male: Int = 0,
    var female: Int = 0,
    var other: Int = 0
) : Serializable {
    
    /**
     * Get total population across all genders
     */
    fun getTotal(): Int = male + female + other
    
    /**
     * Get percentage breakdown of each gender
     */
    fun getPercentages(): Map<Gender, BigDecimal> {
        val total = getTotal()
        if (total == 0) return emptyMap()
        
        return mapOf(
            Gender.MALE to BigDecimal(male * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP),
            Gender.FEMALE to BigDecimal(female * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP),
            Gender.OTHER to BigDecimal(other * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        )
    }
}

/**
 * Metrics related to households
 */
data class HouseholdMetrics(
    var femaleHeaded: Int = 0,
    var maleHeaded: Int = 0,
    var otherHeaded: Int = 0,
    var singlePersonHouseholds: Int = 0,
    var householdsWithElderlyMembers: Int = 0,
    var householdsWithDisabledMembers: Int = 0
) : Serializable {
    
    /**
     * Get total number of households
     */
    fun getTotal(): Int = femaleHeaded + maleHeaded + otherHeaded
    
    /**
     * Calculate percentage of households headed by a particular gender
     */
    fun getHeadshipPercentage(gender: Gender): BigDecimal {
        val total = getTotal()
        if (total == 0) return BigDecimal.ZERO
        
        val count = when(gender) {
            Gender.MALE -> maleHeaded
            Gender.FEMALE -> femaleHeaded
            Gender.OTHER -> otherHeaded
        }
        
        return BigDecimal(count * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
    }
}

/**
 * Statistical time period
 */
data class StatisticalPeriod(
    val year: Int,
    val quarter: Int? = null,
    val month: Int? = null,
    val isNepaliFiscalYear: Boolean = true
) : Serializable {
    
    /**
     * Get standardized string representation of the period
     */
    fun getDisplayName(): String {
        return when {
            month != null -> "$year-$month"
            quarter != null -> "$year-Q$quarter"
            else -> if (isNepaliFiscalYear) "$year/${ (year % 100) + 1 }" else year.toString()
        }
    }
}

/**
 * Comparison result between two statistical values
 */
data class StatisticalComparison(
    val currentValue: Number,
    val previousValue: Number,
    val percentageChange: BigDecimal,
    val absoluteChange: BigDecimal,
    val isPositiveTrend: Boolean
) : Serializable {
    
    companion object {
        /**
         * Create a comparison between two values
         */
        fun create(current: Number, previous: Number, higherIsBetter: Boolean = true): StatisticalComparison {
            val currentBD = BigDecimal(current.toString())
            val previousBD = BigDecimal(previous.toString())
            val absoluteChange = currentBD.subtract(previousBD)
            
            val percentageChange = if (previousBD.compareTo(BigDecimal.ZERO) != 0) {
                absoluteChange.multiply(BigDecimal(100)).divide(previousBD.abs(), 2, RoundingMode.HALF_UP)
            } else {
                BigDecimal.ZERO
            }
            
            val isPositive = if (higherIsBetter) 
                currentBD.compareTo(previousBD) > 0 
            else 
                currentBD.compareTo(previousBD) < 0
                
            return StatisticalComparison(
                currentValue = current,
                previousValue = previous,
                percentageChange = percentageChange,
                absoluteChange = absoluteChange,
                isPositiveTrend = isPositive
            )
        }
    }
}

/**
 * Represents a confidence interval for statistical calculations
 */
data class ConfidenceInterval(
    val mean: BigDecimal,
    val lowerBound: BigDecimal,
    val upperBound: BigDecimal,
    val confidenceLevel: BigDecimal = BigDecimal("0.95")
) : Serializable {
    
    /**
     * Get the interval width
     */
    fun getIntervalWidth(): BigDecimal = upperBound.subtract(lowerBound)
    
    /**
     * Check if a value is within the confidence interval
     */
    fun isWithinInterval(value: BigDecimal): Boolean {
        return value >= lowerBound && value <= upperBound
    }
}
