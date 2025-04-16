package np.sthaniya.dpis.profile.location.service.impl

import np.sthaniya.dpis.profile.location.dto.WardCreateRequest
import np.sthaniya.dpis.profile.location.dto.WardResponse
import np.sthaniya.dpis.profile.location.dto.WardUpdateRequest
import np.sthaniya.dpis.profile.location.model.Ward
import np.sthaniya.dpis.profile.location.repository.MunicipalityRepository
import np.sthaniya.dpis.profile.location.repository.WardRepository
import np.sthaniya.dpis.profile.location.service.WardService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class WardServiceImpl(
    private val wardRepository: WardRepository,
    private val municipalityRepository: MunicipalityRepository
) : WardService {

    @Transactional
    override fun createWard(request: WardCreateRequest): WardResponse {
        val municipality = municipalityRepository.findFirstByOrderByCreatedAtAsc()
            ?: throw IllegalStateException("Municipality not found. Please create a municipality first.")
            
        // Check if a ward with the same number already exists
        wardRepository.findByNumberAndMunicipality(request.number, municipality)?.let {
            throw IllegalStateException("Ward with number ${request.number} already exists.")
        }
        
        val ward = Ward(
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
        val municipality = municipalityRepository.findFirstByOrderByCreatedAtAsc()
            ?: return emptyList()
            
        return wardRepository.findByMunicipality(municipality).map { mapToResponse(it) }
    }

    @Transactional
    override fun deleteWard(wardId: UUID) {
        val ward = getWardEntityById(wardId)
        wardRepository.delete(ward)
    }

    @Transactional(readOnly = true)
    override fun getWardByNumber(number: Int): WardResponse {
        val municipality = municipalityRepository.findFirstByOrderByCreatedAtAsc()
            ?: throw IllegalStateException("Municipality not found.")
            
        val ward = wardRepository.findByNumberAndMunicipality(number, municipality)
            ?: throw NoSuchElementException("Ward with number $number not found.")
            
        return mapToResponse(ward)
    }
    
    private fun getWardEntityById(wardId: UUID): Ward {
        return wardRepository.findById(wardId)
            .orElseThrow { NoSuchElementException("Ward with ID $wardId not found.") }
    }
    
    private fun mapToResponse(ward: Ward): WardResponse {
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
