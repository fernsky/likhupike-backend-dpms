package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Enumeration of religion categories used for demographic classification.
 */
enum class ReligionType {
    HINDU,
    BUDDHIST,
    KIRANT,
    CHRISTIAN,
    ISLAM,
    NATURE,
    BON,
    JAIN,
    BAHAI,
    SIKH,
    OTHER;
}

/**
 * Value object representing a distribution of population by religion
 */
data class ReligionDistribution(
    val religionMap: MutableMap<ReligionType, Int> = mutableMapOf()
) : Serializable {
    
    /**
     * Get total population across all religions
     */
    fun getTotal(): Int = religionMap.values.sum()
    
    /**
     * Get percentage distribution by religion
     */
    fun getPercentages(): Map<ReligionType, BigDecimal> {
        val total = getTotal()
        if (total == 0) return emptyMap()
        
        return religionMap.mapValues { (_, count) -> 
            BigDecimal(count * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Add population count for a religion
     */
    fun addReligion(religion: ReligionType, count: Int) {
        religionMap[religion] = (religionMap[religion] ?: 0) + count
    }
    
    /**
     * Get the dominant religion in the distribution
     */
    fun getDominantReligion(): ReligionType? {
        return religionMap.maxByOrNull { it.value }?.key
    }
    
    /**
     * Check if the distribution is religiously diverse (no religion has >75% population)
     */
    fun isReligiouslyDiverse(): Boolean {
        val percentages = getPercentages()
        return percentages.values.none { it.toDouble() > 75.0 }
    }
}
