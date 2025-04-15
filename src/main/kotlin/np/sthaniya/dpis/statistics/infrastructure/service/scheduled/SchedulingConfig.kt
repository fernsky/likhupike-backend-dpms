package np.sthaniya.dpis.statistics.infrastructure.service.scheduled

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.context.annotation.Bean
import org.slf4j.LoggerFactory

/**
 * Configuration for task scheduling in the statistics module.
 * Sets up the task scheduler and enables asynchronous execution.
 */
@Configuration
@EnableScheduling
@EnableAsync
class SchedulingConfig {
    private val logger = LoggerFactory.getLogger(SchedulingConfig::class.java)
    
    /**
     * Configure the thread pool task scheduler for scheduled jobs
     */
    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        logger.info("Configuring statistics task scheduler")
        return ThreadPoolTaskScheduler().apply {
            poolSize = 5
            threadNamePrefix = "stats-scheduler-"
            setWaitForTasksToCompleteOnShutdown(true)
            setAwaitTerminationSeconds(60)
            errorHandler = { ex ->
                logger.error("Unhandled exception in scheduled task", ex)
            }
        }
    }
}
