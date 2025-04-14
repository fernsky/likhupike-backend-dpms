package np.sthaniya.dpis.statistics.domain.vo

import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

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
