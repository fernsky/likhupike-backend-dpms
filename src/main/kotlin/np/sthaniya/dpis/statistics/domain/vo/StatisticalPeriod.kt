package np.sthaniya.dpis.statistics.domain.vo

import java.io.Serializable

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
