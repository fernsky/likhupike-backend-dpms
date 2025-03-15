package np.likhupikemun.dpms.fixtures

import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.domain.entity.Permission
import np.likhupikemun.dpms.auth.domain.enums.PermissionType
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.auth.dto.LoginRequest
import np.likhupikemun.dpms.auth.dto.RegisterRequest
import java.util.*

object UserTestFixture {
    val DEFAULT_PASSWORD = "Test@123"
    val ADMIN_EMAIL = "admin@test.com"
    val WARD_USER_EMAIL = "ward@test.com"
    val REGULAR_USER_EMAIL = "user@test.com"

    fun createRegisterRequest(
        email: String = REGULAR_USER_EMAIL,
        password: String = DEFAULT_PASSWORD,
        confirmPassword: String = DEFAULT_PASSWORD, // Added confirm password param
        isWardLevelUser: Boolean = false,
        wardNumber: Int? = null
    ) = RegisterRequest(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        isWardLevelUser = isWardLevelUser,
        wardNumber = wardNumber
    )

    fun createLoginRequest(
        email: String = REGULAR_USER_EMAIL,
        password: String = DEFAULT_PASSWORD
    ) = LoginRequest(
        email = email,
        password = password
    )

    private fun createPermission(type: PermissionType) = Permission(type = type)

    fun createUser(
        id: UUID = UUID.randomUUID(),
        email: String = REGULAR_USER_EMAIL,
        password: String = DEFAULT_PASSWORD,
        isApproved: Boolean = false,
        isWardLevelUser: Boolean = false,
        wardNumber: Int? = null,
        permissions: Set<PermissionType> = emptySet()
    ) = User().apply {
        this.id = id
        this.email = email
        this.setPassword(password)
        this.isApproved = isApproved
        this.isWardLevelUser = isWardLevelUser
        this.wardNumber = wardNumber
        
        // Clear existing permissions and add new ones
        this.clearPermissions()
        permissions.forEach { permType ->
            this.addPermission(createPermission(permType))
        }
    }

    fun createAdminUser() = createUser(
        email = ADMIN_EMAIL,
        isApproved = true,
        permissions = setOf(
            PermissionType.CREATE_USER,
            PermissionType.VIEW_USER,
            PermissionType.EDIT_USER,
            PermissionType.DELETE_USER,
            PermissionType.APPROVE_USER
        )
    )

    fun createApprovedUser() = createUser(
        isApproved = true,
        permissions = setOf(PermissionType.VIEW_USER)
    )

    fun createWardUser() = createUser(
        email = WARD_USER_EMAIL,
        isApproved = true,
        isWardLevelUser = true,
        wardNumber = 1,
        permissions = setOf(PermissionType.VIEW_USER)
    )
}
