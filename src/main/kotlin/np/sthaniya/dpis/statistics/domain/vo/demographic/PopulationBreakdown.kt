package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Represents a population breakdown by gender.
 * 
 * This value object encapsulates gender distribution data and provides methods
 * for calculating totals and percentages.
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
    
    /**
     * Calculate sex ratio (males per 100 females)
     */
    fun getSexRatio(): BigDecimal {
        return if (female > 0) {
            BigDecimal(male * 100).divide(BigDecimal(female), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Calculate gender gap (difference between male and female percentages)
     */
    fun getGenderGap(): BigDecimal {
        val total = getTotal().toDouble()
        if (total == 0.0) return BigDecimal.ZERO
        
        val malePercentage = (male.toDouble() / total) * 100
        val femalePercentage = (female.toDouble() / total) * 100
        
        return BigDecimal(malePercentage - femalePercentage).setScale(2, RoundingMode.HALF_UP)
    }
    
    /**
     * Check if the gender distribution is balanced within a given threshold
     * (default threshold is 5% difference between male and female percentages)
     */
    fun isBalanced(thresholdPercentage: Double = 5.0): Boolean {
        return getGenderGap().abs().toDouble() <= thresholdPercentage
    }
    
    /**
     * Get the dominant gender (gender with highest population)
     */
    fun getDominantGender(): Gender? {
        return when {
            male > female && male > other -> Gender.MALE
            female > male && female > other -> Gender.FEMALE
            other > male && other > female -> Gender.OTHER
            else -> null // No dominant gender (equal counts)
        }
    }
    
    /**
     * Add another population breakdown to this one
     */
    fun add(other: PopulationBreakdown): PopulationBreakdown {
        return PopulationBreakdown(
            male = this.male + other.male,
            female = this.female + other.female,
            other = this.other + other.other
        )
    }
    
    /**
     * Create a new population breakdown with values incremented by the given amounts
     */
    fun incrementBy(male: Int = 0, female: Int = 0, other: Int = 0): PopulationBreakdown {
        return PopulationBreakdown(
            male = this.male + male,
            female = this.female + female,
            other = this.other + other
        )
    }
    
    /**
     * Convert to a human-readable string representation
     */
    override fun toString(): String {
        return "Population(total=${getTotal()}, male=$male, female=$female, other=$other, sexRatio=${getSexRatio()})"
    }
    
    companion object {
        /**
         * Create a population breakdown from a total and percentages
         */
        fun fromPercentages(total: Int, malePercent: Double, femalePercent: Double, otherPercent: Double): PopulationBreakdown {
            // Normalize percentages to ensure they sum to 100%
            val sum = malePercent + femalePercent + otherPercent
            val normalizedMalePercent = malePercent / sum * 100
            val normalizedFemalePercent = femalePercent / sum * 100
            val normalizedOtherPercent = otherPercent / sum * 100
            
            // Calculate counts based on percentages
            val male = (normalizedMalePercent * total / 100).toInt()
            val female = (normalizedFemalePercent * total / 100).toInt()
            
            // Ensure the total stays exactly the same by adjusting the "other" category
            val other = total - male - female
            
            return PopulationBreakdown(male, female, other)
        }
    }
}
