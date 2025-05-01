package np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.impl

import np.sthaniya.dpis.common.dto.ApiResponse
import np.sthaniya.dpis.common.service.I18nMessageService
import np.sthaniya.dpis.profile.institutions.cooperatives.api.controller.CooperativeStatisticsController
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeTypeStatistics
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeWardStatistics
import np.sthaniya.dpis.profile.institutions.cooperatives.service.CooperativeStatisticsService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

/** Implementation of the CooperativeStatisticsController interface. */
@RestController
class CooperativeStatisticsControllerImpl(
        private val cooperativeStatisticsService: CooperativeStatisticsService,
        private val i18nMessageService: I18nMessageService
) : CooperativeStatisticsController {

    private val logger = LoggerFactory.getLogger(javaClass)

    /** Get statistics of cooperatives by type. */
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    override fun getStatisticsByType(): ResponseEntity<ApiResponse<CooperativeTypeStatistics>> {
        logger.info("Getting cooperative statistics by type")

        val statistics = cooperativeStatisticsService.getStatisticsByType()

        logger.debug(
                "Successfully retrieved statistics for ${statistics.statistics.size} cooperative types"
        )

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = statistics,
                        message =
                                i18nMessageService.getMessage("cooperative.statistics.type.success")
                )
        )
    }

    /** Get statistics of cooperatives by ward. */
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_COOPERATIVE')")
    override fun getStatisticsByWard(): ResponseEntity<ApiResponse<CooperativeWardStatistics>> {
        logger.info("Getting cooperative statistics by ward")

        val statistics = cooperativeStatisticsService.getStatisticsByWard()

        logger.debug("Successfully retrieved statistics for ${statistics.statistics.size} wards")

        return ResponseEntity.ok(
                ApiResponse.success(
                        data = statistics,
                        message =
                                i18nMessageService.getMessage("cooperative.statistics.ward.success")
                )
        )
    }
}
