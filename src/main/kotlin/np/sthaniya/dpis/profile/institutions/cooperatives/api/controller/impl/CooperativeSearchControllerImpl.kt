package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeSearchController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeProjection
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.search.CooperativeSearchCriteria
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeSearchService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

/** Implementation of the CooperativeSearchController interface. */
@RestController
class CooperativeSearchControllerImpl(
        private val cooperativeSearchService: CooperativeSearchService
) : CooperativeSearchController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    override fun searchCooperatives(
            criteria: CooperativeSearchCriteria
    ): ResponseEntity<ApiResponse<Page<CooperativeProjection>>> {
        logger.info(
                "Searching cooperatives with criteria: type=${criteria.type}, ward=${criteria.ward}, page=${criteria.page}"
        )

        val searchResults = cooperativeSearchService.searchCooperatives(criteria)

        logger.debug("Found ${searchResults.totalElements} cooperatives matching criteria")

        return ResponseEntity.ok(ApiResponse.success(searchResults))
    }
}
