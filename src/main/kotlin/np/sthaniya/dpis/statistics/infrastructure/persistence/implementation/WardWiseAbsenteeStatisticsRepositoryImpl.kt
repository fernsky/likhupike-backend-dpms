package np.sthaniya.dpis.statistics.infrastructure.persistence.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import np.sthaniya.dpis.statistics.domain.model.demographics.WardWiseAbsenteeStatistics
import np.sthaniya.dpis.statistics.domain.repository.demographics.WardWiseAbsenteeStatisticsRepository
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenceReason
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeData
import np.sthaniya.dpis.statistics.domain.vo.demographic.AbsenteeLocationType
import np.sthaniya.dpis.statistics.domain.vo.demographic.Gender
import np.sthaniya.dpis.statistics.infrastructure.persistence.entity.AuditTrailEntity
import np.sthaniya.dpis.statistics.infrastructure.persistence.entity.WardWiseAbsenteeStatisticsEntity
import np.sthaniya.dpis.statistics.infrastructure.persistence.repository.JpaAuditTrailRepository
import np.sthaniya.dpis.statistics.infrastructure.persistence.repository.JpaStatisticsEventRepository
import np.sthaniya.dpis.statistics.infrastructure.persistence.repository.JpaWardWiseAbsenteeStatisticsRepository
import np.sthaniya.dpis.statistics.infrastructure.service.CacheService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.UUID
import jakarta.annotation.PostConstruct

/**
 * Implementation of the WardWiseAbsenteeStatisticsRepository interface.
 * 
 * Connects the domain model to the persistence layer, handling database operations,
 * event publishing, auditing, and caching.
 */
