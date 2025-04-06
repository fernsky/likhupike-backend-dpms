package np.sthaniya.dpis.location.service

import np.sthaniya.dpis.location.api.dto.criteria.DistrictSearchCriteria
import np.sthaniya.dpis.location.api.dto.request.CreateDistrictRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateDistrictRequest
import np.sthaniya.dpis.location.api.dto.response.DistrictDetailResponse
import np.sthaniya.dpis.location.api.dto.response.DynamicDistrictProjection
import np.sthaniya.dpis.location.api.dto.response.DistrictResponse
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

interface DistrictService {
    @Transactional
    fun createDistrict(request: CreateDistrictRequest): DistrictResponse

    @Transactional
    fun updateDistrict(
        code: String,
        request: UpdateDistrictRequest,
    ): DistrictResponse

    @Transactional(readOnly = true)
    fun getDistrictDetail(code: String): DistrictDetailResponse

    @Transactional(readOnly = true)
    fun getDistrict(code: String): DistrictResponse

    @Transactional(readOnly = true)
    fun searchDistricts(criteria: DistrictSearchCriteria): Page<DynamicDistrictProjection> // Updated return type

    @Transactional(readOnly = true)
    fun getAllDistricts(): List<DistrictResponse>

    @Transactional(readOnly = true)
    fun getDistrictsByProvince(provinceCode: String): List<DistrictResponse>

    @Transactional(readOnly = true)
    fun findLargeDistricts(
        minArea: BigDecimal,
        minPopulation: Long,
        page: Int,
        size: Int,
    ): Page<DistrictResponse>

    @Transactional(readOnly = true)
    fun validateDistrictExists(code: String)

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    fun findNearbyDistricts(
        latitude: BigDecimal,
        longitude: BigDecimal,
        radiusKm: Double,
        page: Int,
        size: Int,
    ): Page<DistrictResponse>
}
