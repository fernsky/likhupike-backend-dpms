package np.sthaniya.dpis.statistics.domain.model

import java.util.UUID

/**
 * Base class for ward-level statistics.
 * 
 * Extends the base statistics class with ward-specific attributes
 * and provides a foundation for all ward-level statistical aggregates.
 */
abstract class WardStatistics : BaseStatistics() {
    
    /**
     * ID of the ward this statistic is about
     */
    var wardId: UUID? = null
    
    /**
     * Ward number (user-friendly identifier)
     */
    var wardNumber: Int? = null
    
    /**
     * Whether this statistic is for the entire ward or a subset
     */
    var isCompleteWard: Boolean = true
    
    /**
     * If not for complete ward, describes the subset
     */
    var subsetDescription: String? = null
    
    /**
     * Population count applicable for this statistic
     */
    var applicablePopulation: Int? = null
    
    /**
     * Household count applicable for this statistic
     */
    var applicableHouseholds: Int? = null
    
    /**
     * Whether this ward data is comparable with others
     */
    var isComparable: Boolean = true
    
    /**
     * Standardized method for comparing this statistic across wards
     */
    abstract fun getComparisonValue(): Double?
    
    /**
     * Population percentage this statistic represents for the ward
     */
    fun getPopulationPercentage(): Double? {
        return applicablePopulation?.let { population ->
            val totalWardPopulation = getTotalWardPopulation()
            if (totalWardPopulation > 0) {
                (population.toDouble() / totalWardPopulation) * 100
            } else null
        }
    }
    
    /**
     * Household percentage this statistic represents for the ward
     */
    fun getHouseholdPercentage(): Double? {
        return applicableHouseholds?.let { households ->
            val totalWardHouseholds = getTotalWardHouseholds()
            if (totalWardHouseholds > 0) {
                (households.toDouble() / totalWardHouseholds) * 100
            } else null
        }
    }
    
    /**
     * Implementation should fetch the total ward population
     */
    protected abstract fun getTotalWardPopulation(): Int
    
    /**
     * Implementation should fetch the total ward households
     */
    protected abstract fun getTotalWardHouseholds(): Int
    
    /**
     * Get the key metrics for this ward statistic as a map
     */
    abstract fun getKeyMetrics(): Map<String, Any>
}
