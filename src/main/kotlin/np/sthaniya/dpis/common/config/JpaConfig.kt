package np.sthaniya.dpis.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@EnableSpringDataWebSupport
@EnableJpaRepositories(
    basePackages = ["np.sthaniya.dpis"],
    repositoryImplementationPostfix = "Impl",
)
class JpaConfig
