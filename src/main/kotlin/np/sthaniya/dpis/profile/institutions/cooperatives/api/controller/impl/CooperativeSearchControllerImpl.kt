package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import java.time.LocalDate
import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeSearchController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeProjection
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.search.CooperativeSearchCriteria
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeStatus
import np.sthaniya.dpis.profile.institutions.cooperatives.model.CooperativeType
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeSearchService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

/** Implementation of the CooperativeSearchController interface. */
@RestController
class CooperativeSearchControllerImpl(
        private val cooperativeSearchService: CooperativeSearchService,
        private val i18nMessageService: I18nMessageService
) : CooperativeSearchController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    override fun searchCooperatives(
            code: String?,
            searchTerm: String?,
            type: CooperativeType?,
            types: Set<CooperativeType>?,
            status: CooperativeStatus?,
            ward: Int?,
            wards: Set<Int>?,
            establishedAfter: LocalDate?,
            establishedBefore: LocalDate?,
            locale: String?,
            nameContains: String?,
            longitude: Double?,
            latitude: Double?,
            radiusInMeters: Double?,
            hasMediaOfType: String?,
            includeTranslations: Boolean,
            includePrimaryMedia: Boolean,
            page: Int,
            size: Int,
            sortBy: String,
            sortDirection: Sort.Direction
    ): ResponseEntity<ApiResponse<List<CooperativeProjection>>> {
        logger.info("Searching cooperatives with criteria: type=$type, ward=$ward, page=$page")

        // Convert all the individual parameters to a CooperativeSearchCriteria object
        val criteria =
                CooperativeSearchCriteria(
                        code = code,
                        searchTerm = searchTerm,
                        type = type,
                        types = types,
                        status = status,
                        ward = ward,
                        wards = wards,
                        establishedAfter = establishedAfter,
                        establishedBefore = establishedBefore,
                        locale = locale,
                        nameContains = nameContains,
                        longitude = longitude,
                        latitude = latitude,
                        radiusInMeters = radiusInMeters,
                        hasMediaOfType = hasMediaOfType,
                        includeTranslations = includeTranslations,
                        includePrimaryMedia = includePrimaryMedia,
                        page = page,
                        size = size,
                        sortBy = sortBy,
                        sortDirection = sortDirection
                )

        val searchResults = cooperativeSearchService.searchCooperatives(criteria)

        logger.debug("Found ${searchResults.totalElements} cooperatives matching criteria")

        // Use withPage helper method to format response similar to UserController
        return ResponseEntity.ok(
                ApiResponse.withPage(
                        page = searchResults,
                        message =
                                i18nMessageService.getMessage(
                                        "cooperative.search.success",
                                )
                )
        )
    }
}
