package np.sthaniya.dpis.profile.location.service.impl

import java.math.BigDecimal
import java.util.*
import np.sthaniya.dpis.profile.location.dto.MunicipalityCreateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityGeoLocationUpdateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityResponse
import np.sthaniya.dpis.profile.location.dto.MunicipalityUpdateRequest
import np.sthaniya.dpis.profile.location.exception.ProfileLocationException
import np.sthaniya.dpis.profile.location.model.ProfileMunicipality
import np.sthaniya.dpis.profile.location.repository.ProfileMunicipalityRepository
import np.sthaniya.dpis.profile.location.service.ProfileMunicipalityService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileMunicipalityServiceImpl(
        private val municipalityRepository: ProfileMunicipalityRepository
) : ProfileMunicipalityService {

    @Transactional
    override fun getOrCreateMunicipality(request: MunicipalityCreateRequest): MunicipalityResponse {
        // Check if a municipality already exists
        val existingMunicipality = municipalityRepository.findFirstByOrderByCreatedAtAsc()

        // If it exists, return it; otherwise create a new one
        val municipality =
                existingMunicipality
                        ?: ProfileMunicipality(
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
                                )
                                .apply { municipalityRepository.save(this) }

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
    override fun updateMunicipalityGeoLocation(
            request: MunicipalityGeoLocationUpdateRequest
    ): MunicipalityResponse {
        val municipality = getMunicipality()

        // Validate geo-location data
        if (request.rightmostLatitude < request.leftmostLatitude ||
                        request.topmostLongitude < request.bottommostLongitude
        ) {
            throw ProfileLocationException.InvalidGeolocationDataException(
                    "Invalid geo-location boundaries provided."
            )
        }

        // Check altitude if both are provided
        if (request.highestAltitude != null && request.lowestAltitude != null) {
            if (request.highestAltitude < request.lowestAltitude) {
                throw ProfileLocationException.InvalidGeolocationDataException(
                        "Highest altitude must be greater than or equal to lowest altitude."
                )
            }
        }

        if (request.areaInSquareKilometers <= BigDecimal.ZERO) {
            throw ProfileLocationException.InvalidGeolocationDataException(
                    "Area must be greater than zero."
            )
        }

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

    private fun getMunicipality(): ProfileMunicipality {
        return municipalityRepository.findFirstByOrderByCreatedAtAsc()
                ?: throw ProfileLocationException.MunicipalityNotFoundException()
    }

    private fun mapToResponse(municipality: ProfileMunicipality): MunicipalityResponse {
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
