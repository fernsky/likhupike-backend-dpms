package np.sthaniya.dpis.location.service.impl

import np.sthaniya.dpis.location.api.dto.criteria.WardSearchCriteria
import np.sthaniya.dpis.location.api.dto.mapper.WardMapper
import np.sthaniya.dpis.location.api.dto.request.CreateWardRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateWardRequest
import np.sthaniya.dpis.location.api.dto.response.*
import np.sthaniya.dpis.location.domain.Ward
import np.sthaniya.dpis.location.exception.*
import np.sthaniya.dpis.location.repository.MunicipalityRepository
import np.sthaniya.dpis.location.repository.WardRepository
import np.sthaniya.dpis.location.repository.specification.WardSpecifications
import np.sthaniya.dpis.location.service.WardService
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.common.util.GeometryConverter
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Service
class WardServiceImpl(
    private val wardRepository: WardRepository,
    private val municipalityRepository: MunicipalityRepository,
    private val securityService: SecurityService,
    private val wardMapper: WardMapper,
    private val geometryConverter: GeometryConverter,
) : WardService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PreAuthorize("hasRole('MUNICIPALITY_ADMIN')")
    @Transactional
    override fun createWard(request: CreateWardRequest): WardResponse {
        logger.info("Creating ward ${request.wardNumber} for municipality: ${request.municipalityCode}")

        val municipality =
            municipalityRepository
                .findByCodeIgnoreCase(request.municipalityCode)
                .orElseThrow { LocationException.MunicipalityNotFoundException(request.municipalityCode) }

        validateWardNumber(request.wardNumber, request.municipalityCode)

        val ward =
            Ward().apply {
                this.wardNumber = request.wardNumber
                this.municipality = municipality
                this.area = request.area
                this.population = request.population
                this.latitude = request.latitude
                this.longitude = request.longitude
                this.officeLocation = request.officeLocation
                this.officeLocationNepali = request.officeLocationNepali
            }

        return wardRepository
            .save(ward)
            .let { wardMapper.toResponse(it) }
            .also { logger.info("Created ward ${it.wardNumber} in ${it.municipality.code}") }
    }

    @PreAuthorize("hasRole('MUNICIPALITY_ADMIN')")
    @Transactional
    override fun updateWard(
        wardNumber: Int,
        municipalityCode: String,
        request: UpdateWardRequest,
    ): WardResponse {
        val ward = getWardEntity(wardNumber, municipalityCode)
        validateWardAccess(wardNumber, municipalityCode)

        ward.apply {
            area = request.area
            population = request.population
            latitude = request.latitude
            longitude = request.longitude
            officeLocation = request.officeLocation
            officeLocationNepali = request.officeLocationNepali
            updatedAt = Instant.now()
        }

        return wardMapper
            .toResponse(wardRepository.save(ward))
            .also { logger.info("Updated ward ${it.wardNumber} in ${it.municipality.code}") }
    }

    @Transactional(readOnly = true)
    override fun getWardDetail(
        wardNumber: Int,
        municipalityCode: String,
    ): WardDetailResponse {
        val ward = getWardEntity(wardNumber, municipalityCode)
        return wardMapper.toDetailResponse(ward)
    }

    @Transactional(readOnly = true)
    override fun getWard(
        wardNumber: Int,
        municipalityCode: String,
    ): WardResponse = wardMapper.toResponse(getWardEntity(wardNumber, municipalityCode))

    @Transactional(readOnly = true)
    override fun searchWards(criteria: WardSearchCriteria): Page<DynamicWardProjection> {
        logger.debug("Searching wards with criteria: $criteria")

        criteria.validate()
        return wardRepository
            .findAll(
                WardSpecifications.withSearchCriteria(criteria),
                PageRequest.of(criteria.page, criteria.pageSize, criteria.getSort()),
            ).map { ward -> DynamicWardProjection.from(ward, criteria.fields, geometryConverter) }
    }

    @Transactional(readOnly = true)
    override fun getWardsByMunicipality(municipalityCode: String): List<WardSummaryResponse> =
        wardRepository
            .findByMunicipalityCode(municipalityCode)
            .map { wardMapper.toSummaryResponse(it) }

    @Transactional(readOnly = true)
    override fun findNearbyWards(
        latitude: BigDecimal,
        longitude: BigDecimal,
        radiusKm: Double,
        page: Int,
        size: Int,
    ): Page<WardSummaryResponse> {
        val criteria =
            WardSearchCriteria(
                latitude = latitude,
                longitude = longitude,
                radiusKm = radiusKm,
                page = page,
                pageSize = size,
            )

        criteria.validate()
        return wardRepository
            .findAll(
                WardSpecifications.withSearchCriteria(criteria),
                PageRequest.of(criteria.page, criteria.pageSize, criteria.getSort()),
            ).map { wardMapper.toSummaryResponse(it) }
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    override fun validateWardExists(
        wardNumber: Int,
        municipalityCode: String,
    ) {
        getWardEntity(wardNumber, municipalityCode)
    }

    private fun validateWardAccess(
        wardNumber: Int,
        municipalityCode: String,
    ) {
        // For MUNICIPALITY_ADMIN role, allow all access
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.authorities.any { authority ->
                authority.authority == "ROLE_MUNICIPALITY_ADMIN" ||
                    authority.authority == "ROLE_SUPER_ADMIN"
            }
        ) {
            return
        }

        // For other roles, throw exception since they shouldn't be able to update wards
        throw LocationException.InvalidWardOperationException()
    }

    private fun getWardEntity(
        wardNumber: Int,
        municipalityCode: String,
    ): Ward =
        wardRepository
            .findByWardNumberAndMunicipalityCode(wardNumber, municipalityCode)
            .orElseThrow { LocationException.WardNotFoundException(wardNumber, municipalityCode) }

    private fun validateWardNumber(
        wardNumber: Int,
        municipalityCode: String,
    ) {
        if (wardRepository.existsByWardNumberAndMunicipality(wardNumber, municipalityCode)) {
            throw LocationException.DuplicateWardNumberException(wardNumber, municipalityCode)
        }
    }
}
