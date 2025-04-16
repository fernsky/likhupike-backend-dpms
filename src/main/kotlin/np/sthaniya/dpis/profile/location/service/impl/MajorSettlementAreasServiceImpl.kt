package np.sthaniya.dpis.profile.location.service.impl

import java.util.*
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasCreateRequest
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasResponse
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasUpdateRequest
import np.sthaniya.dpis.profile.location.exception.ProfileLocationException
import np.sthaniya.dpis.profile.location.model.MajorSettlementAreas
import np.sthaniya.dpis.profile.location.repository.MajorSettlementAreasRepository
import np.sthaniya.dpis.profile.location.repository.ProfileWardRepository
import np.sthaniya.dpis.profile.location.service.MajorSettlementAreasService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MajorSettlementAreasServiceImpl(
        private val majorSettlementAreasRepository: MajorSettlementAreasRepository,
        private val wardRepository: ProfileWardRepository
) : MajorSettlementAreasService {

    @Transactional
    override fun createSettlement(
            request: MajorSettlementAreasCreateRequest
    ): MajorSettlementAreasResponse {
        val ward =
                wardRepository.findById(request.wardId).orElseThrow {
                    ProfileLocationException.WardNotFoundException(request.wardId)
                }

        // Check if a settlement with the same name already exists in this ward
        majorSettlementAreasRepository
                .findByWard(ward)
                .find { it.name.equals(request.name, ignoreCase = true) }
                ?.let {
                    throw ProfileLocationException.DuplicateSettlementNameException(
                            wardNumber = ward.number,
                            settlementName = request.name
                    )
                }

        val settlement = MajorSettlementAreas(name = request.name, ward = ward)

        val savedSettlement = majorSettlementAreasRepository.save(settlement)
        return mapToResponse(savedSettlement)
    }

    @Transactional
    override fun updateSettlement(
            id: UUID,
            request: MajorSettlementAreasUpdateRequest
    ): MajorSettlementAreasResponse {
        val settlement = getSettlementEntityById(id)

        // Check if the new name would create a duplicate in the same ward
        if (!settlement.name.equals(request.name, ignoreCase = true)) {
            majorSettlementAreasRepository
                    .findByWard(settlement.ward)
                    .find { it.name.equals(request.name, ignoreCase = true) && it.id != id }
                    ?.let {
                        throw ProfileLocationException.DuplicateSettlementNameException(
                                wardNumber = settlement.ward.number,
                                settlementName = request.name
                        )
                    }
        }

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
        val ward =
                wardRepository.findById(wardId).orElseThrow {
                    ProfileLocationException.WardNotFoundException(wardId)
                }

        return majorSettlementAreasRepository.findByWard(ward).map { mapToResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun searchSettlementsByName(name: String): List<MajorSettlementAreasResponse> {
        return majorSettlementAreasRepository.findByNameContainingIgnoreCase(name).map {
            mapToResponse(it)
        }
    }

    @Transactional
    override fun deleteSettlement(id: UUID) {
        val settlement = getSettlementEntityById(id)
        majorSettlementAreasRepository.delete(settlement)
    }

    private fun getSettlementEntityById(id: UUID): MajorSettlementAreas {
        return majorSettlementAreasRepository.findById(id).orElseThrow {
            ProfileLocationException.SettlementNotFoundException(id)
        }
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
