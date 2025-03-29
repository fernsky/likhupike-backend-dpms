package np.sthaniya.dpis

import np.sthaniya.dpis.auth.security.impl.JwtServiceImpl
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
    basePackages = ["np.sthaniya.dpis"],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JwtServiceImpl::class]
        )
    ]
)
@DirtiesContext
class dpisApplicationTests {
    // Test methods will go here
}
