package np.sthaniya.dpis.citizen.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

/**
 * Represents a physical address in Nepal's administrative structure.
 * 
 * This embeddable entity contains address components following Nepal's
 * federal structure with provinces, districts, municipalities, and wards.
 * It can be embedded into other entities like Citizen for representing
 * permanent or temporary addresses.
 * 
 * @property province The province name (e.g., "Bagmati")
 * @property district The district name (e.g., "Kathmandu")
 * @property municipality The municipality name (e.g., "Kathmandu Metropolitan City")
 * @property wardNumber The ward number (1-33)
 * @property streetAddress Optional detailed street address or location description
 */
@Embeddable
data class Address(
    @Column(nullable = false)
    var province: String,
    
    @Column(nullable = false)
    var district: String,
    
    @Column(nullable = false)
    var municipality: String,
    
    @Column(name = "ward_number", nullable = false)
    var wardNumber: Int,
    
    @Column(name = "street_address")
    var streetAddress: String? = null
)
