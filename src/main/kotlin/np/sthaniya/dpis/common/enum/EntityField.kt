package np.sthaniya.dpis.common.enum

interface EntityField {
    fun fieldName(): String

    fun toJsonFieldName(): String

    fun toPropertyName(): String = toJsonFieldName() // Add this method
}