@Repository
@Transactional
class WardWiseAbsenteeStatisticsRepositoryImpl(
    private val jpaRepository: JpaWardWiseAbsenteeStatisticsRepository,
    private val eventRepository: JpaStatisticsEventRepository,
    private val auditRepository: JpaAuditTrailRepository,
    private val cacheService: CacheService,
    private val objectMapper: ObjectMapper
) : WardWiseAbsenteeStatisticsRepository {

    @PostConstruct
    fun init() {
        // Optionally pre-warm cache or initialize resources
    }
    
    /**
     * Find a statistics entity by its unique identifier
     */
    override fun findById(id: UUID): WardWiseAbsenteeStatistics? {
        // Try cache first
        val cacheKey = "absentee_stats:${id}"
        val cachedValue = cacheService.get(cacheKey, WardWiseAbsenteeStatistics::class.java)
        if (cachedValue != null) {
            return cachedValue
        }
        
        // Fetch from database if not in cache
        val entity = jpaRepository.findById(id).orElse(null) ?: return null
        val domainObject = mapToDomainObject(entity)
        
        // Cache the result
        cacheService.put(cacheKey, domainObject, 3600)
        
        return domainObject
    }
    
    /**
     * Find the latest version of statistics by its reference properties
     */
    override fun findLatestByCriteria(criteria: Map<String, Any>): WardWiseAbsenteeStatistics? {
        val wardId = criteria["wardId"] as? UUID
        val wardNumber = criteria["wardNumber"] as? Int
        
        val entities = when {
            wardId != null -> jpaRepository.findByWardIdAndIsValidTrue(wardId)
            wardNumber != null -> jpaRepository.findByWardNumber(wardNumber)
            else -> return null
        }
        
        if (entities.isEmpty()) return null
        
        // Get the most recent entity
        val latestEntity = entities.maxByOrNull { it.calculationDate }
        
        return latestEntity?.let { mapToDomainObject(it) }
    }
    
    /**
     * Find statistics as they existed at a specific point in time
     */
    override fun findByIdAsOf(id: UUID, asOfDate: LocalDateTime): WardWiseAbsenteeStatistics? {
        // Get events up to the specified date
        val events = eventRepository.findByEntityIdAndCreatedAtBeforeOrderBySequenceNumberAsc(id, asOfDate)
        if (events.isEmpty()) return null
        
        // Get current state to use as base
        val currentState = findById(id) ?: return null
        
        // Create a new instance and rebuild from historical events
        val historicalState = WardWiseAbsenteeStatistics()
        // Convert event entities to domain events and apply them
        val domainEvents = events.map { eventEntity ->
            // Deserialize event data to domain event
            // This is a simplified version - actual implementation would need proper event deserialization
            val eventData = objectMapper.readTree(eventEntity.eventData)
            // Create and return domain event from event data
            // ...
        }
        
        // In a full implementation, we would:
        // historicalState.rebuildFromEvents(domainEvents)
        
        return historicalState
    }
    
    /**
     * Find all statistics entities matching the given criteria
     */
    override fun findAllByCriteria(criteria: Map<String, Any>): List<WardWiseAbsenteeStatistics> {
        val municipalityId = criteria["municipalityId"] as? UUID
        
        val entities = when {
            municipalityId != null -> jpaRepository.findByMunicipalityId(municipalityId)
            else -> jpaRepository.findAll()
        }
        
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Find valid statistics (not marked as invalid) matching the criteria
     */
    override fun findValidByCriteria(criteria: Map<String, Any>): List<WardWiseAbsenteeStatistics> {
        // Add validity check to criteria
        val validCriteria = criteria.toMutableMap()
        validCriteria["isValid"] = true
        
        return findAllByCriteria(validCriteria)
    }
    
    /**
     * Save a statistics entity
     */
    @Transactional
    override fun save(entity: WardWiseAbsenteeStatistics): WardWiseAbsenteeStatistics {
        // Prepare entity for persistence
        val persistenceEntity = if (entity.id != null) {
            val existingEntity = jpaRepository.findById(entity.id!!).orElse(WardWiseAbsenteeStatisticsEntity())
            mapToEntity(entity, existingEntity)
        } else {
            mapToEntity(entity, WardWiseAbsenteeStatisticsEntity())
        }
        
        // Save to database
        val savedEntity = jpaRepository.save(persistenceEntity)
        
        // Invalidate cache
        if (entity.id != null) {
            cacheService.evict("absentee_stats:${entity.id}")
        }
        
        // Return updated domain object
        return mapToDomainObject(savedEntity)
    }
    
    /**
     * Save a statistics entity while explicitly recording the user who performed the action
     */
    @Transactional
    override fun saveWithAudit(entity: WardWiseAbsenteeStatistics, userId: UUID): WardWiseAbsenteeStatistics {
        // If entity already exists, retrieve its current state for audit
        val oldState = if (entity.id != null) {
            jpaRepository.findById(entity.id!!).orElse(null)?.let {
                objectMapper.writeValueAsString(mapToDomainObject(it).getCurrentState())
            }
        } else null
        
        // Save entity
        val savedEntity = save(entity)
        
        // Record audit
        val auditEntry = AuditTrailEntity().apply {
            this.entityId = savedEntity.id
            this.entityType = "WardWiseAbsenteeStatistics"
            this.action = if (oldState == null) "CREATE" else "UPDATE"
            this.userId = userId
            this.timestamp = LocalDateTime.now()
            this.oldState = oldState
            this.newState = objectMapper.writeValueAsString(savedEntity.getCurrentState())
        }
        auditRepository.save(auditEntry)
        
        return savedEntity
    }
    
    /**
     * Mark a statistics entity as invalid
     */
    @Transactional
    override fun invalidate(id: UUID, reason: String, userId: UUID): Boolean {
        val entity = jpaRepository.findById(id).orElse(null) ?: return false
        
        val oldState = objectMapper.writeValueAsString(mapToDomainObject(entity).getCurrentState())
        
        // Update entity
        entity.isValid = false
        entity.validationNotes = reason
        entity.updatedAt = LocalDateTime.now()
        entity.updatedBy = userId
        
        // Save changes
        jpaRepository.save(entity)
        
        // Record audit
        val auditEntry = AuditTrailEntity().apply {
            this.entityId = id
            this.entityType = "WardWiseAbsenteeStatistics"
            this.action = "INVALIDATE"
            this.userId = userId
            this.timestamp = LocalDateTime.now()
            this.details = reason
            this.oldState = oldState
            this.newState = objectMapper.writeValueAsString(mapToDomainObject(entity).getCurrentState())
            this.isSensitive = true
        }
        auditRepository.save(auditEntry)
        
        // Invalidate cache
        cacheService.evict("absentee_stats:$id")
        
        return true
    }
    
    /**
     * Retrieve the version history of a statistics entity
     */
    override fun getVersionHistory(id: UUID): List<Map<String, Any>> {
        val auditEntries = auditRepository.findByEntityIdOrderByTimestampDesc(id)
        
        return auditEntries.map { entry ->
            mapOf(
                "id" to (entry.id as Any),
                "timestamp" to (entry.timestamp as Any),
                "action" to (entry.action as Any),
                "userId" to (entry.userId as Any),
                "details" to (entry.details as Any)
            )
        }
    }
    
    /**
     * Get statistics entities for specific wards
     */
    override fun findByWardIds(wardIds: List<UUID>): Map<UUID, WardWiseAbsenteeStatistics> {
        val result = mutableMapOf<UUID, WardWiseAbsenteeStatistics>()
        
        for (wardId in wardIds) {
            val entities = jpaRepository.findByWardIdAndIsValidTrue(wardId)
            if (entities.isNotEmpty()) {
                val latestEntity = entities.maxByOrNull { it.calculationDate }
                latestEntity?.let { 
                    result[wardId] = mapToDomainObject(it)
                }
            }
        }
        
        return result
    }
    
    /**
     * Get count of statistics entities matching criteria
     */
    override fun countByCriteria(criteria: Map<String, Any>): Long {
        val wardId = criteria["wardId"] as? UUID
        
        return when {
            wardId != null -> jpaRepository.countByWardId(wardId)
            else -> jpaRepository.count()
        }
    }
    
    /**
     * Get statistics with pagination
     */
    override fun findWithPagination(
        criteria: Map<String, Any>, 
        page: Int, 
        size: Int, 
        sortBy: String, 
        sortDirection: String
    ): List<WardWiseAbsenteeStatistics> {
        val direction = if (sortDirection.equals("ASC", ignoreCase = true)) 
            Sort.Direction.ASC else Sort.Direction.DESC
        val pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        
        val entities = jpaRepository.findAll(pageable).content
        
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Find statistics for a specific ward by ward ID
     */
    override fun findByWardId(wardId: UUID): WardWiseAbsenteeStatistics? {
        val entities = jpaRepository.findByWardIdAndIsValidTrue(wardId)
        if (entities.isEmpty()) return null
        
        val latestEntity = entities.maxByOrNull { it.calculationDate }
        return latestEntity?.let { mapToDomainObject(it) }
    }
    
    /**
     * Find statistics for a specific ward as they existed at a point in time
     */
    override fun findByWardIdAsOf(wardId: UUID, asOfDate: LocalDateTime): WardWiseAbsenteeStatistics? {
        val pageable = PageRequest.of(0, 1)
        val entities = jpaRepository.findByWardIdAsOf(wardId, asOfDate, pageable)
        
        return if (entities.hasContent()) {
            mapToDomainObject(entities.content.first())
        } else null
    }
    
    /**
     * Find statistics by ward number
     */
    override fun findByWardNumber(wardNumber: Int): WardWiseAbsenteeStatistics? {
        val entities = jpaRepository.findByWardNumber(wardNumber)
        if (entities.isEmpty()) return null
        
        val latestEntity = entities.maxByOrNull { it.calculationDate }
        return latestEntity?.let { mapToDomainObject(it) }
    }
    
    /**
     * Find all ward statistics for a given municipality
     */
    override fun findAllByMunicipality(municipalityId: UUID): List<WardWiseAbsenteeStatistics> {
        val entities = jpaRepository.findByMunicipalityId(municipalityId)
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Find ward statistics that have been updated since a given date
     */
    override fun findUpdatedSince(since: LocalDateTime): List<WardWiseAbsenteeStatistics> {
        val entities = jpaRepository.findUpdatedSince(since)
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Find the top N wards based on the comparison value of their statistics
     */
    override fun findTopNByComparisonValue(n: Int, ascending: Boolean): List<WardWiseAbsenteeStatistics> {
        val pageable = PageRequest.of(0, n)
        val entities = if (ascending) {
            jpaRepository.findAllOrderByAbsenteePercentageAsc(pageable)
        } else {
            jpaRepository.findAllOrderByAbsenteePercentageDesc(pageable)
        }
        
        return entities.content.map { mapToDomainObject(it) }
    }
    
    /**
     * Find wards with absentee percentage above a threshold
     */
    override fun findByAbsenteePercentageAbove(thresholdPercentage: BigDecimal): List<WardWiseAbsenteeStatistics> {
        val entities = jpaRepository.findByAbsenteePercentageGreaterThanEqual(thresholdPercentage)
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Find wards with a specific primary reason for absence
     */
    override fun findByPrimaryAbsenceReason(reason: AbsenceReason): List<WardWiseAbsenteeStatistics> {
        val entities = jpaRepository.findByPrimaryReason(reason)
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Find wards with foreign absentee percentage above a threshold
     */
    override fun findByForeignAbsenteePercentageAbove(thresholdPercentage: BigDecimal): List<WardWiseAbsenteeStatistics> {
        val entities = jpaRepository.findByForeignAbsenteePercentageGreaterThanEqual(thresholdPercentage)
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Find wards with a specific top destination country
     */
    override fun findByTopDestinationCountry(country: String): List<WardWiseAbsenteeStatistics> {
        val entities = jpaRepository.findByTopDestinationCountry(country)
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Find wards with absentee gender imbalance above a threshold
     */
    override fun findByAbsenteeGenderImbalance(ratio: BigDecimal): List<WardWiseAbsenteeStatistics> {
        val entities = jpaRepository.findByAbsenteeSexRatioGreaterThanEqual(ratio)
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Get aggregated absentee statistics by absence reason across all wards
     */
    override fun getAggregatedStatsByReason(): Map<AbsenceReason, Int> {
        // For efficiency, this would be better implemented with a native query
        // For now, we'll do it in memory
        val statistics = jpaRepository.findAll()
        
        val result = mutableMapOf<AbsenceReason, Int>()
        
        for (entity in statistics) {
            if (!entity.isValid) continue
            
            val reasonDistribution = objectMapper.readValue(entity.reasonDistribution, Map::class.java) as Map<String, Int>
            
            reasonDistribution.forEach { (reasonStr, count) ->
                try {
                    val reason = AbsenceReason.valueOf(reasonStr)
                    result[reason] = (result[reason] ?: 0) + count
                } catch (e: IllegalArgumentException) {
                    // Ignore invalid enum values
                }
            }
        }
        
        return result
    }
    
    /**
     * Get aggregated absentee statistics by location type across all wards
     */
    override fun getAggregatedStatsByLocation(): Map<AbsenteeLocationType, Int> {
        // Similar implementation to getAggregatedStatsByReason
        val statistics = jpaRepository.findAll()
        
        val result = mutableMapOf<AbsenteeLocationType, Int>()
        
        for (entity in statistics) {
            if (!entity.isValid) continue
            
            val locationDistribution = objectMapper.readValue(entity.locationDistribution, Map::class.java) as Map<String, Int>
            
            locationDistribution.forEach { (locationStr, count) ->
                try {
                    val location = AbsenteeLocationType.valueOf(locationStr)
                    result[location] = (result[location] ?: 0) + count
                } catch (e: IllegalArgumentException) {
                    // Ignore invalid enum values
                }
            }
        }
        
        return result
    }
    
    /**
     * Get aggregated absentee statistics by destination country across all wards
     */
    override fun getAggregatedStatsByDestinationCountry(): Map<String, Int> {
        // Similar implementation to getAggregatedStatsByReason
        val statistics = jpaRepository.findAll()
        
        val result = mutableMapOf<String, Int>()
        
        for (entity in statistics) {
            if (!entity.isValid) continue
            
            val countryDistribution = objectMapper.readValue(entity.destinationCountryDistribution, Map::class.java) as Map<String, Int>
            
            countryDistribution.forEach { (country, count) ->
                result[country] = (result[country] ?: 0) + count
            }
        }
        
        return result
    }
    
    /**
     * Get absentee trend data over time for a specific ward
     */
    override fun getAbsenteeTrendData(
        wardId: UUID,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Map<String, Any>> {
        // For a complete implementation, this would use time-series queries
        // For now, we'll retrieve all statistics for the ward and filter in memory
        
        val allStats = jpaRepository.findByWardId(wardId)
        
        return allStats
            .filter { it.calculationDate.isAfter(startDate) && it.calculationDate.isBefore(endDate) }
            .sortedBy { it.calculationDate }
            .map {
                mapOf(
                    "date" to (it.calculationDate as Any),
                    "totalAbsenteePopulation" to (it.totalAbsenteePopulation as Any),
                    "absenteePercentage" to (it.absenteePercentage as Any),
                    "foreignAbsenteePercentage" to (it.foreignAbsenteePercentage as Any),
                    "maleAbsenteeCount" to (it.maleAbsenteeCount as Any),
                    "femaleAbsenteeCount" to (it.femaleAbsenteeCount as Any)
                )
            }
    }
    
    /**
     * Get comparison of absentee data by gender
     */
    override fun getAbsenteeGenderComparison(): Map<Gender, Map<String, Any>> {
        // This would be better implemented with aggregate queries
        val statistics = jpaRepository.findAll().filter { it.isValid }
        
        val maleTotal = statistics.sumOf { it.maleAbsenteeCount }
        val femaleTotal = statistics.sumOf { it.femaleAbsenteeCount }
        val otherTotal = statistics.sumOf { it.otherAbsenteeCount }
        val grandTotal = maleTotal + femaleTotal + otherTotal
        
        val result = mutableMapOf<Gender, Map<String, Any>>()
        
        if (grandTotal > 0) {
            result[Gender.MALE] = mapOf(
                "count" to maleTotal,
                "percentage" to BigDecimal((maleTotal.toDouble() / grandTotal) * 100).setScale(2, java.math.RoundingMode.HALF_UP)
            )
            
            result[Gender.FEMALE] = mapOf(
                "count" to femaleTotal,
                "percentage" to BigDecimal((femaleTotal.toDouble() / grandTotal) * 100).setScale(2, java.math.RoundingMode.HALF_UP)
            )
            
            result[Gender.OTHER] = mapOf(
                "count" to otherTotal,
                "percentage" to BigDecimal((otherTotal.toDouble() / grandTotal) * 100).setScale(2, java.math.RoundingMode.HALF_UP)
            )
        }
        
        return result
    }
    
    /**
     * Find wards where absentee population has increased beyond a threshold
     * compared to a previous reference period
     */
    override fun findWardsWithSignificantAbsenteeIncrease(
        referenceDate: LocalDateTime,
        thresholdPercentage: BigDecimal
    ): List<WardWiseAbsenteeStatistics> {
        // This is a complex query that requires comparison of statistics over time
        // For now, we'll implement a simplified version in memory
        
        val currentStatsByWard = jpaRepository.findAll().filter { it.isValid }
            .groupBy { it.wardId }
            .mapValues { (_, stats) -> stats.maxByOrNull { it.calculationDate } }
            .filterValues { it != null }
            .mapValues { it.value!! }
        
        val result = mutableListOf<WardWiseAbsenteeStatistics>()
        
        for ((wardId, currentStats) in currentStatsByWard) {
            // Find historical statistics
            val historicalStats = jpaRepository.findByWardIdAsOf(wardId!!, referenceDate, PageRequest.of(0, 1))
            
            if (historicalStats.hasContent()) {
                val oldStats = historicalStats.content.first()
                
                // Skip if old value was zero to avoid division by zero
                if (oldStats.totalAbsenteePopulation > 0) {
                    val increasePercentage = BigDecimal(
                        ((currentStats.totalAbsenteePopulation - oldStats.totalAbsenteePopulation).toDouble() / 
                        oldStats.totalAbsenteePopulation) * 100
                    ).setScale(2, java.math.RoundingMode.HALF_UP)
                    
                    if (increasePercentage >= thresholdPercentage) {
                        result.add(mapToDomainObject(currentStats))
                    }
                }
            }
        }
        
        return result
    }
    
    /**
     * Get cross-tabulation of absentee statistics by multiple dimensions
     */
    override fun getCrossTabulation(dimensions: List<String>): Map<String, Any> {
        // This is a complex analytics query that would normally be done with OLAP tools
        // For now, we'll implement a simplified version for demonstration
        
        if (dimensions.isEmpty()) return emptyMap()
        
        val statistics = jpaRepository.findAll().filter { it.isValid }
        
        // Basic implementation for 2 dimensions
        if (dimensions.size == 2 && dimensions.contains("gender") && dimensions.contains("reason")) {
            val result = mutableMapOf<String, MutableMap<String, Int>>()
            
            for (entity in statistics) {
                val genderDistribution = objectMapper.readValue(entity.ageGenderDistribution, Map::class.java) as Map<String, Map<String, Int>>
                val reasonDistribution = objectMapper.readValue(entity.reasonDistribution, Map::class.java) as Map<String, Int>
                
                // For a real implementation, this would need to parse the JSON correctly and perform the cross-tabulation
                // This is just a placeholder
            }
            
            return mapOf("crossTab" to result)
        }
        
        // Return empty for unsupported dimension combinations
        return emptyMap()
    }
    
    /**
     * Get statistics for wards with population greater than the specified threshold
     *
     * @param populationThreshold The minimum population
     * @return List of ward statistics for wards exceeding the population threshold
     */
    override fun findByPopulationGreaterThan(populationThreshold: Int): List<WardWiseAbsenteeStatistics> {
        // Retrieve all entities with valid applicablePopulation > threshold
        val entities = jpaRepository.findAll().filter { 
            it.isValid && 
            it.applicablePopulation != null && 
            it.applicablePopulation!! > populationThreshold 
        }
        
        // Convert entities to domain objects
        return entities.map { mapToDomainObject(it) }
    }
    
    /**
     * Calculate aggregated statistics across multiple wards
     *
     * @param wardIds The IDs of the wards to include in the aggregation
     * @return Aggregated statistics result
     */
    override fun calculateAggregateForWards(wardIds: List<UUID>): Map<String, Any> {
        // Get all valid statistics for the requested wards
        val wardStats = wardIds.mapNotNull { wardId ->
            val stats = jpaRepository.findByWardIdAndIsValidTrue(wardId)
            stats.maxByOrNull { it.calculationDate }
        }.map { mapToDomainObject(it) }
        
        if (wardStats.isEmpty()) {
            return mapOf(
                "wardsIncluded" to 0,
                "totalAbsenteePopulation" to 0,
                "averageAbsenteePercentage" to BigDecimal.ZERO,
                "aggregationDate" to LocalDateTime.now()
            )
        }
        
        // Calculate aggregated totals
        val totalAbsenteePopulation = wardStats.sumOf { it.absenteeData.totalAbsenteePopulation }
        
        // Calculate average percentages
        val avgAbsenteePercentage = wardStats
            .map { it.absenteePercentage }
            .reduce { acc, value -> acc.add(value) }
            .divide(BigDecimal(wardStats.size), 2, RoundingMode.HALF_UP)
            
        val avgForeignAbsenteePercentage = wardStats
            .map { it.foreignAbsenteePercentage }
            .reduce { acc, value -> acc.add(value) }
            .divide(BigDecimal(wardStats.size), 2, RoundingMode.HALF_UP)
            
        // Aggregate gender distribution
        val maleTotal = wardStats.sumOf { 
            it.absenteeData.getGenderDistribution()[Gender.MALE] ?: 0 
        }
        val femaleTotal = wardStats.sumOf { 
            it.absenteeData.getGenderDistribution()[Gender.FEMALE] ?: 0 
        }
        val otherTotal = wardStats.sumOf { 
            it.absenteeData.getGenderDistribution()[Gender.OTHER] ?: 0 
        }
        
        // Calculate gender percentages
        val genderTotal = maleTotal + femaleTotal + otherTotal
        val genderPercentages = if (genderTotal > 0) {
            mapOf(
                "MALE" to BigDecimal((maleTotal.toDouble() / genderTotal) * 100)
                    .setScale(2, RoundingMode.HALF_UP),
                "FEMALE" to BigDecimal((femaleTotal.toDouble() / genderTotal) * 100)
                    .setScale(2, RoundingMode.HALF_UP),
                "OTHER" to BigDecimal((otherTotal.toDouble() / genderTotal) * 100)
                    .setScale(2, RoundingMode.HALF_UP)
            )
        } else {
            mapOf("MALE" to BigDecimal.ZERO, "FEMALE" to BigDecimal.ZERO, "OTHER" to BigDecimal.ZERO)
        }
        
        // Aggregate reason distribution
        val reasonDistribution = mutableMapOf<AbsenceReason, Int>()
        wardStats.forEach { stats ->
            stats.absenteeData.reasonDistribution.forEach { (reason, count) ->
                reasonDistribution[reason] = (reasonDistribution[reason] ?: 0) + count
            }
        }
        
        // Calculate primary reason
        val primaryReason = reasonDistribution.maxByOrNull { it.value }?.key
        
        // Aggregate location distribution
        val locationDistribution = mutableMapOf<AbsenteeLocationType, Int>()
        wardStats.forEach { stats ->
            stats.absenteeData.locationDistribution.forEach { (location, count) ->
                locationDistribution[location] = (locationDistribution[location] ?: 0) + count
            }
        }
        
        // Aggregate destination countries
        val destinationCountries = mutableMapOf<String, Int>()
        wardStats.forEach { stats ->
            stats.absenteeData.destinationCountryDistribution.forEach { (country, count) ->
                destinationCountries[country] = (destinationCountries[country] ?: 0) + count
            }
        }
        
        // Get top destination countries
        val topDestinations = destinationCountries.entries
            .sortedByDescending { it.value }
            .take(5)
            .map { it.key to it.value }
        
        // Calculate aggregated literacy rate
        val literacyRates = wardStats.mapNotNull { stats ->
            val rate = stats.absenteeData.getLiteracyRate()
            if (rate > BigDecimal.ZERO) rate else null
        }
        
        val avgLiteracyRate = if (literacyRates.isNotEmpty()) {
            literacyRates.reduce { acc, rate -> acc.add(rate) }
                .divide(BigDecimal(literacyRates.size), 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO
        
        // Return aggregated results
        return mapOf(
            "wardsIncluded" to wardStats.size,
            "wardIds" to wardIds,
            "totalAbsenteePopulation" to totalAbsenteePopulation,
            "maleAbsenteeCount" to maleTotal,
            "femaleAbsenteeCount" to femaleTotal,
            "otherAbsenteeCount" to otherTotal,
            "genderPercentages" to genderPercentages,
            "averageAbsenteePercentage" to avgAbsenteePercentage,
            "averageForeignAbsenteePercentage" to avgForeignAbsenteePercentage,
            "primaryAbsenceReason" to (primaryReason?.name ?: "UNKNOWN"),
            "topDestinationCountries" to topDestinations,
            "averageLiteracyRate" to avgLiteracyRate,
            "aggregationDate" to LocalDateTime.now()
        )
    }
    
    /**
     * Compare statistics between two wards
     *
     * @param wardId1 First ward ID
     * @param wardId2 Second ward ID
     * @return Comparison result with key metrics
     */
    override fun compareWards(wardId1: UUID, wardId2: UUID): Map<String, Any> {
        // Get most recent valid statistics for each ward
        val stats1 = findByWardId(wardId1)
        val stats2 = findByWardId(wardId2)
        
        // If either ward stats are missing, return partial or empty comparison
        if (stats1 == null && stats2 == null) {
            return mapOf(
                "error" to "No valid statistics found for either ward",
                "comparisonDate" to LocalDateTime.now()
            )
        }
        
        if (stats1 == null) {
            return mapOf(
                "error" to "No valid statistics found for ward ID: $wardId1",
                "ward2" to stats2!!.getKeyMetrics(),
                "comparisonDate" to LocalDateTime.now()
            )
        }
        
        if (stats2 == null) {
            return mapOf(
                "error" to "No valid statistics found for ward ID: $wardId2",
                "ward1" to stats1.getKeyMetrics(),
                "comparisonDate" to LocalDateTime.now()
            )
        }
        
        // Calculate key metric differences
        val absenteePopDiff = stats1.absenteeData.totalAbsenteePopulation - stats2.absenteeData.totalAbsenteePopulation
        val absenteePercentDiff = stats1.absenteePercentage.subtract(stats2.absenteePercentage)
        val foreignAbsenteePercentDiff = stats1.foreignAbsenteePercentage.subtract(stats2.foreignAbsenteePercentage)
        
        // Compare gender distributions
        val gender1 = stats1.absenteeData.getGenderDistribution()
        val gender2 = stats2.absenteeData.getGenderDistribution()
        
        val maleDiff = (gender1[Gender.MALE] ?: 0) - (gender2[Gender.MALE] ?: 0)
        val femaleDiff = (gender1[Gender.FEMALE] ?: 0) - (gender2[Gender.FEMALE] ?: 0)
        val otherDiff = (gender1[Gender.OTHER] ?: 0) - (gender2[Gender.OTHER] ?: 0)
        
        // Compare reason distributions
        val reasonComparison = mutableMapOf<String, Map<String, Any>>()
        val reasonTypes = AbsenceReason.values()
        
        for (reason in reasonTypes) {
            val count1 = stats1.absenteeData.reasonDistribution[reason] ?: 0
            val count2 = stats2.absenteeData.reasonDistribution[reason] ?: 0
            val diff = count1 - count2
            
            reasonComparison[reason.name] = mapOf(
                "ward1" to count1,
                "ward2" to count2,
                "difference" to diff
            )
        }
        
        // Compare education levels
        val literacy1 = stats1.absenteeData.getLiteracyRate()
        val literacy2 = stats2.absenteeData.getLiteracyRate()
        val literacyDiff = literacy1.subtract(literacy2)
        
        val higherEd1 = stats1.absenteeData.getHigherEducationRate()
        val higherEd2 = stats2.absenteeData.getHigherEducationRate()
        val higherEdDiff = higherEd1.subtract(higherEd2)
        
        // Return comparison results
        return mapOf(
            "ward1" to mapOf(
                "wardId" to stats1.wardId,
                "wardNumber" to stats1.wardNumber,
                "absenteePopulation" to stats1.absenteeData.totalAbsenteePopulation,
                "absenteePercentage" to stats1.absenteePercentage,
                "foreignAbsenteePercentage" to stats1.foreignAbsenteePercentage,
                "primaryReason" to (stats1.primaryAbsenceReason?.name ?: "UNKNOWN"),
                "topDestination" to (stats1.topDestinationCountry ?: "UNKNOWN"),
                "maleCount" to (gender1[Gender.MALE] ?: 0),
                "femaleCount" to (gender1[Gender.FEMALE] ?: 0),
                "literacyRate" to literacy1,
                "higherEducationRate" to higherEd1
            ),
            "ward2" to mapOf(
                "wardId" to stats2.wardId,
                "wardNumber" to stats2.wardNumber,
                "absenteePopulation" to stats2.absenteeData.totalAbsenteePopulation,
                "absenteePercentage" to stats2.absenteePercentage,
                "foreignAbsenteePercentage" to stats2.foreignAbsenteePercentage,
                "primaryReason" to (stats2.primaryAbsenceReason?.name ?: "UNKNOWN"),
                "topDestination" to (stats2.topDestinationCountry ?: "UNKNOWN"),
                "maleCount" to (gender2[Gender.MALE] ?: 0),
                "femaleCount" to (gender2[Gender.FEMALE] ?: 0),
                "literacyRate" to literacy2,
                "higherEducationRate" to higherEd2
            ),
            "differences" to mapOf(
                "absenteePopulation" to absenteePopDiff,
                "absenteePercentage" to absenteePercentDiff,
                "foreignAbsenteePercentage" to foreignAbsenteePercentDiff,
                "maleCount" to maleDiff,
                "femaleCount" to femaleDiff,
                "otherCount" to otherDiff,
                "literacyRate" to literacyDiff,
                "higherEducationRate" to higherEdDiff
            ),
            "reasonComparison" to reasonComparison,
            "comparisonDate" to LocalDateTime.now()
        )
    }
    
    /**
     * Map a JPA entity to a domain object
     */
    private fun mapToDomainObject(entity: WardWiseAbsenteeStatisticsEntity): WardWiseAbsenteeStatistics {
        return WardWiseAbsenteeStatistics().apply {
            // Map base entity fields
            this.statisticalGroup = entity.statisticalGroup
            this.statisticalCategory = entity.statisticalCategory
            this.referenceDate = entity.referenceDate
            this.calculationDate = entity.calculationDate
            this.calculatedBy = entity.calculatedBy
            this.methodologyVersion = entity.methodologyVersion
            this.isEstimate = entity.isEstimate
            this.confidenceScore = entity.confidenceScore
            this.dataSource = entity.dataSource
            this.isValid = entity.isValid
            this.validationNotes = entity.validationNotes
            
            // Map caching fields
            this.lastCachedAt = entity.lastCachedAt
            this.cacheExpirationPolicy = entity.cacheExpirationPolicy
            this.cacheTTLSeconds = entity.cacheTTLSeconds
            
            // Map ward statistics fields
            this.wardId = entity.wardId
            this.wardNumber = entity.wardNumber
            this.isCompleteWard = entity.isCompleteWard
            this.subsetDescription = entity.subsetDescription
            this.applicablePopulation = entity.applicablePopulation
            this.applicableHouseholds = entity.applicableHouseholds
            this.isComparable = entity.isComparable
            
            // Map absentee statistics fields
            this.absenteePercentage = entity.absenteePercentage
            this.foreignAbsenteePercentage = entity.foreignAbsenteePercentage
            this.primaryAbsenceReason = entity.primaryAbsenceReason
            this.topDestinationCountry = entity.topDestinationCountry
            this.averageAbsenteeAge = entity.averageAbsenteeAge
            this.absenteeSexRatio = entity.absenteeSexRatio
            
            // Map complex fields (JSON)
            val absentee = AbsenteeData(entity.totalAbsenteePopulation)
            
            // Parse JSON fields to populate absentee data
            // In a real implementation, you would deserialize these fields correctly
            // For now, we're just creating a placeholder implementation
            
            this.absenteeData = absentee
        }
    }
    
    /**
     * Map a domain object to a JPA entity
     */
    private fun mapToEntity(domainObject: WardWiseAbsenteeStatistics, entity: WardWiseAbsenteeStatisticsEntity): WardWiseAbsenteeStatisticsEntity {
        return entity.apply {
            // Map ID only if it exists
            domainObject.id?.let { this.id = it }
            
            // Map base entity fields
            this.statisticalGroup = domainObject.statisticalGroup
            this.statisticalCategory = domainObject.statisticalCategory
            this.referenceDate = domainObject.referenceDate
            this.calculationDate = domainObject.calculationDate
            this.calculatedBy = domainObject.calculatedBy
            this.methodologyVersion = domainObject.methodologyVersion
            this.isEstimate = domainObject.isEstimate
            this.confidenceScore = domainObject.confidenceScore
            this.dataSource = domainObject.dataSource
            this.isValid = domainObject.isValid
            this.validationNotes = domainObject.validationNotes
            
            // Map caching fields
            this.lastCachedAt = domainObject.lastCachedAt
            this.cacheExpirationPolicy = domainObject.cacheExpirationPolicy
            this.cacheTTLSeconds = domainObject.cacheTTLSeconds
            
            // Map ward statistics fields
            this.wardId = domainObject.wardId
            this.wardNumber = domainObject.wardNumber
            this.isCompleteWard = domainObject.isCompleteWard
            this.subsetDescription = domainObject.subsetDescription
            this.applicablePopulation = domainObject.applicablePopulation
            this.applicableHouseholds = domainObject.applicableHouseholds
            this.isComparable = domainObject.isComparable
            
            // Map absentee statistics fields
            this.totalAbsenteePopulation = domainObject.absenteeData.totalAbsenteePopulation
            this.absenteePercentage = domainObject.absenteePercentage
            this.foreignAbsenteePercentage = domainObject.foreignAbsenteePercentage
            this.primaryAbsenceReason = domainObject.primaryAbsenceReason
            this.topDestinationCountry = domainObject.topDestinationCountry
            this.averageAbsenteeAge = domainObject.averageAbsenteeAge
            this.absenteeSexRatio = domainObject.absenteeSexRatio
            
            // Map gender distribution
            val genderDistribution = domainObject.absenteeData.getGenderDistribution()
            this.maleAbsenteeCount = genderDistribution[Gender.MALE] ?: 0
            this.femaleAbsenteeCount = genderDistribution[Gender.FEMALE] ?: 0
            this.otherAbsenteeCount = genderDistribution[Gender.OTHER] ?: 0
            
            // Map complex fields to JSON
            // In a real implementation, you would serialize these fields correctly
            this.ageGenderDistribution = objectMapper.writeValueAsString(domainObject.absenteeData.ageGenderDistribution)
            this.reasonDistribution = objectMapper.writeValueAsString(domainObject.absenteeData.reasonDistribution)
            this.locationDistribution = objectMapper.writeValueAsString(domainObject.absenteeData.locationDistribution)
            this.educationLevelDistribution = objectMapper.writeValueAsString(domainObject.absenteeData.educationLevelDistribution)
            this.destinationCountryDistribution = objectMapper.writeValueAsString(domainObject.absenteeData.destinationCountryDistribution)
            
            // Update audit fields
            if (this.id == domainObject.id) {
                // Existing entity, update modification data
                this.updatedAt = LocalDateTime.now()
                // updatedBy would be set from saveWithAudit
            } else {
                // New entity, set creation data
                this.createdAt = LocalDateTime.now()
                // createdBy would be set from saveWithAudit
            }
        }
    }
}
