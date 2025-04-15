package np.sthaniya.dpis.statistics.infrastructure.service.demographics

import np.sthaniya.dpis.statistics.domain.model.demographics.WardWiseAbsenteeStatistics
import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeAgeGroup
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeLocationType
import np.sthaniya.dpis.statistics.domain.repository.demographics.WardWiseAbsenteeStatisticsRepository
import np.sthaniya.dpis.statistics.domain.service.QualityAssessment
import np.sthaniya.dpis.statistics.domain.service.ValidationIssue
import np.sthaniya.dpis.statistics.domain.service.demographics.AbsenteeStatisticsService
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenceReason
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeData
import np.sthaniya.dpis.statistics.infrastructure.service.event.EventSourcingService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Implementation of AbsenteeStatisticsService
 */
@Service
class AbsenteeStatisticsServiceImpl(
    private val repository: WardWiseAbsenteeStatisticsRepository,
    private val eventSourcingService: EventSourcingService,
    private val validationService: AbsenteeStatisticsValidationService
) : AbsenteeStatisticsService {
    
    private val logger = LoggerFactory.getLogger(AbsenteeStatisticsServiceImpl::class.java)

    @Transactional
    override fun calculate(
        inputData: Map<String, Any>,
        referenceDate: LocalDateTime,
        calculatedBy: UUID?
    ): WardWiseAbsenteeStatistics {
        logger.info("Calculating absentee statistics for input data with reference date: $referenceDate")
        
        // Extract required parameters from inputData
        val wardId = inputData["wardId"] as? UUID
            ?: throw IllegalArgumentException("Ward ID is required")
        val wardNumber = inputData["wardNumber"] as? Int
            ?: throw IllegalArgumentException("Ward number is required")
        val totalPopulation = inputData["totalPopulation"] as? Int
            ?: throw IllegalArgumentException("Total population is required")
        
        // Extract or create absentee data
        val absenteeData = if (inputData.containsKey("absenteeData") && inputData["absenteeData"] is AbsenteeData) {
            inputData["absenteeData"] as AbsenteeData
        } else {
            // Try to construct absentee data from individual components
            constructAbsenteeData(inputData)
        }
        
        return calculateForWard(
            wardId = wardId,
            wardNumber = wardNumber,
            totalPopulation = totalPopulation,
            absenteeData = absenteeData,
            referenceDate = referenceDate,
            calculatedBy = calculatedBy
        )
    }

    @Transactional
    override fun calculateForWard(
        wardId: UUID,
        wardNumber: Int,
        totalPopulation: Int,
        absenteeData: AbsenteeData,
        referenceDate: LocalDateTime,
        calculatedBy: UUID?
    ): WardWiseAbsenteeStatistics {
        logger.info("Calculating absentee statistics for ward $wardNumber (id: $wardId)")
        
        // Create new statistics instance or update existing
        val statistics = repository.findByWardId(wardId) ?: WardWiseAbsenteeStatistics().apply {
            this.wardId = wardId
            this.wardNumber = wardNumber
        }
        
        // Update metadata
        statistics.apply {
            this.referenceDate = referenceDate
            this.calculationDate = LocalDateTime.now()
            this.calculatedBy = calculatedBy
            this.statisticalGroup = "demographics"
            this.statisticalCategory = "ward_absentee_population"
            this.methodologyVersion = "1.0"
            this.applicablePopulation = totalPopulation
            this.isEstimate = false
            this.isValid = true
        }
        
        // Set absentee data and calculate metrics
        statistics.absenteeData = absenteeData
        statistics.calculateAbsenteeMetrics(totalPopulation)
        
        // Validate before saving
        val validationIssues = validate(statistics)
        val criticalIssues = validationIssues.filter { 
            it.severity == ValidationIssue.IssueSeverity.CRITICAL 
        }
        
        if (criticalIssues.isNotEmpty()) {
            throw IllegalStateException(
                "Critical validation errors in absentee statistics: ${criticalIssues.joinToString { it.message }}"
            )
        }
        
        // Save with audit
        val savedStatistics = if (calculatedBy != null) {
            repository.saveWithAudit(statistics, calculatedBy)
        } else {
            repository.save(statistics)
        }
        
        // Store events
        val events = savedStatistics.getProducedEvents()
        if (events.isNotEmpty()) {
            eventSourcingService.saveEvents(savedStatistics, events, calculatedBy)
        }
        
        return savedStatistics
    }

    override fun validate(entity: WardWiseAbsenteeStatistics): List<ValidationIssue> {
        return validationService.validateAbsenteeStatistics(entity)
    }

    override fun rebuildFromEvents(entityId: UUID, asOfDate: LocalDateTime): WardWiseAbsenteeStatistics? {
        logger.info("Rebuilding absentee statistics with ID: $entityId as of $asOfDate")
        
        // Get current state as base or create new
        val currentEntity = repository.findById(entityId) ?: return null
        
        // Create a new instance to avoid modifying the original
        val rebuildEntity = WardWiseAbsenteeStatistics().apply {
            // Copy only ID and essential attributes that don't change
            this.wardId = currentEntity.wardId
            this.wardNumber = currentEntity.wardNumber
        }
        
        // Rebuild entity from events
        return eventSourcingService.rebuildEntity(rebuildEntity, asOfDate)
    }

    override fun assessQuality(entity: WardWiseAbsenteeStatistics): QualityAssessment {
        return validationService.assessQuality(entity)
    }

    @Transactional(readOnly = true)
    override fun aggregate(entityIds: List<UUID>, aggregationParams: Map<String, Any>): Map<String, Any> {
        // Use the repository's aggregation capability
        return repository.calculateAggregateForWards(entityIds)
    }

    @Transactional(readOnly = true)
    override fun compare(entity1Id: UUID, entity2Id: UUID): Map<String, Any> {
        // Use the repository's comparison capability
        return repository.compareWards(entity1Id, entity2Id)
    }

    @Transactional(readOnly = true)
    override fun findWardsWithSignificantForeignAbsentees(thresholdPercentage: BigDecimal): List<WardWiseAbsenteeStatistics> {
        return repository.findByForeignAbsenteePercentageAbove(thresholdPercentage)
    }

    @Transactional(readOnly = true)
    override fun findWardsWithDominantAbsenceReason(reason: AbsenceReason): List<WardWiseAbsenteeStatistics> {
        return repository.findByPrimaryAbsenceReason(reason)
    }

    @Transactional(readOnly = true)
    override fun findWardsWithGenderImbalance(ratioThreshold: BigDecimal): List<WardWiseAbsenteeStatistics> {
        return repository.findByAbsenteeGenderImbalance(ratioThreshold)
    }

    @Transactional(readOnly = true)
    override fun getDestinationCountriesBreakdown(): Map<String, Int> {
        return repository.getAggregatedStatsByDestinationCountry()
    }

    @Transactional(readOnly = true)
    override fun getTopAbsenceReasons(limit: Int): Map<AbsenceReason, Double> {
        val reasonStats = repository.getAggregatedStatsByReason()
        val total = reasonStats.values.sum().toDouble()
        
        if (total == 0.0) return emptyMap()
        
        // Calculate percentages and take top N
        return reasonStats.entries
            .map { it.key to (it.value.toDouble() / total * 100) }
            .sortedByDescending { it.second }
            .take(limit)
            .toMap()
    }

    @Transactional(readOnly = true)
    override fun getWardsWithHighestAbsenteeEducation(limit: Int): List<WardWiseAbsenteeStatistics> {
        // Get all valid ward statistics
        val allStats = repository.findValidByCriteria(emptyMap())
        
        // Calculate higher education rates for each and sort
        return allStats
            .map { it to it.absenteeData.getHigherEducationRate().toDouble() }
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }

    @Transactional(readOnly = true)
    override fun generateAbsenteeTrendsReport(
        municipalityId: UUID?,
        asOfDate: LocalDateTime
    ): Map<String, Any> {
        // Filter stats by municipality if provided
        val wardStats = if (municipalityId != null) {
            repository.findAllByMunicipality(municipalityId)
        } else {
            repository.findValidByCriteria(emptyMap())
        }
        
        // Get reference data from 1 year ago for comparison
        val oneYearAgo = asOfDate.minusYears(1)
        
        // Overall statistics
        val totalAbsentees = wardStats.sumOf { it.absenteeData.totalAbsenteePopulation }
        val totalPopulation = wardStats.sumOf { it.applicablePopulation ?: 0 }
        val overallAbsenteePercentage = if (totalPopulation > 0) {
            BigDecimal(totalAbsentees * 100.0 / totalPopulation).setScale(2, BigDecimal.ROUND_HALF_UP)
        } else BigDecimal.ZERO
        
        // Prepare destination countries data
        val destinationCountries = repository.getAggregatedStatsByDestinationCountry()
        val topDestinations = destinationCountries.entries
            .sortedByDescending { it.value }
            .take(10)
            .associate { it.key to it.value }
        
        // Prepare reasons data
        val reasons = repository.getAggregatedStatsByReason()
        
        // Get trend data by comparing with historical data
        val wardsWithSignificantIncrease = repository.findWardsWithSignificantAbsenteeIncrease(
            referenceDate = oneYearAgo,
            thresholdPercentage = BigDecimal(10) // 10% increase threshold
        )
        
        return mapOf(
            "reportGeneratedAt" to LocalDateTime.now(),
            "asOfDate" to asOfDate,
            "totalWards" to wardStats.size,
            "totalAbsentees" to totalAbsentees,
            "overallAbsenteePercentage" to overallAbsenteePercentage,
            "topDestinationCountries" to topDestinations,
            "reasonsBreakdown" to reasons,
            "wardsWithSignificantIncrease" to wardsWithSignificantIncrease.map { 
                mapOf(
                    "wardId" to it.wardId,
                    "wardNumber" to it.wardNumber,
                    "absenteePercentage" to it.absenteePercentage
                )
            },
            "genderComparison" to repository.getAbsenteeGenderComparison(),
            "locationBreakdown" to repository.getAggregatedStatsByLocation(),
            "topWardsByAbsenteePercentage" to wardStats
                .sortedByDescending { it.absenteePercentage }
                .take(5)
                .map { 
                    mapOf(
                        "wardId" to it.wardId,
                        "wardNumber" to it.wardNumber,
                        "absenteePercentage" to it.absenteePercentage
                    )
                }
        )
    }

    /**
     * Construct AbsenteeData from individual components in the input data
     */
    private fun constructAbsenteeData(inputData: Map<String, Any>): AbsenteeData {
        val totalAbsenteePopulation = inputData["totalAbsenteePopulation"] as? Int ?: 0
        
        val absenteeData = AbsenteeData(totalAbsenteePopulation)
        
        // If we have gender breakdown by age group, add it
        AbsenteeAgeGroup.values().forEach { ageGroup ->
            inputData["maleAbsenteeCount_${ageGroup.name}"]?.let { 
            absenteeData.addAgeGender(ageGroup, Gender.MALE, it as Int) 
            }
            inputData["femaleAbsenteeCount_${ageGroup.name}"]?.let { 
            absenteeData.addAgeGender(ageGroup, Gender.FEMALE, it as Int) 
            }
            inputData["otherAbsenteeCount_${ageGroup.name}"]?.let { 
            absenteeData.addAgeGender(ageGroup, Gender.OTHER, it as Int) 
            }
        }
        
        // Add any reason distribution data
        @Suppress("UNCHECKED_CAST")
        (inputData["reasonDistribution"] as? Map<String, Int>)?.forEach { (reasonStr, count) ->
            try {
                val reason = AbsenceReason.valueOf(reasonStr)
                absenteeData.addReason(reason, count)
            } catch (e: IllegalArgumentException) {
                logger.warn("Invalid reason in input data: $reasonStr")
            }
        }
        
        // Add location data if available
        @Suppress("UNCHECKED_CAST")
        (inputData["locationDistribution"] as? Map<String, Int>)?.forEach { (locationStr, count) ->
            try {
                val location = AbsenteeLocationType.valueOf(locationStr)
                absenteeData.addLocation(location, count)
            } catch (e: IllegalArgumentException) {
                logger.warn("Invalid location in input data: $locationStr")
            }
        }
        
        // Add destination country data if available
        @Suppress("UNCHECKED_CAST")
        (inputData["destinationCountryDistribution"] as? Map<String, Int>)?.forEach { (country, count) ->
            absenteeData.addDestinationCountry(country, count)
        }
        
        return absenteeData
    }
}
