package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeSearchController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * Implementation of the CooperativeSearchController interface.
 * Provides endpoints for searching and filtering cooperatives.
 */
@RestController
class CooperativeSearchControllerImpl(
    private val cooperativeService: CooperativeService
) : CooperativeSearchController {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun searchCooperativesByName(
        nameQuery: String,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeResponse>>> {
        logger.debug("Searching cooperatives with name query: $nameQuery")
        
        val cooperatives = cooperativeService.searchCooperativesByName(nameQuery, pageable)
        
        return ResponseEntity.ok(ApiResponse.success(cooperatives))
    }

    override fun getCooperativesByType(
        type: CooperativeType,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeResponse>>> {
        logger.debug("Fetching cooperatives of type: $type")
        
        val cooperatives = cooperativeService.getCooperativesByType(type, pageable)
        
        return ResponseEntity.ok(ApiResponse.success(cooperatives))
    }

    override fun getCooperativesByStatus(
        status: CooperativeStatus,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeResponse>>> {
        logger.debug("Fetching cooperatives with status: $status")
        
        val cooperatives = cooperativeService.getCooperativesByStatus(status, pageable)
        
        return ResponseEntity.ok(ApiResponse.success(cooperatives))
    }

    override fun getCooperativesByWard(
        ward: Int,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeResponse>>> {
        logger.debug("Fetching cooperatives in ward: $ward")
        
        val cooperatives = cooperativeService.getCooperativesByWard(ward, pageable)
        
        return ResponseEntity.ok(ApiResponse.success(cooperatives))
    }

    override fun findCooperativesNear(
        longitude: Double,
        latitude: Double,
        distanceInMeters: Double,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CooperativeResponse>>> {
        logger.debug("Finding cooperatives near coordinates ($longitude, $latitude) within $distanceInMeters meters")
        
        val cooperatives = cooperativeService.findCooperativesNear(
            longitude, 
            latitude, 
            distanceInMeters, 
            pageable
        )
        
        return ResponseEntity.ok(ApiResponse.success(cooperatives))
    }

    override fun getCooperativeStatisticsByType(): ResponseEntity<ApiResponse<Map<CooperativeType, Long>>> {
        logger.debug("Getting cooperative statistics by type")
        
        val statistics = cooperativeService.getCooperativeStatisticsByType()
        
        return ResponseEntity.ok(ApiResponse.success(statistics))
    }

    override fun getCooperativeStatisticsByWard(): ResponseEntity<ApiResponse<Map<Int, Long>>> {
        logger.debug("Getting cooperative statistics by ward")
        
        val statistics = cooperativeService.getCooperativeStatisticsByWard()
        
        return ResponseEntity.ok(ApiResponse.success(statistics))
    }
}
