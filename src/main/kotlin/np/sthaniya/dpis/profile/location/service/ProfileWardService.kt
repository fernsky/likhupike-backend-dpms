package np.sthaniya.dpis.profile.location.service

import java.util.UUID
import np.sthaniya.dpis.profile.location.dto.WardCreateRequest
import np.sthaniya.dpis.profile.location.dto.WardResponse
import np.sthaniya.dpis.profile.location.dto.WardUpdateRequest

interface ProfileWardService {
    fun createWard(request: WardCreateRequest): WardResponse
    fun updateWard(wardId: UUID, request: WardUpdateRequest): WardResponse
    fun getWardById(wardId: UUID): WardResponse
    fun getAllWards(): List<WardResponse>
    fun deleteWard(wardId: UUID)
    fun getWardByNumber(number: Int): WardResponse
}
