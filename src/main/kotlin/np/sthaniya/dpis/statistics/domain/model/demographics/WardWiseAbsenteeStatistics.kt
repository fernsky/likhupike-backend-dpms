package np.sthaniya.dpis.statistics.domain.model.demographics

import np.sthaniya.dpis.statistics.domain.model.WardStatistics
import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeAgeGroup
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeData
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenceReason
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeLocationType
import np.sthaniya.dpis.statistics.domain.vo.demographic.EducationalLevelType
import np.sthaniya.dpis.statistics.domain.event.demographics.AbsenteeStatsUpdatedEvent
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Ward-level statistics aggregating absentee population data.
 */
class WardWiseAbsenteeStatistics : WardStatistics() {
    
    /**
     * Detailed absentee population data
     * This now also includes educational level distribution
     */
    var absenteeData: AbsenteeData = AbsenteeData()
    
    /**
     * Percentage of ward population that is absent
     */
    var absenteePercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Percentage of absentees who are abroad (foreign country)
     */
    var foreignAbsenteePercentage: BigDecimal = BigDecimal.ZERO
    
    /**
     * Primary reason for absence in this ward
     */
    var primaryAbsenceReason: AbsenceReason? = null
    
    /**
     * Top destination for absentees from this ward
     */
    var topDestinationCountry: String? = null
    
    /**
     * Average age of absentee population
     */
    var averageAbsenteeAge: BigDecimal? = null
    
    /**
     * Gender ratio of absentee population (males per 100 females)
     */
    var absenteeSexRatio: BigDecimal = BigDecimal.ZERO
    
    init {
        this.statisticalGroup = "demographics"
        this.statisticalCategory = "ward_absentee_population"
    }
    
    /**
     * Calculate absentee population metrics
     */
    fun calculateAbsenteeMetrics(totalWardPopulation: Int) {
        // Calculate absentee percentage
        absenteePercentage = absenteeData.getAbsenteePercentage(totalWardPopulation)
        
        // Calculate foreign absentee percentage
        foreignAbsenteePercentage = absenteeData.getForeignAbsenteePercentage()
        
        // Determine primary absence reason
        calculatePrimaryAbsenceReason()
        
        // Determine top destination country
        calculateTopDestinationCountry()
        
        // Calculate absentee sex ratio
        calculateAbsenteeSexRatio()
        
        // Calculate average absentee age
        calculateAverageAbsenteeAge()
    }
    
    /**
     * Calculate the primary reason for absence
     */
    private fun calculatePrimaryAbsenceReason() {
        if (absenteeData.reasonDistribution.isEmpty()) {
            primaryAbsenceReason = null
            return
        }
        
        primaryAbsenceReason = absenteeData.reasonDistribution.maxByOrNull { it.value }?.key
    }
    
    /**
     * Calculate the top destination country
     */
    private fun calculateTopDestinationCountry() {
        val foreignLocations = absenteeData.locationDistribution[AbsenteeLocationType.ANOTHER_COUNTRY] ?: 0
        
        if (foreignLocations == 0 || absenteeData.destinationCountryDistribution.isEmpty()) {
            topDestinationCountry = null
            return
        }
        
        topDestinationCountry = absenteeData.destinationCountryDistribution.maxByOrNull { it.value }?.key
    }
    
