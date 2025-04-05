package np.sthaniya.dpis.location.repository

import np.sthaniya.dpis.location.domain.Ward
import np.sthaniya.dpis.location.domain.WardId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface WardRepository :
    JpaRepository<Ward, WardId>,
    JpaSpecificationExecutor<Ward>,
    CustomWardRepository
