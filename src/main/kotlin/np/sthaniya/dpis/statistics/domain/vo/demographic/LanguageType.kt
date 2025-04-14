package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Enumeration of language categories used for linguistic classification.
 */
enum class LanguageType {
    NEPALI,
    LIMBU,
    RAI,
    HINDI,
    NEWARI,
    SHERPA,
    TAMANG,
    MAITHILI,
    BHOJPURI,
    GURUNG,
    MAGAR,
    THARU,
    AVADHI,
    BAJJIKA,
    URDU,
    TIBETAN,
    DOTELI,
    ENGLISH,
    SANSKRIT,
    BANTAWA,
    RAJBANSHI,
    DHIMAL,
    KULUNG,
    SUNUWAR,
    MAJHI,
    YAKKHA,
    THAMI,
    CHAMLING,
    KHAM,
    THULUNG,
    OTHER;
    
    companion object {
        /**
         * Returns list of major languages for reporting purposes
         */
        fun getMajorLanguages(): List<LanguageType> {
            return listOf(
                NEPALI, MAITHILI, BHOJPURI, THARU, TAMANG, NEWARI, HINDI, LIMBU, RAI, OTHER
            )
        }
    }
}

/**
 * Value object representing a distribution of population by mother tongue
 */
data class LanguageDistribution(
    val languageMap: MutableMap<LanguageType, Int> = mutableMapOf()
) : Serializable {
    
    /**
     * Get total population across all languages
     */
    fun getTotal(): Int = languageMap.values.sum()
    
    /**
     * Get percentage distribution by language
     */
    fun getPercentages(): Map<LanguageType, BigDecimal> {
        val total = getTotal()
        if (total == 0) return emptyMap()
        
        return languageMap.mapValues { (_, count) -> 
            BigDecimal(count * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Add population count for a language
     */
    fun addLanguage(language: LanguageType, count: Int) {
        languageMap[language] = (languageMap[language] ?: 0) + count
    }
    
    /**
     * Get the dominant language in the distribution
     */
    fun getDominantLanguage(): LanguageType? {
        return languageMap.maxByOrNull { it.value }?.key
    }
}
