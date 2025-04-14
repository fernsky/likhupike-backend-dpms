package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable

/**
 * Enumeration of caste and ethnic groups for demographic classification.
 */
enum class CasteType {
    CHETTRI,
    BRAHMAN_HILL,
    LIMBU,
    SHERPA,
    TAMANG,
    RAI,
    MAGAR,
    THARU,
    NEWAR,
    KAMI,
    GURUNG,
    DAMAI,
    SARKI,
    MUSLIM,
    MAJHI,
    THAMI,
    CHHANTYAL,
    BHOTE,
    SANYASI,
    THAKURI,
    KUMAL,
    RAJBANSHI,
    SUNUWAR,
    BRAHMAN_TARAI,
    DHANUK,
    CHEPANG,
    SANTHAL,
    GHARTI,
    DHIMAL,
    JIREL,
    DARAI,
    DURA,
    MECHE,
    LEPCHA,
    DUSADH,
    MARWADI,
    BAJHANGI,
    BOTE,
    BAHING,
    RAJI,
    DOM,
    THAKALI,
    YADAV,
    KISAN,
    UNIDENTIFIED,
    OTHER;
    
    companion object {
        /**
         * Returns a list of major caste groups for reporting purposes
         */
        fun getMajorGroups(): List<CasteType> {
            return listOf(
                CHETTRI, BRAHMAN_HILL, LIMBU, TAMANG, RAI, MAGAR, THARU, NEWAR, KAMI, GURUNG, OTHER
            )
        }
        
        /**
         * Returns a mapping of castes to their broader categories for aggregated analysis
         */
        fun getCasteCategories(): Map<String, List<CasteType>> {
            return mapOf(
                "BRAHMIN_CHHETRI" to listOf(BRAHMAN_HILL, BRAHMAN_TARAI, CHETTRI, THAKURI, SANYASI),
                "JANAJATI" to listOf(MAGAR, TAMANG, NEWAR, RAI, GURUNG, LIMBU, SHERPA, THAMI, THAKALI, 
                    JIREL, SUNUWAR, CHEPANG, MAJHI, DURA, LEPCHA, BHOTE, BAHING, THARU, DHIMAL, 
                    KUMAL, RAJBANSHI, BOTE, DARAI, RAJI),
                "DALIT" to listOf(KAMI, DAMAI, SARKI, DOM, DUSADH),
                "MADHESI" to listOf(YADAV, DHANUK),
                "MUSLIM" to listOf(MUSLIM),
                "OTHER" to listOf(MARWADI, KISAN, MECHE, SANTHAL, UNIDENTIFIED, OTHER)
            )
        }
    }
}

/**
 * Value object representing a distribution of population by caste
 */
data class CasteDistribution(
    val casteMap: MutableMap<CasteType, Int> = mutableMapOf()
) : Serializable {
    
    /**
     * Get total population across all castes
     */
    fun getTotal(): Int = casteMap.values.sum()
    
    /**
     * Get percentage distribution by caste
     */
    fun getPercentages(): Map<CasteType, Double> {
        val total = getTotal()
        if (total == 0) return emptyMap()
        
        return casteMap.mapValues { (_, count) -> 
            (count.toDouble() / total) * 100.0 
        }
    }
    
    /**
     * Get aggregated distribution by caste category
     */
    fun getAggregatedByCategory(): Map<String, Int> {
        val categoryMap = mutableMapOf<String, Int>()
        val casteCategories = CasteType.getCasteCategories()
        
        casteMap.forEach { (caste, count) ->
            val category = casteCategories.entries.find { it.value.contains(caste) }?.key ?: "OTHER"
            categoryMap[category] = (categoryMap[category] ?: 0) + count
        }
        
        return categoryMap
    }
    
    /**
     * Add population count for a caste
     */
    fun addCaste(caste: CasteType, count: Int) {
        casteMap[caste] = (casteMap[caste] ?: 0) + count
    }
}
