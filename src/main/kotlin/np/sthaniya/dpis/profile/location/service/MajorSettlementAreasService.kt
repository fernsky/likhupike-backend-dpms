package np.sthaniya.dpis.profile.location.service

import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasCreateRequest
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasResponse
import np.sthaniya.dpis.profile.location.dto.MajorSettlementAreasUpdateRequest
import java.util.UUID

interface MajorSettlementAreasService {
    fun createSettlement(request: MajorSettlementAreasCreateRequest): MajorSettlementAreasResponse
    fun updateSettlement(id: UUID, request: MajorSettlementAreasUpdateRequest): MajorSettlementAreasResponse
    fun getSettlementById(id: UUID): MajorSettlementAreasResponse
    fun getAllSettlements(): List<MajorSettlementAreasResponse>
    fun getSettlementsByWardId(wardId: UUID): List<MajorSettlementAreasResponse>
    fun searchSettlementsByName(name: String): List<MajorSettlementAreasResponse>
    fun deleteSettlement(id: UUID)
}
