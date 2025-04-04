package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.Role
import np.sthaniya.dpis.auth.domain.enums.RoleType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for managing Role entities with JPA and custom implementations.
 *
 * Technical Details:
 * - Primary key: RoleType (Enum)
 * - No soft delete implementation
 * - Supports JPA Specifications
 * 
 * Transaction Behavior:
 * - Inherits transaction context
 * - Read operations don't require transaction
 * - Write operations require active transaction
 *
 * Usage Examples:
 * ```kotlin
 * // Basic CRUD
 * val exists = repository.existsByType(RoleType.SYSTEM_ADMINISTRATOR)
 * val role = Role(type = RoleType.SYSTEM_ADMINISTRATOR)
 * repository.save(role)
 *
 * // Specification Usage
 * val spec = RoleSpecs.byCategory("ADMIN")
 * val roles = repository.findAll(spec)
 *
 * // Custom Operations
 * val roleSet = repository.findByTypes(setOf(RoleType.SYSTEM_ADMINISTRATOR))
 * ```
 *
 * Performance Notes:
 * - Low memory footprint (typically < 20 records)
 * - Suitable for full table scans
 * - Consider caching for frequent lookups
 */
@Repository
interface RoleRepository : 
    JpaRepository<Role, RoleType>, 
    JpaSpecificationExecutor<Role>,
    RoleRepositoryCustom {
    
    /**
     * Checks if a role exists for the given type.
     *
     * @param type The RoleType to check
     * @return true if role exists, false otherwise
     */
    fun existsByType(type: RoleType): Boolean
}
