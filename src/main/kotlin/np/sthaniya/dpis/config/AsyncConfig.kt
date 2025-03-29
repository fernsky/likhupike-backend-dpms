package np.sthaniya.dpis.config

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

/**
 * Configuration class for asynchronous operation settings.
 *
 * This configuration enables asynchronous execution support and configures
 * a thread pool specifically for email operations with:
 * - Dedicated thread pool for email processing
 * - Configurable core and max pool sizes
 * - Queue capacity for managing load
 * - Custom thread naming pattern
 */
@Configuration
@EnableAsync
class AsyncConfig : AsyncConfigurer {
    /**
     * Creates and configures the executor for asynchronous operations.
     *
     * The executor is configured with:
     * - Core pool size: 2 threads
     * - Max pool size: 4 threads
     * - Queue capacity: 50 tasks
     * - Thread name prefix: "EmailAsync-"
     *
     * @return A configured [ThreadPoolTaskExecutor]
     */
    override fun getAsyncExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 2
            maxPoolSize = 4
            queueCapacity = 50
            setThreadNamePrefix("EmailAsync-")
            initialize()
        }
    }
}
