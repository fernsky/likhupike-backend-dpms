package np.sthaniya.dpis.auth.test

import np.sthaniya.dpis.auth.domain.entity.Permission
import np.sthaniya.dpis.auth.domain.entity.Role
import np.sthaniya.dpis.auth.domain.entity.User
import np.sthaniya.dpis.auth.domain.entity.UserPermission
import np.sthaniya.dpis.auth.domain.entity.UserRole
import np.sthaniya.dpis.auth.domain.enums.RoleType
import np.sthaniya.dpis.auth.domain.enums.PermissionType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * A test data factory for creating User entities and related objects for testing purposes.
 * 
 * This factory provides methods to create different types of users with various roles and permissions.
 * It is designed to be used in unit and integration tests.
 */
object UserTestDataFactory {
    private val passwordEncoder = BCryptPasswordEncoder()
    
    /**
     * Creates a standard test user with basic attributes.
     *
     * @param id Optional UUID to assign to the user (generates random UUID if not provided)
     * @param email Email address for the user
     * @param password Raw password (will be encoded)
     * @param isDeleted Whether the user account is marked as deleted
     * @param isWardLevelUser Whether the user is a ward-level user
     * @param wardNumber The ward number for ward-level users
     * @param isApproved Whether the user is approved
     * @param phoneNumber User's phone number
     * @return A configured User entity
     */
    fun createUser(
        id: UUID = UUID.randomUUID(),
        email: String = "test@example.com",
        password: String = "password",
        isDeleted: Boolean = false,
        isWardLevelUser: Boolean = false,
        wardNumber: Int? = null,
        isApproved: Boolean = true,
        phoneNumber: String? = "9800000000"
    ): User {
        val user = User()
        user.id = id
        user.email = email
        user.setPassword(passwordEncoder.encode(password))
        user.isDeleted = isDeleted
        user.isWardLevelUser = isWardLevelUser
        user.wardNumber = wardNumber
        user.isApproved = isApproved
        user.phoneNumber = phoneNumber
        user.createdAt = Instant.now()
        user.updatedAt = Instant.now()
        
        if (isDeleted) {
            user.deletedAt = LocalDateTime.now()
            user.deletedBy = UUID.randomUUID() // Mock admin ID
        }
        
        if (isApproved) {
            user.approvedAt = LocalDateTime.now()
            user.approvedBy = UUID.randomUUID() // Mock admin ID
        }
        
        return user
    }

    /**
     * Creates a system administrator user with SYSTEM_ADMINISTRATOR role.
     *
     * @return A User entity with system administrator role
     */
    fun createSystemAdministrator(): User {
        val user = createUser(
            email = "admin@system.com"
        )
        
        // Create role
        val adminRole = createRole(RoleType.SYSTEM_ADMINISTRATOR)
        
        // Create and assign permissions
        val createUserPermission = createPermission(PermissionType.CREATE_USER)
        val approveUserPermission = createPermission(PermissionType.APPROVE_USER)
        
        // Add permissions to user
        user.addPermission(createUserPermission)
        user.addPermission(approveUserPermission)
        
        // Add role to user
        user.addRole(adminRole)
        
        return user
    }

    /**
     * Creates a super admin user for integration tests.
     */
    fun createSuperAdmin(): User {
        return createSystemAdministrator()
    }

    /**
     * Creates a viewer user for integration tests.
     */
    fun createViewer(): User {
        val user = createUser(
            email = "viewer@example.com"
        )
        
        val viewerRole = createRole(RoleType.PUBLIC_USER)
        user.addRole(viewerRole)
        
        return user
    }
    
    /**
     * Creates a ward-level user for testing ward-specific functionality.
     * 
     * @param wardNumber The ward number to assign to the user
     * @return A User entity with ward-level access
     */
    fun createWardLevelUser(wardNumber: Int = 1): User {
        return createUser(
            email = "ward-user@example.com",
            isWardLevelUser = true,
            wardNumber = wardNumber
        )
    }
    
    /**
     * Creates a regular user with no special roles or permissions.
     *
     * @return A basic User entity
     */
    fun createRegularUser(): User {
        return createUser(
            email = "user@example.com"
        )
    }
    
    /**
     * Creates a role entity.
     *
     * @param roleType The RoleType enum value
     * @return A Role entity
     */
    fun createRole(roleType: RoleType): Role {
        return Role(roleType)
    }
    
    /**
     * Creates a permission entity.
     *
     * @param permissionType The PermissionType enum value
     * @return A Permission entity
     */
    fun createPermission(permissionType: PermissionType): Permission {
        return Permission(permissionType)
    }
    
    /**
     * Creates a user permission association.
     *
     * @param user The user to associate the permission with
     * @param permission The permission to assign
     * @param id Optional UUID to assign to the user permission
     * @return A UserPermission entity
     */
    fun createUserPermission(
        user: User,
        permission: Permission,
        id: UUID = UUID.randomUUID()
    ): UserPermission {
        val userPermission = UserPermission(user, permission)
        userPermission.id = id
        userPermission.createdAt = Instant.now()
        userPermission.updatedAt = Instant.now()
        return userPermission
    }
    
    /**
     * Creates a user role association.
     *
     * @param user The user to associate the role with
     * @param role The role to assign
     * @param id Optional UUID to assign to the user role
     * @return A UserRole entity
     */
    fun createUserRole(
        user: User,
        role: Role,
        id: UUID = UUID.randomUUID()
    ): UserRole {
        val userRole = UserRole(user, role)
        userRole.id = id
        userRole.createdAt = Instant.now()
        userRole.updatedAt = Instant.now()
        return userRole
    }
}
