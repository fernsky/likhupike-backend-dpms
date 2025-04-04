package np.sthaniya.dpis.common.projection

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import np.sthaniya.dpis.common.enum.EntityField
import np.sthaniya.dpis.common.serializer.CustomProjectionSerializer

@JsonSerialize(using = CustomProjectionSerializer::class)
interface EntityProjection<F : EntityField> {
    fun getValue(field: F): Any?

    fun getRawData(): Map<String, Any?>
}
