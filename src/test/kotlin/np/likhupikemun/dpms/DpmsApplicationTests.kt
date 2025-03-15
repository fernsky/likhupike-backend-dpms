package np.likhupikemun.dpms

import np.likhupikemun.dpms.auth.security.impl.JwtServiceImpl
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(
    properties = [
        "spring.main.allow-bean-definition-overriding=true",
        "management.metrics.enable.all=true",
    ]
)
@ComponentScan(
    basePackages = ["np.likhupikemun.dpms"],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JwtServiceImpl::class]
        )
    ]
)
@DirtiesContext
class DpmsApplicationTests {
    // Test methods will go here
}
