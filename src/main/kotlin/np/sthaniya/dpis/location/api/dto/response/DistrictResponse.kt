package np.sthaniya.dpis.location.api.dto.response

import org.geojson.GeoJsonObject
import java.math.BigDecimal
import java.util.*

data class DistrictResponse(
    val code: String,
    val name: String,
    val nameNepali: String,
    val area: BigDecimal?,
    val population: Long?,
    val headquarter: String?,
    val headquarterNepali: String?,
    val province: ProvinceSummaryResponse,
    val municipalityCount: Int,
    val totalPopulation: Long?,
    val totalArea: BigDecimal?,
)

data class DistrictDetailResponse(
    val code: String,
    val name: String,
    val nameNepali: String,
    val area: BigDecimal?,
    val population: Long?,
    val headquarter: String?,
    val headquarterNepali: String?,
    val province: ProvinceSummaryResponse,
    val municipalities: List<MunicipalitySummaryResponse>,
    val geometry: GeoJsonObject?,
)

data class DistrictSummaryResponse(
    val code: String,
    val name: String,
    val nameNepali: String,
    val municipalityCount: Int,
)
