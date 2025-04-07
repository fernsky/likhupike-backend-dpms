package np.sthaniya.dpis.citizen.domain.entity

/**
 * Enumeration of possible states for documents in the verification workflow.
 */
enum class DocumentState {
    /**
     * Document has not been uploaded yet.
     * This is the initial state for required documents.
     */
    NOT_UPLOADED,
    
    /**
     * Document has been uploaded and is waiting for administrator review.
     */
    AWAITING_REVIEW,
    
    /**
     * Document has been rejected because it is too blurry or unclear.
     */
    REJECTED_BLURRY,
    
    /**
     * Document has been rejected because it is unsuitable.
     * This could be due to wrong document type, inappropriate content, etc.
     */
    REJECTED_UNSUITABLE,
    
    /**
     * Document has been rejected because it doesn't match other provided information.
     * This could be mismatched names, dates, etc.
     */
    REJECTED_MISMATCH,
    
    /**
     * Document has been rejected because it doesn't match with other submitted documents.
     * For example, citizenship front and back don't appear to belong to the same certificate.
     */
    REJECTED_INCONSISTENT,
    
    /**
     * Document has been approved and verified successfully.
     */
    APPROVED
}
