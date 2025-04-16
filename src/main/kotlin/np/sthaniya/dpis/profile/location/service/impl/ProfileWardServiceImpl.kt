package np.sthaniya.dpis.profile.location.service.impl

import java.util.*
import np.sthaniya.dpis.profile.location.dto.WardCreateRequest
import np.sthaniya.dpis.profile.location.dto.WardResponse
import np.sthaniya.dpis.profile.location.dto.WardUpdateRequest
import np.sthaniya.dpis.profile.location.exception.ProfileLocationException
import np.sthaniya.dpis.profile.location.model.ProfileWard
import np.sthaniya.dpis.profile.location.repository.ProfileMunicipalityRepository
import np.sthaniya.dpis.profile.location.repository.ProfileWardRepository
import np.sthaniya.dpis.profile.location.service.ProfileWardService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileWardServiceImpl(
        private val wardRepository: ProfileWardRepository,
        private val municipalityRepository: ProfileMunicipalityRepository
) : ProfileWardService {

    @Transactional
    override fun createWard(request: WardCreateRequest): WardResponse {
        val municipality =
                municipalityRepository.findFirstByOrderByCreatedAtAsc()
                        ?: throw ProfileLocationException.MunicipalityRequiredException()

        // Check if a ward with the same number already exists
        wardRepository.findByNumberAndMunicipality(request.number, municipality)?.let {
            throw ProfileLocationException.DuplicateWardNumberException(request.number)
        }

        val ward =
                ProfileWard(
                        number = request.number,
                        area = request.area,
                        formingLocalBodies = request.formingLocalBodies,
                        formingConstituentWards = request.formingConstituentWards,
                        municipality = municipality
                )

        val savedWard = wardRepository.save(ward)
        return mapToResponse(savedWard)
    }

    @Transactional
    override fun updateWard(wardId: UUID, request: WardUpdateRequest): WardResponse {
        val ward = getWardEntityById(wardId)

        ward.area = request.area
        ward.formingLocalBodies = request.formingLocalBodies
        ward.formingConstituentWards = request.formingConstituentWards

        return mapToResponse(wardRepository.save(ward))
    }

    @Transactional(readOnly = true)
    override fun getWardById(wardId: UUID): WardResponse {
        return mapToResponse(getWardEntityById(wardId))
    }

    @Transactional(readOnly = true)
    override fun getAllWards(): List<WardResponse> {
        val municipality =
                municipalityRepository.findFirstByOrderByCreatedAtAsc() ?: return emptyList()

        return wardRepository.findByMunicipality(municipality).map { mapToResponse(it) }
    }

    @Transactional
    override fun deleteWard(wardId: UUID) {
        val ward = getWardEntityById(wardId)
        wardRepository.delete(ward)
    }

    @Transactional(readOnly = true)
    override fun getWardByNumber(number: Int): WardResponse {
        val municipality =
                municipalityRepository.findFirstByOrderByCreatedAtAsc()
                        ?: throw ProfileLocationException.MunicipalityNotFoundException()

        val ward =
                wardRepository.findByNumberAndMunicipality(number, municipality)
                        ?: throw ProfileLocationException.WardNotFoundByNumberException(number)

        return mapToResponse(ward)
    }

    private fun getWardEntityById(wardId: UUID): ProfileWard {
        return wardRepository.findById(wardId).orElseThrow {
            ProfileLocationException.WardNotFoundException(wardId)
        }
    }

    private fun mapToResponse(ward: ProfileWard): WardResponse {
        return WardResponse(
                id = ward.id,
                number = ward.number,
                area = ward.area,
                formingLocalBodies = ward.formingLocalBodies,
                formingConstituentWards = ward.formingConstituentWards,
                settlementCount = ward.majorSettlementAreas.size
        )
    }
}
