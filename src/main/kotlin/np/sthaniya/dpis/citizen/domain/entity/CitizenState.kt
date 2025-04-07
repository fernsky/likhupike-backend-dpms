package np.sthaniya.dpis.citizen.domain.entity

/**
 * Enumeration of possible states for a citizen in the verification workflow.
 */
enum class CitizenState {
    /**
     * Initial state for newly self-registered citizens.
     * Indicates that the registration is complete but not yet verified.
     */
    PENDING_REGISTRATION,
    
    /**
     * Indicates that a citizen's registration is currently being reviewed by an administrator.
     * This state is set when verification process has started.
     */
    UNDER_REVIEW,
    
    /**
     * Indicates that there are issues with the registration that require action from the citizen.
     * This could be missing or invalid information, documents, etc.
     */
    ACTION_REQUIRED,
    
    /**
     * Indicates that the registration has been rejected.
     * This could be due to invalid identity information, suspected fraud, etc.
     */
    REJECTED,
    
    /**
     * Indicates that the registration has been fully verified and approved.
     * Citizen has full access to services in this state.
     */
    APPROVED
}
