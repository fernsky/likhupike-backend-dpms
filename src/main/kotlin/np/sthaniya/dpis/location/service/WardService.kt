package np.sthaniya.dpis.location.service

import np.sthaniya.dpis.location.api.dto.criteria.WardSearchCriteria
import np.sthaniya.dpis.location.api.dto.request.CreateWardRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateWardRequest
import np.sthaniya.dpis.location.api.dto.response.DynamicWardProjection
import np.sthaniya.dpis.location.api.dto.response.WardDetailResponse
import np.sthaniya.dpis.location.api.dto.response.WardResponse
import np.sthaniya.dpis.location.api.dto.response.WardSummaryResponse
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

interface WardService {
    /**
     * Creates a new ward
     * @throws MunicipalityNotFoundException if municipality not found
     * @throws DuplicateWardNumberException if ward number already exists in municipality
     */
    @Transactional
    fun createWard(request: CreateWardRequest): WardResponse

    /**
     * Updates an existing ward
     * @throws WardNotFoundException if ward not found
     * @throws InvalidWardOperationException if user doesn't have access to ward
     */
    @Transactional
    fun updateWard(
        wardNumber: Int,
        municipalityCode: String,
        request: UpdateWardRequest,
    ): WardResponse

    /**
     * Gets detailed ward information including statistics
     * @throws WardNotFoundException if ward not found
     */

    @Transactional(readOnly = true)
    fun getWardDetail(
        wardNumber: Int,
        municipalityCode: String,
    ): WardDetailResponse

    /**
     * Gets basic ward information
     * @throws WardNotFoundException if ward not found
     */

    @Transactional(readOnly = true)
    fun getWard(
        wardNumber: Int,
        municipalityCode: String,
    ): WardResponse

    /**
     * Searches wards based on various criteria
     * No authentication required for search
     */
    @Transactional(readOnly = true)
    fun searchWards(criteria: WardSearchCriteria): Page<DynamicWardProjection> // Updated return type

    /**
     * Lists all wards in a municipality
     * No authentication required
     */
    @Transactional(readOnly = true)
    fun getWardsByMunicipality(municipalityCode: String): List<WardSummaryResponse>

    /**
     * Finds nearby wards within specified radius
     * No authentication required
     */
    @Transactional(readOnly = true)
    fun findNearbyWards(
        latitude: BigDecimal,
        longitude: BigDecimal,
        radiusKm: Double,
        page: Int,
        size: Int,
    ): Page<WardSummaryResponse>

    /**
     * Validates if ward exists and user has access
     * @throws WardNotFoundException if ward not found
     * @throws InvalidWardOperationException if user doesn't have access
     */
    @Transactional(readOnly = true)
    fun validateWardExists(
        wardNumber: Int,
        municipalityCode: String,
    )
}
