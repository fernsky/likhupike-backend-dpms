package np.sthaniya.dpis.auth.repository

import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*
/**
 * Primary repository interface for Permission entity operations.
 *
 * This repository provides:
 * - Standard JPA operations for Permission entities
 * - Specification-based querying capabilities
 * - Custom operations through [PermissionRepositoryCustom]
 *
 * Key Features:
 * - CRUD operations for Permission entities
 * - Dynamic querying support
 * - Permission type existence checks
 *
 * Integration Points:
 * - Works with Permission domain entity
 * - Uses PermissionType as primary key
 * - Supports Spring Data JPA patterns
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
