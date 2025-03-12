package np.likhupikemun.dpms.auth.service

import np.likhupikemun.dpms.auth.domain.entity.User
import np.likhupikemun.dpms.auth.dto.CreateUserDto
import np.likhupikemun.dpms.auth.dto.UserPermissionsDto
import java.util.*

interface UserService {
    fun createUser(createUserDto: CreateUserDto): User
    fun findByEmail(email: String): User?
    fun updatePermissions(userId: UUID, permissions: UserPermissionsDto): User
    fun resetPassword(userId: UUID, newPassword: String): User
    fun approveUser(userId: UUID, approvedBy: UUID): User
    fun deleteUser(userId: UUID, deletedBy: String): User
}
