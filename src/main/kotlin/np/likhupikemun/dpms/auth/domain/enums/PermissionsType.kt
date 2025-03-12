package np.likhupikemun.dpms.auth.domain.enums

enum class PermissionType {
    // User Management
    CREATE_USER,
    APPROVE_USER,
    EDIT_USER,
    DELETE_USER,
    VIEW_USER,
    RESET_USER_PASSWORD,
    ;

    fun getAuthority() = "PERMISSION_$name"
}