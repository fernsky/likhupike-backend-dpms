package np.sthaniya.dpis.profile.location.service.impl

import np.sthaniya.dpis.profile.location.dto.MunicipalityCreateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityGeoLocationUpdateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityResponse
import np.sthaniya.dpis.profile.location.dto.MunicipalityUpdateRequest
import np.sthaniya.dpis.profile.location.model.Municipality
import np.sthaniya.dpis.profile.location.repository.MunicipalityRepository
import np.sthaniya.dpis.profile.location.service.MunicipalityService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MunicipalityServiceImpl(
    private val municipalityRepository: MunicipalityRepository
) : MunicipalityService {

    @Transactional
    override fun getOrCreateMunicipality(request: MunicipalityCreateRequest): MunicipalityResponse {
        // Check if a municipality already exists
        val existingMunicipality = municipalityRepository.findFirstByOrderByCreatedAtAsc()
        
        // If it exists, return it; otherwise create a new one
        val municipality = existingMunicipality ?: Municipality(
            name = request.name,
            province = request.province,
            district = request.district,
            rightmostLatitude = request.rightmostLatitude,
            leftmostLatitude = request.leftmostLatitude,
            bottommostLongitude = request.bottommostLongitude,
            topmostLongitude = request.topmostLongitude,
            lowestAltitude = request.lowestAltitude,
            highestAltitude = request.highestAltitude,
            areaInSquareKilometers = request.areaInSquareKilometers
        ).apply {
            municipalityRepository.save(this)
        }
        
        return mapToResponse(municipality)
    }

    @Transactional
    override fun updateMunicipalityInfo(request: MunicipalityUpdateRequest): MunicipalityResponse {
        val municipality = getMunicipality()
        
        municipality.name = request.name
        municipality.province = request.province
        municipality.district = request.district
        
        return mapToResponse(municipalityRepository.save(municipality))
    }

    @Transactional
    override fun updateMunicipalityGeoLocation(request: MunicipalityGeoLocationUpdateRequest): MunicipalityResponse {
        val municipality = getMunicipality()
        
        municipality.rightmostLatitude = request.rightmostLatitude
        municipality.leftmostLatitude = request.leftmostLatitude
        municipality.bottommostLongitude = request.bottommostLongitude
        municipality.topmostLongitude = request.topmostLongitude
        municipality.lowestAltitude = request.lowestAltitude
        municipality.highestAltitude = request.highestAltitude
        municipality.areaInSquareKilometers = request.areaInSquareKilometers
        
        return mapToResponse(municipalityRepository.save(municipality))
    }

    @Transactional(readOnly = true)
    override fun getMunicipalityData(): MunicipalityResponse {
        return mapToResponse(getMunicipality())
    }
    
    private fun getMunicipality(): Municipality {
        return municipalityRepository.findFirstByOrderByCreatedAtAsc()
            ?: throw IllegalStateException("Municipality not found. Please create one first.")
    }
    
    private fun mapToResponse(municipality: Municipality): MunicipalityResponse {
        return MunicipalityResponse(
            id = municipality.id,
            name = municipality.name,
            province = municipality.province,
            district = municipality.district,
            rightmostLatitude = municipality.rightmostLatitude,
            leftmostLatitude = municipality.leftmostLatitude,
            bottommostLongitude = municipality.bottommostLongitude,
            topmostLongitude = municipality.topmostLongitude,
            lowestAltitude = municipality.lowestAltitude,
            highestAltitude = municipality.highestAltitude,
            areaInSquareKilometers = municipality.areaInSquareKilometers,
            wardsCount = municipality.wards.size
        )
    }
}
