package np.sthaniya.dpis.location.repository

import np.sthaniya.dpis.location.domain.Province
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ProvinceRepository :
    JpaRepository<Province, String>,
    JpaSpecificationExecutor<Province>,
    CustomProvinceRepository
