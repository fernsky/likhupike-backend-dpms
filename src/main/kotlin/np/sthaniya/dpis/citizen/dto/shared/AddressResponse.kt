package np.sthaniya.dpis.citizen.dto.shared

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data transfer object representing address information for API responses.
 */
@Schema(
    description = "Address information following Nepal's administrative structure",
    title = "Address Response"
)
data class AddressResponse(
    @Schema(
        description = "Province code",
        example = "3"
    )
    val provinceCode: String,
    
    @Schema(
        description = "Province name",
        example = "Bagmati"
    )
    val provinceName: String,
    
    @Schema(
        description = "Province name in Nepali",
        example = "बाग्मती"
    )
    val provinceNameNepali: String?,
    
    @Schema(
        description = "District code",
        example = "27"
    )
    val districtCode: String,
    
    @Schema(
        description = "District name",
        example = "Kathmandu"
    )
    val districtName: String,
    
    @Schema(
        description = "District name in Nepali",
        example = "काठमाडौं"
    )
    val districtNameNepali: String?,
    
    @Schema(
        description = "Municipality code",
        example = "27002"
    )
    val municipalityCode: String,
    
    @Schema(
        description = "Municipality name",
        example = "Kathmandu Metropolitan City"
    )
    val municipalityName: String,
    
    @Schema(
        description = "Municipality name in Nepali",
        example = "काठमाडौं महानगरपालिका"
    )
    val municipalityNameNepali: String?,
    
    @Schema(
        description = "Municipality type",
        example = "METROPOLITAN_CITY"
    )
    val municipalityType: String,
    
    @Schema(
        description = "Ward number",
        example = "5"
    )
    val wardNumber: Int,
    
    @Schema(
        description = "Detailed street address or location description",
        example = "Near Pashupati Temple, Gaurighat"
    )
    val streetAddress: String?
)
