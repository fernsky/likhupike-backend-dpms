package np.sthaniya.dpis.citizen.service

import np.sthaniya.dpis.citizen.dto.management.CreateCitizenDto
import np.sthaniya.dpis.citizen.dto.management.UpdateCitizenDto
import np.sthaniya.dpis.citizen.dto.response.CitizenResponse
import np.sthaniya.dpis.citizen.dto.response.DocumentUploadResponse
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Service interface for administrative management of citizen records.
 */
interface CitizenManagementService {
    
    /**
     * Creates a new citizen record in the system.
     * 
     * @param createCitizenDto The data for creating a new citizen
     * @return The created citizen as a response DTO
     * @throws CitizenException if there are validation errors or duplicate data
     */
    fun createCitizen(createCitizenDto: CreateCitizenDto): CitizenResponse
    
    /**
     * Retrieves a citizen by their ID.
     * 
     * @param id The unique identifier of the citizen
     * @return The citizen data as a response DTO
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     */
    fun getCitizenById(id: UUID): CitizenResponse
    
    /**
     * Approves a citizen record.
     * 
     * @param id The unique identifier of the citizen to approve
     * @param approvedBy The ID of the user approving the citizen record
     * @return The updated citizen data as a response DTO
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.CITIZEN_ALREADY_APPROVED if the citizen is already approved
     */
    fun approveCitizen(id: UUID, approvedBy: UUID): CitizenResponse
    
    /**
     * Updates an existing citizen record in the system.
     *
     * @param id The unique identifier of the citizen to update
     * @param updateCitizenDto The data for updating the citizen
     * @return The updated citizen as a response DTO
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException if there are validation errors or duplicate data
     */
    fun updateCitizen(id: UUID, updateCitizenDto: UpdateCitizenDto, updatedBy: UUID): CitizenResponse
    
    /**
     * Soft deletes a citizen record.
     * 
     * @param id The unique identifier of the citizen to delete
     * @param deletedBy The ID of the user deleting the citizen record
     * @return The deleted citizen data as a response DTO
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.CITIZEN_ALREADY_DELETED if the citizen is already deleted
     */
    fun deleteCitizen(id: UUID, deletedBy: UUID): CitizenResponse
    
    /**
     * Uploads or replaces a citizen's photo.
     *
     * @param id The unique identifier of the citizen
     * @param photo The photo file to upload
     * @param updatedBy The ID of the user uploading the photo
     * @return Details about the uploaded photo
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.INVALID_DOCUMENT_FORMAT if the file format is not supported
     * @throws CitizenException.CitizenErrorCode.DOCUMENT_TOO_LARGE if the file exceeds size limits
     */
    fun uploadCitizenPhoto(id: UUID, photo: MultipartFile, updatedBy: UUID): DocumentUploadResponse
    
    /**
     * Uploads or replaces the front page of a citizen's citizenship certificate.
     *
     * @param id The unique identifier of the citizen
     * @param document The document file to upload
     * @param updatedBy The ID of the user uploading the document
     * @return Details about the uploaded document
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.INVALID_DOCUMENT_FORMAT if the file format is not supported
     * @throws CitizenException.CitizenErrorCode.DOCUMENT_TOO_LARGE if the file exceeds size limits
     */
    fun uploadCitizenshipFront(id: UUID, document: MultipartFile, updatedBy: UUID): DocumentUploadResponse
    
    /**
     * Uploads or replaces the back page of a citizen's citizenship certificate.
     *
     * @param id The unique identifier of the citizen
     * @param document The document file to upload
     * @param updatedBy The ID of the user uploading the document
     * @return Details about the uploaded document
     * @throws CitizenException.CitizenErrorCode.CITIZEN_NOT_FOUND if the citizen doesn't exist
     * @throws CitizenException.CitizenErrorCode.INVALID_DOCUMENT_FORMAT if the file format is not supported
     * @throws CitizenException.CitizenErrorCode.DOCUMENT_TOO_LARGE if the file exceeds size limits
     */
    fun uploadCitizenshipBack(id: UUID, document: MultipartFile, updatedBy: UUID): DocumentUploadResponse
}