    /**
     * Calculate sex ratio of absentee population
     */
    private fun calculateAbsenteeSexRatio() {
        val genderDistribution = absenteeData.getGenderDistribution()
        val maleCount = genderDistribution[Gender.MALE] ?: 0
        val femaleCount = genderDistribution[Gender.FEMALE] ?: 0
        
        absenteeSexRatio = if (femaleCount > 0) {
            BigDecimal(maleCount * 100).divide(BigDecimal(femaleCount), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
    }
    
    /**
     * Calculate average age of absentee population
     * This is an approximation based on age group midpoints
     */
    private fun calculateAverageAbsenteeAge() {
        val ageDistribution = absenteeData.getAgeDistribution()
        if (ageDistribution.isEmpty()) {
            averageAbsenteeAge = null
            return
        }
        
        // Define midpoints for age groups
        val ageMidpoints = mapOf(
            AbsenteeAgeGroup.AGE_0_4 to 2.5,
            AbsenteeAgeGroup.AGE_5_9 to 7.5,
            AbsenteeAgeGroup.AGE_10_14 to 12.5,
            AbsenteeAgeGroup.AGE_15_19 to 17.5,
            AbsenteeAgeGroup.AGE_20_24 to 22.5,
            AbsenteeAgeGroup.AGE_25_29 to 27.5,
            AbsenteeAgeGroup.AGE_30_34 to 32.5,
            AbsenteeAgeGroup.AGE_35_39 to 37.5,
            AbsenteeAgeGroup.AGE_40_44 to 42.5,
            AbsenteeAgeGroup.AGE_45_49 to 47.5,
            AbsenteeAgeGroup.AGE_50_AND_ABOVE to 60.0 // Assumed midpoint for 50+ group
        )
        
        var totalWeightedAge = 0.0
        var totalAbsentees = 0
        
        ageDistribution.forEach { (ageGroup, count) ->
            val midpoint = ageMidpoints[ageGroup] ?: 0.0
            totalWeightedAge += midpoint * count
            totalAbsentees += count
        }
        
        averageAbsenteeAge = if (totalAbsentees > 0) {
            BigDecimal(totalWeightedAge / totalAbsentees).setScale(1, RoundingMode.HALF_UP)
        } else null
    }
    
    /**
     * Get distribution of absentees by reason as percentages
     */
    fun getReasonDistributionPercentages(): Map<AbsenceReason, BigDecimal> {
        return absenteeData.getReasonPercentages()
    }
    
    /**
     * Get distribution of absentees by location as percentages
     */
    fun getLocationDistributionPercentages(): Map<AbsenteeLocationType, BigDecimal> {
        return absenteeData.getLocationPercentages()
    }
    
    /**
     * Get breakdown of foreign absentee destinations
     */
    fun getDestinationCountryPercentages(): Map<String, BigDecimal> {
        val foreignAbsentees = absenteeData.locationDistribution[AbsenteeLocationType.ANOTHER_COUNTRY] ?: 0
        if (foreignAbsentees == 0) return emptyMap()
        
        return absenteeData.destinationCountryDistribution.mapValues { (_, count) ->
            BigDecimal(count * 100).divide(BigDecimal(foreignAbsentees), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Get gender breakdown of absentees
     */
    fun getGenderBreakdown(): Map<Gender, BigDecimal> {
        val total = absenteeData.totalAbsenteePopulation
        if (total == 0) return emptyMap()
        
        val genderDistribution = absenteeData.getGenderDistribution()
        
        return genderDistribution.mapValues { (_, count) ->
            BigDecimal(count * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        }
    }
    
    /**
     * Calculate literacy rate of absentee population
     */
    fun getAbsenteeLiteracyRate(): BigDecimal {
        val total = absenteeData.educationLevelDistribution.values.sum()
        if (total == 0) return BigDecimal.ZERO
        
        val literatePopulation = absenteeData.educationLevelDistribution.entries
            .filter { (level, _) -> 
                level != EducationalLevelType.UNKNOWN && 
                level != EducationalLevelType.CHILD_DEVELOPMENT_CENTER && 
                level != EducationalLevelType.NURSERY 
            }
            .sumOf { it.value }
        
        return BigDecimal(literatePopulation * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
    }
    
    /**
     * Get educational level breakdown by category
     */
    fun getEducationLevelCategoryBreakdown(): Map<String, Int> {
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
        
        absenteeData.educationLevelDistribution.forEach { (level, count) ->
            val category = educationGroups.entries.find { it.value.contains(level) }?.key ?: "OTHER_EDUCATION"
            categoryMap[category] = (categoryMap[category] ?: 0) + count
        }
        
        return categoryMap
    }
    
    /**
     * Get higher education rate of absentee population
     */
    fun getAbsenteeHigherEducationRate(): BigDecimal {
        val total = absenteeData.educationLevelDistribution.values.sum()
        if (total == 0) return BigDecimal.ZERO
        
        val higherEducationPopulation = absenteeData.educationLevelDistribution.entries
            .filter { (level, _) -> 
                level == EducationalLevelType.BACHELOR_LEVEL || 
                level == EducationalLevelType.MASTERS_LEVEL || 
                level == EducationalLevelType.PHD_LEVEL 
            }
            .sumOf { it.value }
        
        return BigDecimal(higherEducationPopulation * 100).divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
    }

    override fun getComparisonValue(): Double? {
        // Use absentee percentage as comparison metric
        return absenteePercentage.toDouble()
    }

    override fun getTotalWardPopulation(): Int {
        // Not directly available from absentee data, return 0 as fallback
        return 0
    }
    
    override fun getTotalWardHouseholds(): Int {
        // Not directly relevant for absentee statistics, return 0 as fallback
        return 0
    }

    override fun getKeyMetrics(): Map<String, Any> {
        val genderDistribution = absenteeData.getGenderDistribution()
        
        return mapOf(
            "totalAbsenteePopulation" to absenteeData.totalAbsenteePopulation,
            "maleAbsentees" to (genderDistribution[Gender.MALE] ?: 0),
            "femaleAbsentees" to (genderDistribution[Gender.FEMALE] ?: 0),
            "otherAbsentees" to (genderDistribution[Gender.OTHER] ?: 0),
            "absenteePercentage" to absenteePercentage,
            "foreignAbsenteePercentage" to foreignAbsenteePercentage,
            "primaryAbsenceReason" to (primaryAbsenceReason?.name ?: "UNKNOWN"),
            "topDestinationCountry" to (topDestinationCountry ?: "UNKNOWN"),
            "averageAbsenteeAge" to (averageAbsenteeAge ?: BigDecimal.ZERO),
            "absenteeSexRatio" to absenteeSexRatio,
            "absenteeLiteracyRate" to getAbsenteeLiteracyRate(),
            "absenteeHigherEducationRate" to getAbsenteeHigherEducationRate(),
            "educationBreakdown" to getEducationLevelCategoryBreakdown()
        )
    }
    
    override fun getProducedEvents(): List<Any> {
        val genderDistribution = absenteeData.getGenderDistribution()
        
        return listOf(
            AbsenteeStatsUpdatedEvent(
                statisticsId = this.id!!,
                wardId = this.wardId!!,
                wardNumber = this.wardNumber!!,
                totalAbsenteePopulation = absenteeData.totalAbsenteePopulation,
                maleAbsenteePopulation = genderDistribution[Gender.MALE] ?: 0,
                femaleAbsenteePopulation = genderDistribution[Gender.FEMALE] ?: 0,
                otherAbsenteePopulation = genderDistribution[Gender.OTHER] ?: 0,
                foreignAbsenteePercentage = this.foreignAbsenteePercentage.toDouble(),
                mainAbsenceReason = this.primaryAbsenceReason?.name,
                mainDestinationCountry = this.topDestinationCountry,
                calculationDate = this.calculationDate
            )
        )
    }
}
