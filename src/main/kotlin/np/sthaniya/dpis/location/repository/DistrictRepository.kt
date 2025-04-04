package np.sthaniya.dpis.location.repository

import np.sthaniya.dpis.location.domain.District
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface DistrictRepository :
    JpaRepository<District, String>,
    JpaSpecificationExecutor<District>,
    CustomDistrictRepository
