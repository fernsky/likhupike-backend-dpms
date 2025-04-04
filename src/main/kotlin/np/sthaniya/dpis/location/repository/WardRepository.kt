package np.sthaniya.dpis.location.repository

import np.sthaniya.dpis.location.domain.Ward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface WardRepository :
    JpaRepository<Ward, String>,
    JpaSpecificationExecutor<Ward>,
    CustomWardRepository
