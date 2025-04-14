package np.sthaniya.dpis.statistics.domain.vo

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

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
