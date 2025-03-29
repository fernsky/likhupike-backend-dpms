package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for managing Permission entities with JPA and custom implementations.
 *
 * Technical Details:
 * - Primary key: PermissionType (Enum)
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
 * val exists = repository.existsByType(PermissionType.CREATE_USER)
 * val permission = Permission(type = PermissionType.CREATE_USER)
 * repository.save(permission)
 *
 * // Specification Usage
 * val spec = PermissionSpecs.byCategory("ADMIN")
 * val permissions = repository.findAll(spec)
 *
 * // Custom Operations
 * val permSet = repository.findByTypes(setOf(PermissionType.CREATE_USER))
 * ```
 *
 * Performance Notes:
 * - Low memory footprint (typically < 100 records)
 * - Suitable for full table scans
 * - Consider caching for frequent lookups
 */
@Repository
interface PermissionRepository : 
    JpaRepository<Permission, PermissionType>, 
    JpaSpecificationExecutor<Permission>,
    PermissionRepositoryCustom {
    
    /**
     * Checks if a permission exists for the given type.
     *
     * @param type The PermissionType to check
     * @return true if permission exists, false otherwise
     */
    fun existsByType(type: PermissionType): Boolean
}
