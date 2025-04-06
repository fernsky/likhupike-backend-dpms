package np.sthaniya.dpis.citizen.service

import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.model.Citizen
import java.util.UUID

/**
 * Service interface for administrative management of citizen records.
 */
interface CitizenManagementService {
    
    /**
     * Creates a new citizen record in the system.
     * 
     * @param createCitizenDto The data for creating a new citizen
     * @return The created citizen entity with its generated ID
     */
    fun createCitizen(createCitizenDto: CreateCitizenDto): Citizen
}
