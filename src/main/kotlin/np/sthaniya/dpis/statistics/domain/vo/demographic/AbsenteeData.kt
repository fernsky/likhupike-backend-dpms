package np.sthaniya.dpis.statistics.domain.vo.demographic

import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

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
    UNKNOWN;                               // थाहा छैन
}

/**
 * Enumeration of educational level categories used for demographic analysis.
 */
enum class EducationalLevelType {
    CHILD_DEVELOPMENT_CENTER, // Child Development Center / Montessori
    NURSERY,                // Nursery/Kindergarten
    CLASS_1,                // Class 1
    CLASS_2,                // Class 2
    CLASS_3,                // Class 3
    CLASS_4,                // Class 4
    CLASS_5,                // Class 5
    CLASS_6,                // Class 6
    CLASS_7,                // Class 7
    CLASS_8,                // Class 8
    CLASS_9,                // Class 9
    CLASS_10,               // Class 10
    SLC_LEVEL,              // SEE/SLC or equivalent
    CLASS_12_LEVEL,         // Class 12 or PCL or equivalent
    BACHELOR_LEVEL,         // Bachelor's degree or equivalent
    MASTERS_LEVEL,          // Master's degree or equivalent
    PHD_LEVEL,              // PhD or equivalent
    OTHER,                  // Other educational qualifications
    INFORMAL_EDUCATION,     // Informal education
    EDUCATED,               // Literate
    UNKNOWN;                // Unknown
}

/**
 * Value object representing detailed absentee population data
 */
data class AbsenteeData(
    val totalAbsenteePopulation: Int = 0,
    val ageGenderDistribution: MutableMap<Pair<AbsenteeAgeGroup, Gender>, Int> = mutableMapOf(),
    val reasonDistribution: MutableMap<AbsenceReason, Int> = mutableMapOf(),
    val locationDistribution: MutableMap<AbsenteeLocationType, Int> = mutableMapOf(),
    val educationLevelDistribution: MutableMap<EducationalLevelType, Int> = mutableMapOf(),
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
    fun addEducationLevel(level: EducationalLevelType, count: Int) {
        educationLevelDistribution[level] = (educationLevelDistribution[level] ?: 0) + count
    }
    
    /**
     * Add absentee data for a specific destination country
     */
    fun addDestinationCountry(country: String, count: Int) {
        destinationCountryDistribution[country] = (destinationCountryDistribution[country] ?: 0) + count
    }
    
    /**
     * Calculate literacy rate (percentage of population at least "EDUCATED" level)
     */
    fun getLiteracyRate(): BigDecimal {
        val total = educationLevelDistribution.values.sum()
        if (total == 0) return BigDecimal.ZERO
        
        val literatePopulation = educationLevelDistribution.entries
            .filter { (level, _) -> 
                level != EducationalLevelType.UNKNOWN && 
                level != EducationalLevelType.CHILD_DEVELOPMENT_CENTER && 
                level != EducationalLevelType.NURSERY 
            }
            .sumOf { it.value }
        
        return BigDecimal(literatePopulation * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
    }
    
    /**
     * Calculate higher education rate (percentage with bachelor's degree or higher)
     */
    fun getHigherEducationRate(): BigDecimal {
        val total = educationLevelDistribution.values.sum()
        if (total == 0) return BigDecimal.ZERO
        
        val higherEducationPopulation = educationLevelDistribution.entries
            .filter { (level, _) -> 
                level == EducationalLevelType.BACHELOR_LEVEL || 
                level == EducationalLevelType.MASTERS_LEVEL || 
                level == EducationalLevelType.PHD_LEVEL 
            }
            .sumOf { it.value }
        
        return BigDecimal(higherEducationPopulation * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
    }
    
    /**
     * Get aggregated distribution by educational category
     */
    fun getEducationalLevelCategories(): Map<String, Int> {
        val educationGroups = mapOf(
            "PRE_PRIMARY" to listOf(EducationalLevelType.CHILD_DEVELOPMENT_CENTER, EducationalLevelType.NURSERY),
            "PRIMARY" to listOf(EducationalLevelType.CLASS_1, EducationalLevelType.CLASS_2, EducationalLevelType.CLASS_3, 
                               EducationalLevelType.CLASS_4, EducationalLevelType.CLASS_5),
            "LOWER_SECONDARY" to listOf(EducationalLevelType.CLASS_6, EducationalLevelType.CLASS_7, EducationalLevelType.CLASS_8),
            "SECONDARY" to listOf(EducationalLevelType.CLASS_9, EducationalLevelType.CLASS_10, EducationalLevelType.SLC_LEVEL),
            "HIGHER_SECONDARY" to listOf(EducationalLevelType.CLASS_12_LEVEL),
            "HIGHER_EDUCATION" to listOf(EducationalLevelType.BACHELOR_LEVEL, EducationalLevelType.MASTERS_LEVEL, 
                                        EducationalLevelType.PHD_LEVEL),
            "OTHER_EDUCATION" to listOf(EducationalLevelType.OTHER, EducationalLevelType.INFORMAL_EDUCATION, 
                                       EducationalLevelType.EDUCATED, EducationalLevelType.UNKNOWN)
        )
        
        val categoryMap = mutableMapOf<String, Int>()
        
        educationLevelDistribution.forEach { (level, count) ->
            val category = educationGroups.entries.find { it.value.contains(level) }?.key ?: "OTHER_EDUCATION"
            categoryMap[category] = (categoryMap[category] ?: 0) + count
        }
        
        return categoryMap
    }
}
