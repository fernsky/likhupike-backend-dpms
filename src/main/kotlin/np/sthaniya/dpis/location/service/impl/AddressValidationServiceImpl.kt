package np.sthaniya.dpis.location.service.impl

import np.sthaniya.dpis.citizen.dto.shared.AddressDto
import np.sthaniya.dpis.location.domain.*
import np.sthaniya.dpis.location.exception.AddressException
import np.sthaniya.dpis.location.exception.AddressException.AddressErrorCode
import np.sthaniya.dpis.location.repository.*
import np.sthaniya.dpis.location.service.*
import np.sthaniya.dpis.common.service.I18nMessageService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressValidationServiceImpl(
    private val provinceService: ProvinceService,
    private val districtService: DistrictService,
    private val municipalityService: MunicipalityService,
    private val wardService: WardService,
    private val provinceRepository: ProvinceRepository,
    private val districtRepository: DistrictRepository,
    private val municipalityRepository: MunicipalityRepository,
    private val wardRepository: WardRepository,
    private val i18nMessageService: I18nMessageService
) : AddressValidationService {

    private val logger = LoggerFactory.getLogger(AddressValidationServiceImpl::class.java)

    @Transactional(readOnly = true)
    override fun validateAddress(addressDto: AddressDto): AddressComponents {
        logger.debug("Validating address: $addressDto")
        
        // Validate and fetch province entity
        val provinceResponse = try {
            provinceService.getProvince(addressDto.provinceCode)
        } catch (e: Exception) {
            logger.error("Province validation failed for code: ${addressDto.provinceCode}", e)
            throw AddressException(
                AddressErrorCode.PROVINCE_NOT_FOUND,
                i18nMessageService.getMessage("location.province.not_found", arrayOf(addressDto.provinceCode as Any))
            )
        }
        
        // Get actual Province entity 
        val province = provinceRepository.findByCodeIgnoreCase(addressDto.provinceCode)
            .orElseThrow {
                AddressException(
                    AddressErrorCode.PROVINCE_NOT_FOUND,
                    i18nMessageService.getMessage("location.province.not_found", arrayOf(addressDto.provinceCode as Any))
                )
            }
        
        // Validate and fetch district entity
        val districtResponse = try {
            val district = districtService.getDistrict(addressDto.districtCode)
            
            if (district.province?.code != addressDto.provinceCode) {
                throw AddressException(
                    AddressErrorCode.DISTRICT_NOT_IN_PROVINCE,
                    i18nMessageService.getMessage("location.district.not_in_province", 
                        arrayOf(district.name as Any, provinceResponse.name as Any))
                )
            }
            district
        } catch (e: AddressException) {
            throw e
        } catch (e: Exception) {
            logger.error("District validation failed for code: ${addressDto.districtCode}", e)
            throw AddressException(
                AddressErrorCode.DISTRICT_NOT_FOUND,
                i18nMessageService.getMessage("location.district.not_found", arrayOf(addressDto.districtCode as Any))
            )
        }
        
        // Get actual District entity
        val district = districtRepository.findByCodeIgnoreCase(addressDto.districtCode)
            .orElseThrow {
                AddressException(
                    AddressErrorCode.DISTRICT_NOT_FOUND,
                    i18nMessageService.getMessage("location.district.not_found", arrayOf(addressDto.districtCode as Any))
                )
            }
        
        // Validate and fetch municipality entity
        val municipalityResponse = try {
            val municipality = municipalityService.getMunicipality(addressDto.municipalityCode)
            
            if (municipality.district?.code != addressDto.districtCode) {
                throw AddressException(
                    AddressErrorCode.MUNICIPALITY_NOT_IN_DISTRICT,
                    i18nMessageService.getMessage("location.municipality.not_in_district", 
                        arrayOf(municipality.name as Any, districtResponse.name as Any))
                )
            }
            municipality
        } catch (e: AddressException) {
            throw e
        } catch (e: Exception) {
            logger.error("Municipality validation failed for code: ${addressDto.municipalityCode}", e)
            throw AddressException(
                AddressErrorCode.MUNICIPALITY_NOT_FOUND,
                i18nMessageService.getMessage("location.municipality.not_found", arrayOf(addressDto.municipalityCode as Any))
            )
        }
        
        // Get actual Municipality entity
        val municipality = municipalityRepository.findByCodeIgnoreCase(addressDto.municipalityCode)
            .orElseThrow {
                AddressException(
                    AddressErrorCode.MUNICIPALITY_NOT_FOUND,
                    i18nMessageService.getMessage("location.municipality.not_found", arrayOf(addressDto.municipalityCode as Any))
                )
            }
        
        // Validate ward existence
        try {
            wardService.validateWardExists(addressDto.wardNumber, addressDto.municipalityCode)
        } catch (e: Exception) {
            logger.error("Ward validation failed for municipality: ${addressDto.municipalityCode}, ward: ${addressDto.wardNumber}", e)
            throw AddressException(
                AddressErrorCode.WARD_NOT_IN_MUNICIPALITY,
                i18nMessageService.getMessage("location.ward.not_in_municipality", 
                    arrayOf(addressDto.wardNumber as Any, municipalityResponse.name as Any))
            )
        }
        
        // Get actual Ward entity
        val ward = wardRepository.findByWardNumberAndMunicipalityCode(addressDto.wardNumber, addressDto.municipalityCode)
            .orElseThrow {
                AddressException(
                    AddressErrorCode.WARD_NOT_FOUND,
                    i18nMessageService.getMessage("location.ward.not_found", 
                        arrayOf(addressDto.wardNumber as Any, addressDto.municipalityCode as Any))
                )
            }
        
        return AddressComponents(
            province = province,
            district = district,
            municipality = municipality,
            ward = ward,
            streetAddress = addressDto.streetAddress
        )
    }

    override fun validateProvince(provinceCode: String): Province {
        try {
            provinceService.validateProvinceExists(provinceCode)
            return provinceRepository.findByCodeIgnoreCase(provinceCode)
                .orElseThrow {
                    AddressException(
                        AddressErrorCode.PROVINCE_NOT_FOUND,
                        i18nMessageService.getMessage("location.province.not_found", arrayOf(provinceCode))
                    )
                }
        } catch (e: AddressException) {
            throw e
        } catch (e: Exception) {
            throw AddressException(
                AddressErrorCode.PROVINCE_NOT_FOUND,
                i18nMessageService.getMessage("location.province.not_found", arrayOf(provinceCode))
            )
        }
    }

    override fun validateDistrict(districtCode: String, provinceCode: String?): District {
        try {
            districtService.validateDistrictExists(districtCode)
            
            val district = districtRepository.findByCodeIgnoreCase(districtCode)
                .orElseThrow {
                    AddressException(
                        AddressErrorCode.DISTRICT_NOT_FOUND,
                        i18nMessageService.getMessage("location.district.not_found", arrayOf(districtCode as Any))
                    )
                }
            
            if (provinceCode != null && district.province?.code != provinceCode) {
                throw AddressException(
                    AddressErrorCode.DISTRICT_NOT_IN_PROVINCE,
                    i18nMessageService.getMessage("location.district.not_in_province", 
                        arrayOf(district.name as Any, provinceCode as Any))
                )
            }
            
            return district
        } catch (e: AddressException) {
            throw e
        } catch (e: Exception) {
            throw AddressException(
                AddressErrorCode.DISTRICT_NOT_FOUND,
                i18nMessageService.getMessage("location.district.not_found", arrayOf(districtCode as Any))
            )
        }
    }

    override fun validateMunicipality(municipalityCode: String, districtCode: String?): Municipality {
        try {
            municipalityService.validateMunicipalityExists(municipalityCode)
            
            val municipality = municipalityRepository.findByCodeIgnoreCase(municipalityCode)
                .orElseThrow {
                    AddressException(
                        AddressErrorCode.MUNICIPALITY_NOT_FOUND,
                        i18nMessageService.getMessage("location.municipality.not_found", arrayOf(municipalityCode as Any))
                    )
                }
            
            if (districtCode != null && municipality.district?.code != districtCode) {
                throw AddressException(
                    AddressErrorCode.MUNICIPALITY_NOT_IN_DISTRICT,
                    i18nMessageService.getMessage("location.municipality.not_in_district", 
                        arrayOf(municipality.name as Any, districtCode as Any))
                )
            }
            
            return municipality
        } catch (e: AddressException) {
            throw e
        } catch (e: Exception) {
            throw AddressException(
                AddressErrorCode.MUNICIPALITY_NOT_FOUND,
                i18nMessageService.getMessage("location.municipality.not_found", arrayOf(municipalityCode as Any))
            )
        }
    }

    override fun validateWard(wardNumber: Int, municipalityCode: String): Ward {
        try {
            wardService.validateWardExists(wardNumber, municipalityCode)
            
            return wardRepository.findByWardNumberAndMunicipalityCode(wardNumber, municipalityCode)
                .orElseThrow {
                    AddressException(
                        AddressErrorCode.WARD_NOT_IN_MUNICIPALITY,
                        i18nMessageService.getMessage("location.ward.not_in_municipality", 
                            arrayOf(wardNumber as Any, municipalityCode as Any))
                    )
                }
        } catch (e: AddressException) {
            throw e
        } catch (e: Exception) {
            throw AddressException(
                AddressErrorCode.WARD_NOT_IN_MUNICIPALITY,
                i18nMessageService.getMessage("location.ward.not_in_municipality", 
                    arrayOf(wardNumber as Any, municipalityCode as Any))
            )
        }
    }
}
