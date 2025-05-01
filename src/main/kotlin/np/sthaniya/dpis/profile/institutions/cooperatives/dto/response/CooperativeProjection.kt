
package np.sthaniya.dpis.profile.institutions.cooperatives.dto.response

/**
 * Projection class for cooperative data that allows for dynamic property selection.
 * This is used in search results to return only the requested fields.
 *
 * @param data A map containing the requested cooperative data fields
 */
data class CooperativeProjection(
    val data: Map<String, Any?>
)
