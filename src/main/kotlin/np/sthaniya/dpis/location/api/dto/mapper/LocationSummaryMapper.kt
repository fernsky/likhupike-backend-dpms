package np.sthaniya.dpis.location.api.dto.mapper

import np.sthaniya.dpis.location.api.dto.response.DistrictSummaryResponse
import np.sthaniya.dpis.location.api.dto.response.MunicipalitySummaryResponse
import np.sthaniya.dpis.location.api.dto.response.ProvinceSummaryResponse
import np.sthaniya.dpis.location.domain.District
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.Province

interface LocationSummaryMapper {
    fun toDistrictSummary(district: District): DistrictSummaryResponse

    fun toProvinceSummary(province: Province): ProvinceSummaryResponse

    fun toMunicipalitySummary(municipality: Municipality): MunicipalitySummaryResponse
}
