package np.sthaniya.dpis.profile.institutions.cooperatives.util

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.stereotype.Component

/**
 * Utility class for working with geographic data.
 */
@Component
class GeoUtils {
    
    // WGS84 coordinate reference system (SRID 4326)
    private val factory = GeometryFactory(PrecisionModel(), 4326)
    
    /**
     * Creates a Point from longitude and latitude coordinates.
     *
     * @param longitude The longitude coordinate
     * @param latitude The latitude coordinate
     * @return The created Point geometry
     */
    fun createPoint(longitude: Double, latitude: Double): Point {
        return factory.createPoint(Coordinate(longitude, latitude))
    }
}
