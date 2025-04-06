package np.sthaniya.dpis.location.service

import np.sthaniya.dpis.citizen.dto.shared.AddressDto
import np.sthaniya.dpis.location.domain.AddressComponents
import np.sthaniya.dpis.location.domain.District
import np.sthaniya.dpis.location.domain.Municipality
import np.sthaniya.dpis.location.domain.Province
import np.sthaniya.dpis.location.domain.Ward

/**
 * Service interface for validating address components and their hierarchical relationships.
 * Returns actual entity objects that can be directly used in Address entities.
 */
interface AddressValidationService {
    
    /**
     * Validates a complete address, checking that all components exist and have valid relationships.
     * 
     * @param addressDto The address data to validate
     * @throws AddressException if any component of the address is invalid
     * @return The validated address components including actual entities for further use
     */
    fun validateAddress(addressDto: AddressDto): AddressComponents
    
    /**
     * Validates the existence of a province.
     * 
     * @param provinceCode The province code to validate
     * @throws AddressException if the province doesn't exist
     * @return The validated Province entity
     */
    fun validateProvince(provinceCode: String): Province
    
    /**
     * Validates the existence of a district and its relationship to a province.
     * 
     * @param districtCode The district code to validate
     * @param provinceCode Optional province code to check relationship
     * @throws AddressException if the district doesn't exist or doesn't belong to the province
     * @return The validated District entity
     */
    fun validateDistrict(districtCode: String, provinceCode: String? = null): District
    
    /**
     * Validates the existence of a municipality and its relationship to a district.
     * 
     * @param municipalityCode The municipality code to validate
     * @param districtCode Optional district code to check relationship
     * @throws AddressException if the municipality doesn't exist or doesn't belong to the district
     * @return The validated Municipality entity
     */
    fun validateMunicipality(municipalityCode: String, districtCode: String? = null): Municipality
    
    /**
     * Validates the existence of a ward and its relationship to a municipality.
     * 
     * @param wardNumber The ward number to validate
     * @param municipalityCode The municipality code to check relationship
     * @throws AddressException if the ward doesn't exist or doesn't belong to the municipality
     * @return The validated Ward entity
     */
    fun validateWard(wardNumber: Int, municipalityCode: String): Ward
}
