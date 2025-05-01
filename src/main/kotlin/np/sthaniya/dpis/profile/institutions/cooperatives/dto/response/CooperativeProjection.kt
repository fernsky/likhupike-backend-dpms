package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter

/**
 * Projection class for cooperative data that allows for dynamic property selection. This is used in
 * search results to return only the requested fields.
 *
 * Instead of wrapping properties in a nested "data" map, this class uses @JsonAnyGetter to include
 * the properties directly in the JSON object.
 */
data class CooperativeProjection(
        @get:JsonAnyGetter val properties: MutableMap<String, Any?> = mutableMapOf()
) {
    /** Support for setting properties dynamically. */
    @JsonAnySetter
    fun add(key: String, value: Any?) {
        properties[key] = value
    }

    /** Gets a property value by name. */
    operator fun get(key: String): Any? = properties[key]
}
