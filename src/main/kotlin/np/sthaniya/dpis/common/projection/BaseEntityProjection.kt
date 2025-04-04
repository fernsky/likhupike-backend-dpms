package np.sthaniya.dpis.common.projection

import com.fasterxml.jackson.annotation.JsonAnyGetter
import np.sthaniya.dpis.common.enum.EntityField

abstract class BaseEntityProjection<T, F : EntityField> {
    private val properties = mutableMapOf<String, Any?>()

    protected fun addField(
        field: F,
        value: Any?,
    ) {
        properties[field.toJsonFieldName()] = value
    }

    fun getValue(field: F): Any? = properties[field.toJsonFieldName()]

    @JsonAnyGetter
    fun getProperties(): Map<String, Any?> = properties

    abstract fun populateFields(
        entity: T,
        fields: Set<F>,
    )
}
