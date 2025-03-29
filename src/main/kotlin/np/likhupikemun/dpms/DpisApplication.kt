package np.likhupikemun.dpis

import np.likhupikemun.dpis.auth.config.AdminConfig
import np.likhupikemun.dpis.common.config.JacksonConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigurationProperties(AdminConfig::class)
@Import(JacksonConfig::class)
@ComponentScan(basePackages = ["np.likhupikemun.dpis"])
class dpisApplication

fun main(args: Array<String>) {
    runApplication<dpisApplication>(*args)
}



