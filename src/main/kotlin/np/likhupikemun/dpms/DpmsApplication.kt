package np.likhupikemun.dpms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AdminConfig::class)
class DpmsApplication

fun main(args: Array<String>) {
	runApplication<DpmsApplication>(*args)
}
