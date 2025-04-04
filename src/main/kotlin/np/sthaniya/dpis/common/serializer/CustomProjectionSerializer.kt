package np.sthaniya.dpis.common.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import np.sthaniya.dpis.common.projection.EntityProjection

class CustomProjectionSerializer : JsonSerializer<EntityProjection<*>>() {
    override fun serialize(value: EntityProjection<*>, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        value.getRawData().forEach { (key, value) ->
            gen.writeObjectField(key, value)
        }
        gen.writeEndObject()
    }
}
