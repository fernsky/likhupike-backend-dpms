package np.sthaniya.dpis.citizen.api.controller.impl

import np.sthaniya.dpis.citizen.api.controller.CitizenSearchController
import np.sthaniya.dpis.citizen.dto.projection.CitizenProjection
import np.sthaniya.dpis.citizen.dto.search.CitizenSearchCriteria
import np.sthaniya.dpis.citizen.service.CitizenSearchService
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * Implementation of the CitizenSearchController interface.
 * 
 * Handles searching citizens with advanced filtering capabilities.
 */
@RestController
class CitizenSearchControllerImpl(
    private val citizenSearchService: CitizenSearchService,
    private val i18nMessageService: I18nMessageService
) : CitizenSearchController {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Search for citizens with comprehensive filtering options.
     *
     * @param criteria The search criteria to apply
     * @return Paginated list of matching citizens with selected fields
     */
    override fun searchCitizens(
        criteria: CitizenSearchCriteria
    ): ResponseEntity<ApiResponse<List<CitizenProjection>>> {
        log.debug("Searching citizens with criteria: {}", criteria)
        val citizens = citizenSearchService.searchCitizens(criteria)
        
        return ResponseEntity.ok(
            ApiResponse.withPage(
                page = citizens,
                message = i18nMessageService.getMessage("citizen.search.success")
            )
        )
    }
}
