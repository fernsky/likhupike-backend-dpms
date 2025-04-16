package np.sthaniya.dpis.profile.location.service.impl

import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasCreateRequest
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasResponse
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasUpdateRequest
import np.sthaniya.dpis.profile.location.model.MajorSettlementAreas
import np.sthaniya.dpis.profile.location.repository.MajorSettlementAreasRepository
import np.sthaniya.dpis.profile.location.repository.WardRepository
import np.sthaniya.dpis.profile.location.service.MajorSettlementAreasService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MajorSettlementAreasServiceImpl(
    private val majorSettlementAreasRepository: MajorSettlementAreasRepository,
    private val wardRepository: WardRepository
) : MajorSettlementAreasService {

    @Transactional
    override fun createSettlement(request: MajorSettlementAreasCreateRequest): MajorSettlementAreasResponse {
        val ward = wardRepository.findById(request.wardId)
            .orElseThrow { NoSuchElementException("Ward with ID ${request.wardId} not found.") }
            
        val settlement = MajorSettlementAreas(
            name = request.name,
            ward = ward
        )
        
        val savedSettlement = majorSettlementAreasRepository.save(settlement)
        return mapToResponse(savedSettlement)
    }

    @Transactional
    override fun updateSettlement(id: UUID, request: MajorSettlementAreasUpdateRequest): MajorSettlementAreasResponse {
        val settlement = getSettlementEntityById(id)
        
        settlement.name = request.name
        
        return mapToResponse(majorSettlementAreasRepository.save(settlement))
    }

    @Transactional(readOnly = true)
    override fun getSettlementById(id: UUID): MajorSettlementAreasResponse {
        return mapToResponse(getSettlementEntityById(id))
    }

    @Transactional(readOnly = true)
    override fun getAllSettlements(): List<MajorSettlementAreasResponse> {
        return majorSettlementAreasRepository.findAll().map { mapToResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun getSettlementsByWardId(wardId: UUID): List<MajorSettlementAreasResponse> {
        val ward = wardRepository.findById(wardId)
            .orElseThrow { NoSuchElementException("Ward with ID $wardId not found.") }
            
        return majorSettlementAreasRepository.findByWard(ward).map { mapToResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun searchSettlementsByName(name: String): List<MajorSettlementAreasResponse> {
        return majorSettlementAreasRepository.findByNameContainingIgnoreCase(name).map { mapToResponse(it) }
    }

    @Transactional
    override fun deleteSettlement(id: UUID) {
        val settlement = getSettlementEntityById(id)
        majorSettlementAreasRepository.delete(settlement)
    }
    
    private fun getSettlementEntityById(id: UUID): MajorSettlementAreas {
        return majorSettlementAreasRepository.findById(id)
            .orElseThrow { NoSuchElementException("Settlement with ID $id not found.") }
    }
    
    private fun mapToResponse(settlement: MajorSettlementAreas): MajorSettlementAreasResponse {
        return MajorSettlementAreasResponse(
            id = settlement.id,
            name = settlement.name,
            wardId = settlement.ward.id,
            wardNumber = settlement.ward.number
        )
    }
}
