package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import np.sthaniya.dpis.statistics.domain.vo.Gender

/**
 * Enumeration of age group categories used for absentee population analysis.
 */
enum class AbsenteeAgeGroup(val minAge: Int, val maxAge: Int?) {
    AGE_0_4(0, 4),
    AGE_5_9(5, 9),
    AGE_10_14(10, 14),
    AGE_15_19(15, 19),
    AGE_20_24(20, 24),
    AGE_25_29(25, 29),
    AGE_30_34(30, 34),
    AGE_35_39(35, 39),
    AGE_40_44(40, 44),
    AGE_45_49(45, 49),
    AGE_50_AND_ABOVE(50, null);
    
    companion object {
        /**
         * Find the appropriate age group for a given age
         */
        fun fromAge(age: Int): AbsenteeAgeGroup {
            return values().find { ageGroup -> 
                age >= ageGroup.minAge && (ageGroup.maxAge == null || age <= ageGroup.maxAge)
            } ?: AGE_50_AND_ABOVE
        }
    }
}

/**
 * Enumeration of reasons for population absence used in demographic tracking.
 */
enum class AbsenceReason {
    BUSINESS,          // व्यापार/व्यवसाय
    PRIVATE_JOB,       // निजी नोकरी
    GOVERNMENTAL_JOB,  // सरकारी जागिर
    STUDY,             // अध्ययन/तालिम
    WORK,              // काम/जागिरको खोजी
    DEPENDENT,         // आश्रित
    CONFLICT,          // द्वन्द्व
    OTHER,             // अन्य
    UNKNOWN;           // थाहा छैन
}

/**
 * Enumeration of location types for tracking where absent population members are located.
 */
enum class AbsenteeLocationType {
    ANOTHER_DISTRICT,                      // नेपालको अर्को जिल्ला
    ANOTHER_MUNICIPALITY_IN_SAME_DISTRICT, // यही जिल्लाको अर्को स्थानीय तह
    ANOTHER_COUNTRY,                       // बिदेश
    UNKNOWN;                              // थाहा छैन
}

/**
 * Value object representing detailed absentee population data
 */
data class AbsenteeData(
    val totalAbsenteePopulation: Int = 0,
    val ageGenderDistribution: MutableMap<Pair<AbsenteeAgeGroup, Gender>, Int> = mutableMapOf(),
    val reasonDistribution: MutableMap<AbsenceReason, Int> = mutableMapOf(),
    val locationDistribution: MutableMap<AbsenteeLocationType, Int> = mutableMapOf(),
    val educationLevelDistribution: MutableMap<String, Int> = mutableMapOf(),
    val destinationCountryDistribution: MutableMap<String, Int> = mutableMapOf()
) : Serializable {
    
    /**
     * Get percentage of total population that is absent
     */
    fun getAbsenteePercentage(totalPopulation: Int): BigDecimal {
        return if (totalPopulation > 0) {
            BigDecimal(totalAbsenteePopulation * 100)
                .divide(BigDecimal(totalPopulation), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Get gender distribution of absentee population
     */
    fun getGenderDistribution(): Map<Gender, Int> {
        return ageGenderDistribution.entries
            .groupBy { it.key.second }
            .mapValues { entry -> entry.value.sumOf { it.value } }
    }
    
    /**
     * Get age distribution of absentee population
     */
    fun getAgeDistribution(): Map<AbsenteeAgeGroup, Int> {
        return ageGenderDistribution.entries
            .groupBy { it.key.first }
            .mapValues { entry -> entry.value.sumOf { it.value } }
    }
    
    /**
     * Get percentage distribution by absence reason
     */
    fun getReasonPercentages(): Map<AbsenceReason, BigDecimal> {
        val total = reasonDistribution.values.sum()
        if (total == 0) return emptyMap()
        
        return reasonDistribution.mapValues { (_, count) -> 
            BigDecimal(count * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Get percentage distribution by location
     */
    fun getLocationPercentages(): Map<AbsenteeLocationType, BigDecimal> {
        val total = locationDistribution.values.sum()
        if (total == 0) return emptyMap()
        
        return locationDistribution.mapValues { (_, count) -> 
            BigDecimal(count * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Get percentage of absentees in foreign countries
     */
    fun getForeignAbsenteePercentage(): BigDecimal {
        val foreignCount = locationDistribution[AbsenteeLocationType.ANOTHER_COUNTRY] ?: 0
        return if (totalAbsenteePopulation > 0) {
            BigDecimal(foreignCount * 100).divide(BigDecimal(totalAbsenteePopulation), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Get top destination countries
     */
    fun getTopDestinationCountries(limit: Int = 5): List<Pair<String, Int>> {
        return destinationCountryDistribution.entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { Pair(it.key, it.value) }
    }
    
    /**
     * Add absentee data for a specific age group and gender
     */
    fun addAgeGender(ageGroup: AbsenteeAgeGroup, gender: Gender, count: Int) {
        val key = Pair(ageGroup, gender)
        ageGenderDistribution[key] = (ageGenderDistribution[key] ?: 0) + count
    }
    
    /**
     * Add absentee data for a specific reason
     */
    fun addReason(reason: AbsenceReason, count: Int) {
        reasonDistribution[reason] = (reasonDistribution[reason] ?: 0) + count
    }
    
    /**
     * Add absentee data for a specific location
     */
    fun addLocation(location: AbsenteeLocationType, count: Int) {
        locationDistribution[location] = (locationDistribution[location] ?: 0) + count
    }
    
    /**
     * Add absentee data for a specific education level
     */
    fun addEducationLevel(level: String, count: Int) {
        educationLevelDistribution[level] = (educationLevelDistribution[level] ?: 0) + count
    }
    
    /**
     * Add absentee data for a specific destination country
     */
    fun addDestinationCountry(country: String, count: Int) {
        destinationCountryDistribution[country] = (destinationCountryDistribution[country] ?: 0) + count
    }
}
