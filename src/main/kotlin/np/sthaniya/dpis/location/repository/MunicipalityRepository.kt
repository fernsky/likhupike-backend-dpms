package np.sthaniya.dpis.location.repository

import np.sthaniya.dpis.location.domain.Municipality
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface MunicipalityRepository :
    JpaRepository<Municipality, String>,
    JpaSpecificationExecutor<Municipality>,
    CustomMunicipalityRepository
