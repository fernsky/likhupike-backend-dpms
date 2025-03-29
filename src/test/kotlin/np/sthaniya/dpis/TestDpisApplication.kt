package np.sthaniya.dpis

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestdpisApplication {
    fun main(args: Array<String>) {
        fromApplication<dpisApplication>().with(TestdpisApplication::class).run(*args)
    }
}
