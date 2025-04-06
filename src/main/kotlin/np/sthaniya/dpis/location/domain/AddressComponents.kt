package np.sthaniya.dpis.location.domain

import np.sthaniya.dpis.citizen.domain.entity.Address

/**
 * Contains validated address entities and components.
 * This class aggregates full address information, including the actual
 * administrative division entities, for constructing complete Address objects.
 */
data class AddressComponents(
    // Full entity references
    val province: Province,
    val district: District,
    val municipality: Municipality,
    val ward: Ward,
    
    // Additional address details
    val streetAddress: String? = null
) {
    /**
     * Converts AddressComponents to an Address entity.
     */
    fun toAddress(): Address {
        return Address(
            province = province,
            district = district,
            municipality = municipality,
            ward = ward,
            streetAddress = streetAddress
        )
    }
}
