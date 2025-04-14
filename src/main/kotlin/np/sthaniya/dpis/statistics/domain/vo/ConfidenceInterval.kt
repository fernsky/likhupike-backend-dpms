package np.sthaniya.dpis.statistics.domain.vo

import java.io.Serializable
import java.math.BigDecimal

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
