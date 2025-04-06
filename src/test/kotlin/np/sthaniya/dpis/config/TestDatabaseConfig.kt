package np.sthaniya.dpis.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator

@TestConfiguration
class TestDatabaseConfig {
    
    @Bean
    @Primary
    fun dataSource(): DataSource {
        val dataSource = EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build()
            
        // Initialize database with custom SQL functions
        val populator = ResourceDatabasePopulator()
        populator.addScript(ClassPathResource("schema.sql"))
        DatabasePopulatorUtils.execute(populator, dataSource)
        
        return dataSource
    }
}
