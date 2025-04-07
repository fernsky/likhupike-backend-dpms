package np.sthaniya.dpis.citizen.domain.entity

/**
 * Enumeration of document types that can be managed in the citizen management system.
 */
enum class DocumentType {
    /**
     * Citizen's profile photograph.
     */
    CITIZEN_PHOTO,
    
    /**
     * Front side of citizenship certificate.
     */
    CITIZENSHIP_FRONT,
    
    /**
     * Back side of citizenship certificate.
     */
    CITIZENSHIP_BACK,
    
    /**
     * Additional supporting document for verification or updates.
     */
    SUPPORTING_DOCUMENT
}
