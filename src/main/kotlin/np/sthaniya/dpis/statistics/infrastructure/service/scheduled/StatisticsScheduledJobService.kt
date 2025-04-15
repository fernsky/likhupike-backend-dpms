package np.sthaniya.dpis.statistics.infrastructure.service.scheduled

import np.sthaniya.dpis.statistics.domain.service.demographics.AbsenteeStatisticsService
import np.sthaniya.dpis.statistics.infrastructure.service.CacheService
import np.sthaniya.dpis.statistics.domain.repository.CacheManager.CacheLevel
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Service responsible for scheduling and executing periodic statistical tasks.
 * Handles jobs like:
 * - Periodic data aggregation and roll-up
 * - Cache management and refreshing
 * - Generation of regular reports
 * - Trend analysis updates
 */
@Service
class StatisticsScheduledJobService(
    private val absenteeStatisticsService: AbsenteeStatisticsService,
    private val cacheService: CacheService
) {
    private val logger = LoggerFactory.getLogger(StatisticsScheduledJobService::class.java)

    /**
     * Daily job to cache pre-computed aggregations
     * Runs at 01:00 AM every night
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Async
    fun cachePrecomputedAggregations() {
        logger.info("Starting pre-computed aggregation caching job")
        try {
            val startTime = System.currentTimeMillis()
            
            // Generate and cache absentee trends report for the entire dataset
            val absenteeTrendsReport = absenteeStatisticsService.generateAbsenteeTrendsReport()
            cacheService.put(
                key = "absentee_trends_report:all",
                value = absenteeTrendsReport,
                ttlSeconds = 86400, // 24 hours
                level = CacheLevel.ALL
            )
            
            // Cache top absence reasons
            val topAbsenceReasons = absenteeStatisticsService.getTopAbsenceReasons(5)
            cacheService.put(
                key = "top_absence_reasons:5",
                value = topAbsenceReasons,
                ttlSeconds = 86400, // 24 hours
                level = CacheLevel.ALL
            )
            
            // Cache destination countries breakdown
            val destinationCountries = absenteeStatisticsService.getDestinationCountriesBreakdown()
            cacheService.put(
                key = "destination_countries_breakdown",
                value = destinationCountries,
                ttlSeconds = 86400, // 24 hours
                level = CacheLevel.ALL
            )
            
            val elapsedTime = System.currentTimeMillis() - startTime
            logger.info("Finished pre-computed aggregation caching job in ${elapsedTime}ms")
        } catch (e: Exception) {
            logger.error("Error in pre-computed aggregation caching job", e)
        }
    }

    /**
     * Weekly job to clean up expired cache entries
     * Runs at 02:00 AM every Monday
     */
    @Scheduled(cron = "0 0 2 * * MON")
    @Async
    fun cleanupExpiredCacheEntries() {
        logger.info("Starting cache cleanup job")
        try {
            val cacheStats = cacheService.getStatistics()
            logger.debug("Cache stats before cleanup: $cacheStats")
            
            // Get current cache key count
            val beforeCount = cacheStats["localCacheSize"] as? Int ?: 0
            
            // Clear expired entries from all cache levels
            // This actually triggers internal cleanup in cache implementations
            cacheService.cleanupExpiredEntries()
            
            // Get updated stats
            val updatedStats = cacheService.getStatistics()
            val afterCount = updatedStats["localCacheSize"] as? Int ?: 0
            val removedCount = beforeCount - afterCount
            
            logger.info("Finished cache cleanup job: removed $removedCount expired entries")
        } catch (e: Exception) {
            logger.error("Error during cache cleanup job", e)
        }
    }

    /**
     * Monthly job to generate comprehensive statistical reports
     * Runs at 03:00 AM on the 1st day of each month
     */
    @Scheduled(cron = "0 0 3 1 * ?")
    @Async
    fun generateMonthlyStatisticalReports() {
        logger.info("Starting monthly statistical report generation job")
        try {
            val now = LocalDateTime.now()
            val previousMonth = now.minusMonths(1)
            
            // Generate and store absentee trends report for the previous month
            val absenteeTrendsReport = absenteeStatisticsService.generateAbsenteeTrendsReport(
                asOfDate = previousMonth
            )
            
            // In a real implementation, you would:
            // 1. Store this report in a persistent store
            // 2. Generate notification for admins or users who requested it
            // 3. Format the report for different outputs (PDF, Excel)
            
            // For now, we'll just cache it
            cacheService.put(
                key = "monthly_absentee_report:${previousMonth.year}:${previousMonth.monthValue}",
                value = absenteeTrendsReport,
                ttlSeconds = 2592000, // 30 days
                level = CacheLevel.ALL
            )
            
            logger.info("Completed monthly statistical report generation job")
        } catch (e: Exception) {
            logger.error("Error generating monthly statistical reports", e)
        }
    }
}
