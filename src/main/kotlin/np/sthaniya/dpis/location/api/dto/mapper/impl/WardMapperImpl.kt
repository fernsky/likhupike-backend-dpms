package np.sthaniya.dpis.location.api.dto.mapper.impl

import np.sthaniya.dpis.location.api.dto.enums.WardField
import np.sthaniya.dpis.location.api.dto.mapper.MunicipalityMapper
import np.sthaniya.dpis.location.api.dto.mapper.WardMapper
import np.sthaniya.dpis.location.api.dto.response.DynamicWardProjection
import np.sthaniya.dpis.location.api.dto.response.WardDetailResponse
import np.sthaniya.dpis.location.api.dto.response.WardResponse
import np.sthaniya.dpis.location.api.dto.response.WardSummaryResponse
import np.sthaniya.dpis.location.domain.Ward
import np.sthaniya.dpis.common.util.GeometryConverter
import org.springframework.stereotype.Component

@Component
class WardMapperImpl(
    private val municipalityMapper: MunicipalityMapper,
    private val geometryConverter: GeometryConverter,
) : WardMapper {
    override fun toResponse(ward: Ward): WardResponse {
        require(ward.wardNumber != null) { "Ward number cannot be null" }
        require(ward.municipality != null) { "Municipality cannot be null" }

        return WardResponse(
            wardNumber = ward.wardNumber!!,
            area = ward.area,
            population = ward.population,
            latitude = ward.latitude,
            longitude = ward.longitude,
            officeLocation = ward.officeLocation,
            officeLocationNepali = ward.officeLocationNepali,
            municipality = municipalityMapper.toSummaryResponse(ward.municipality!!),
        )
    }

    override fun toDetailResponse(ward: Ward): WardDetailResponse {
        require(ward.wardNumber != null) { "Ward number cannot be null" }
        require(ward.municipality != null) { "Municipality cannot be null" }

        return WardDetailResponse(
            wardNumber = ward.wardNumber!!,
            area = ward.area,
            population = ward.population,
            latitude = ward.latitude,
            longitude = ward.longitude,
            officeLocation = ward.officeLocation,
            officeLocationNepali = ward.officeLocationNepali,
            municipality = municipalityMapper.toSummaryResponse(ward.municipality!!),
            geometry = geometryConverter.convertToGeoJson(ward.geometry),
        )
    }

    override fun toSummaryResponse(ward: Ward): WardSummaryResponse {
        require(ward.wardNumber != null) { "Ward number cannot be null" }

        return WardSummaryResponse(
            wardNumber = ward.wardNumber!!,
            population = ward.population,
        )
    }

    override fun toProjection(
        ward: Ward,
        fields: Set<WardField>,
    ): DynamicWardProjection {
        validateRequiredFields(ward)
        return DynamicWardProjection.from(ward, fields, geometryConverter)
    }

    private fun validateRequiredFields(ward: Ward) {
        require(ward.wardNumber != null) { "Ward number cannot be null" }
        require(ward.municipality != null) { "Municipality cannot be null" }
    }
}
