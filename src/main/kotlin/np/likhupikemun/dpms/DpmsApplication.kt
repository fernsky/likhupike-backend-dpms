package np.likhupikemun.dpms

import np.likhupikemun.dpms.auth.config.AdminConfig
import np.likhupikemun.dpms.common.config.JacksonConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigurationProperties(AdminConfig::class)
@Import(JacksonConfig::class)
@ComponentScan(basePackages = ["np.likhupikemun.dpms"])
class DpmsApplication

fun main(args: Array<String>) {
    runApplication<DpmsApplication>(*args)
}



