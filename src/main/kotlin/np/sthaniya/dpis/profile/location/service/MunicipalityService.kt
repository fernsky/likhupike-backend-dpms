package np.sthaniya.dpis.profile.location.service

import np.sthaniya.dpis.profile.location.dto.MunicipalityCreateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityGeoLocationUpdateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityResponse
import np.sthaniya.dpis.profile.location.dto.MunicipalityUpdateRequest
import np.sthaniya.dpis.profile.location.model.Municipality

interface MunicipalityService {
    fun getOrCreateMunicipality(request: MunicipalityCreateRequest): MunicipalityResponse
    fun updateMunicipalityInfo(request: MunicipalityUpdateRequest): MunicipalityResponse
    fun updateMunicipalityGeoLocation(request: MunicipalityGeoLocationUpdateRequest): MunicipalityResponse
    fun getMunicipalityData(): MunicipalityResponse
}
