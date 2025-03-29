package np.sthaniya.dpis.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * This configuration class ensures that only repositories
 * explicitly annotated with @RepositoryRestResource are exposed.
 * Since we don't want any repositories to be exposed, we'll not use
 * this annotation in our repository interfaces.
 */
@Configuration
class RepositoryConfig {
    // This class exists purely for configuration purposes.
    // The actual configuration is done in RestConfig.
}
