package np.sthaniya.dpis.profile.location.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal
import java.util.UUID

data class MunicipalityResponse(
    val id: UUID?,
    val name: String,
    val province: String,
    val district: String,
    val rightmostLatitude: BigDecimal,
    val leftmostLatitude: BigDecimal,
    val bottommostLongitude: BigDecimal,
    val topmostLongitude: BigDecimal,
    val lowestAltitude: BigDecimal?,
    val highestAltitude: BigDecimal?,
    val areaInSquareKilometers: BigDecimal,
    val wardsCount: Int
)

data class MunicipalityCreateRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @field:NotBlank(message = "Province is required")
    val province: String,
    
    @field:NotBlank(message = "District is required")
    val district: String,
    
    @field:NotNull(message = "Rightmost latitude is required")
    val rightmostLatitude: BigDecimal,
    
    @field:NotNull(message = "Leftmost latitude is required")
    val leftmostLatitude: BigDecimal,
    
    @field:NotNull(message = "Bottommost longitude is required")
    val bottommostLongitude: BigDecimal,
    
    @field:NotNull(message = "Topmost longitude is required")
    val topmostLongitude: BigDecimal,
    
    val lowestAltitude: BigDecimal? = null,
    
    val highestAltitude: BigDecimal? = null,
    
    @field:NotNull(message = "Area in square kilometers is required")
    @field:PositiveOrZero(message = "Area must be positive or zero")
    val areaInSquareKilometers: BigDecimal
)

data class MunicipalityUpdateRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @field:NotBlank(message = "Province is required")
    val province: String,
    
    @field:NotBlank(message = "District is required")
    val district: String
)

data class MunicipalityGeoLocationUpdateRequest(
    @field:NotNull(message = "Rightmost latitude is required")
    val rightmostLatitude: BigDecimal,
    
    @field:NotNull(message = "Leftmost latitude is required")
    val leftmostLatitude: BigDecimal,
    
    @field:NotNull(message = "Bottommost longitude is required")
    val bottommostLongitude: BigDecimal,
    
    @field:NotNull(message = "Topmost longitude is required")
    val topmostLongitude: BigDecimal,
    
    val lowestAltitude: BigDecimal? = null,
    
    val highestAltitude: BigDecimal? = null,
    
    @field:NotNull(message = "Area in square kilometers is required")
    @field:PositiveOrZero(message = "Area must be positive or zero")
    val areaInSquareKilometers: BigDecimal
)
