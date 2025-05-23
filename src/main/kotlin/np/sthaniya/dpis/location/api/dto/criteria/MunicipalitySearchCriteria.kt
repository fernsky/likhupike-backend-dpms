package np.sthaniya.dpis.location.api.dto.criteria

import jakarta.validation.constraints.*
import np.sthaniya.dpis.location.api.dto.enums.MunicipalityField
import np.sthaniya.dpis.location.api.dto.enums.MunicipalitySortField
import np.sthaniya.dpis.location.domain.MunicipalityType
import org.springframework.data.domain.Sort
import java.math.BigDecimal
import java.util.*

data class MunicipalitySearchCriteria(
    @field:Size(max = 100, message = "Search term must not exceed 100 characters")
    val searchTerm: String? = null,
    @field:Pattern(regexp = "^[A-Z0-9]{1,10}$", message = "Code must be 1-10 uppercase letters or numbers")
    val code: String? = null,
    val districtCode: String? = null,
    val provinceCode: String? = null,
    val types: Set<MunicipalityType>? = null,
    val fields: Set<MunicipalityField> = MunicipalityField.DEFAULT_FIELDS,
    val includeTotals: Boolean = false,
    val includeGeometry: Boolean = false,
    val includeWards: Boolean = false,
    @field:Min(1) @field:Max(33)
    val minWards: Int? = null,
    @field:Min(1) @field:Max(33)
    val maxWards: Int? = null,
    @field:Positive
    val minPopulation: Long? = null,
    @field:Positive
    val maxPopulation: Long? = null,
    @field:Positive
    val minArea: BigDecimal? = null,
    @field:Positive
    val maxArea: BigDecimal? = null,
    @field:DecimalMin("-90") @field:DecimalMax("90")
    val latitude: BigDecimal? = null,
    @field:DecimalMin("-180") @field:DecimalMax("180")
    val longitude: BigDecimal? = null,
    @field:Positive
    val radiusKm: Double? = null,
    val sortBy: MunicipalitySortField = MunicipalitySortField.NAME,
    val sortDirection: Sort.Direction = Sort.Direction.ASC,
    @field:Min(0)
    val page: Int = 0,
    @field:Min(1)
    val pageSize: Int = 20,
)  {
    fun validate() {
        require(!(minWards != null && maxWards != null && minWards > maxWards)) {
            "Minimum wards cannot be greater than maximum wards"
        }
        require(!(minPopulation != null && maxPopulation != null && minPopulation > maxPopulation)) {
            "Minimum population cannot be greater than maximum population"
        }
        require(!(minArea != null && maxArea != null && minArea > maxArea)) {
            "Minimum area cannot be greater than maximum area"
        }
        require(!((latitude != null || longitude != null) && radiusKm == null)) {
            "Radius is required when searching by coordinates"
        }
    }
}
