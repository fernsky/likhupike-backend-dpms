package np.sthaniya.dpis.location.service.impl

import np.sthaniya.dpis.location.api.dto.criteria.MunicipalitySearchCriteria
import np.sthaniya.dpis.location.api.dto.mapper.MunicipalityMapper
import np.sthaniya.dpis.location.api.dto.request.CreateMunicipalityRequest
import np.sthaniya.dpis.location.api.dto.request.UpdateMunicipalityRequest
import np.sthaniya.dpis.location.api.dto.response.DynamicMunicipalityProjection
import np.sthaniya.dpis.location.api.dto.response.MunicipalityDetailResponse
import np.sthaniya.dpis.location.api.dto.response.MunicipalityResponse
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.MunicipalityType
import np.sthaniya.dpis.location.exception.*
import np.sthaniya.dpis.location.repository.DistrictRepository
import np.sthaniya.dpis.location.repository.MunicipalityRepository
import np.sthaniya.dpis.location.repository.specification.MunicipalitySpecifications
import np.sthaniya.dpis.location.service.MunicipalityService
import np.sthaniya.dpis.common.service.SecurityService
import np.sthaniya.dpis.common.util.GeometryConverter
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MunicipalityServiceImpl(
    private val municipalityRepository: MunicipalityRepository,
    private val districtRepository: DistrictRepository,
    private val municipalityMapper: MunicipalityMapper,
    private val securityService: SecurityService,
    private val geometryConverter: GeometryConverter,
) : MunicipalityService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun createMunicipality(request: CreateMunicipalityRequest): MunicipalityResponse {
        logger.info("Creating municipality with code: ${request.code}")

        val district =
            districtRepository
                .findByCodeIgnoreCase(request.districtCode)
                .orElseThrow { LocationException.DistrictNotFoundException(request.districtCode) }

        validateMunicipalityCode(request.code, request.districtCode)

        val municipality =
            Municipality().apply {
                name = request.name
                nameNepali = request.nameNepali
                code = request.code.uppercase()
                type = request.type
                area = request.area
                population = request.population
                latitude = request.latitude
                longitude = request.longitude
                totalWards = request.totalWards
                this.district = district
            }

        return municipalityRepository
            .save(municipality)
            .let { municipalityMapper.toResponse(it) }
            .also { logger.info("Created municipality with code: ${it.code}") }
    }

    @Transactional
    override fun updateMunicipality(
        code: String,
        request: UpdateMunicipalityRequest,
    ): MunicipalityResponse {
        logger.info("Updating municipality: $code")

        val municipality = getMunicipalityEntity(code)

        municipality.apply {
            request.name?.let { name = it }
            request.nameNepali?.let { nameNepali = it }
            request.area?.let { area = it }
            request.population?.let { population = it }
            request.latitude?.let { latitude = it }
            request.longitude?.let { longitude = it }
            request.totalWards?.let { totalWards = it }
        }

        return municipalityRepository
            .save(municipality)
            .let { municipalityMapper.toResponse(it) }
            .also { logger.info("Updated municipality with code: $code") }
    }

    @Transactional(readOnly = true)
    override fun getMunicipalityDetail(code: String): MunicipalityDetailResponse {
        logger.debug("Fetching detailed information for municipality: $code")

        val municipality = getMunicipalityEntity(code)

        return municipalityMapper.toDetailResponse(municipality)
    }

    @Transactional(readOnly = true)
    override fun searchMunicipalities(criteria: MunicipalitySearchCriteria): Page<DynamicMunicipalityProjection> {
        logger.debug("Searching municipalities with criteria: $criteria")

        criteria.validate()

        val specification = MunicipalitySpecifications.withSearchCriteria(criteria)
        val pageable =
            PageRequest.of(
                criteria.page,
                criteria.pageSize,
                Sort.by(criteria.sortDirection, criteria.sortBy.toEntityField()),
            )

        return municipalityRepository
            .findAll(specification, pageable)
            .map { municipality -> DynamicMunicipalityProjection.from(municipality, criteria.fields, geometryConverter) }
    }

    @Transactional(readOnly = true)
    override fun getMunicipality(code: String): MunicipalityResponse {
        logger.debug("Fetching municipality: $code")
        return getMunicipalityEntity(code)
            .let { municipalityMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun getMunicipalitiesByDistrict(districtCode: String): List<MunicipalityResponse> {
        logger.debug("Fetching municipalities for district: $districtCode")

        if (!districtRepository.existsByCode(districtCode)) {
            throw LocationException.DistrictNotFoundException(districtCode)
        }

        return municipalityRepository
            .findByDistrictCode(districtCode)
            .map { municipalityMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun findNearbyMunicipalities(criteria: MunicipalitySearchCriteria): Page<MunicipalityResponse> {
        logger.debug("Searching nearby municipalities with criteria: $criteria")

        criteria.validate()

        return municipalityRepository
            .findAll(
                MunicipalitySpecifications.withSearchCriteria(criteria),
                PageRequest.of(
                    criteria.page,
                    criteria.pageSize,
                    Sort.by(criteria.sortDirection, criteria.sortBy.toEntityField()),
                ),
            ).map { municipalityMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun getMunicipalitiesByType(type: MunicipalityType): List<MunicipalityResponse> {
        logger.debug("Fetching municipalities of type: $type")
        return municipalityRepository
            .findByType(type)
            .map { municipalityMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun validateMunicipalityAccess(municipalityCode: String) {
        logger.debug("Validating access for municipality: $municipalityCode")

        val currentUser = securityService.getCurrentUser()

        
    }

    @Transactional(readOnly = true)
    override fun getAllMunicipalities(): List<MunicipalityResponse> {
        logger.debug("Fetching all municipalities")
        return municipalityRepository
            .findAll()
            .map { municipalityMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun validateMunicipalityExists(code: String) {
        if (!municipalityRepository.existsByCodeIgnoreCase(code)) {
            throw LocationException.MunicipalityNotFoundException(code)
        }
    }

    // Private helper methods

    private fun getMunicipalityEntity(code: String): Municipality =
        municipalityRepository
            .findByCodeIgnoreCase(code)
            .orElseThrow { LocationException.MunicipalityNotFoundException(code) }

    private fun validateMunicipalityCode(
        code: String,
        districtCode: String,
    ) {
        if (municipalityRepository.existsByCodeAndDistrict(code.uppercase(), districtCode)) {
            throw LocationException.DuplicateMunicipalityCodeException(code, districtCode)
        }
    }

    // Implement remaining interface methods...
}
