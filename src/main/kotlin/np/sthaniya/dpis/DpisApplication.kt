package np.sthaniya.dpis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ComponentScan
import np.sthaniya.dpis.common.config.JacksonConfig

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["np.sthaniya.dpis"])
@Import(JacksonConfig::class)
@ComponentScan(basePackages = ["np.sthaniya.dpis"])
class DpisApplication

fun main(args: Array<String>) {
    runApplication<DpisApplication>(*args)
}



