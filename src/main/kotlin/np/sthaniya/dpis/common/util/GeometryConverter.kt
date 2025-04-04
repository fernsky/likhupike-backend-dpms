package np.sthaniya.dpis.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import np.sthaniya.dpis.common.dto.*
import org.geojson.GeoJsonObject
import org.geojson.LngLatAlt
import org.locationtech.jts.geom.*
import org.locationtech.jts.io.geojson.GeoJsonWriter
import org.locationtech.jts.util.GeometricShapeFactory
import org.springframework.stereotype.Component
import kotlin.math.cos

@Component
class GeometryConverter(
    private val geometryFactory: GeometryFactory = GeometryFactory(PrecisionModel(), 4326),
) {
    private val geoJsonWriter = GeoJsonWriter()

    // Helper function to convert Array<Double> to JTS Coordinate
    private fun Array<Double>.toJtsCoordinate(): org.locationtech.jts.geom.Coordinate =
        org.locationtech.jts.geom
            .Coordinate(this[1], this[0]) // lon, lat

    fun toGeometry(request: GeometryRequest): Geometry =
        when (request.type.lowercase()) {
            "point" -> createPoint(request)
            "multipoint" -> createMultiPoint(request)
            "linestring" -> createLineString(request)
            "multilinestring" -> createMultiLineString(request)
            "polygon" -> createPolygon(request)
            "multipolygon" -> createMultiPolygon(request)
            "circle" -> createCircle(request)
            "rectangle" -> createRectangle(request)
            "collection" -> createGeometryCollection(request)
            else -> throw IllegalArgumentException("Unsupported geometry type: ${request.type}")
        }

    private fun createPoint(request: GeometryRequest): Point {
        require(request.coordinates.size == 1) { "Point requires exactly one coordinate" }
        val coord = request.coordinates.first().toJtsCoordinate()
        return geometryFactory.createPoint(coord)
    }

    private fun createLineString(request: GeometryRequest): LineString {
        require(request.coordinates.size >= 2) { "LineString requires at least two coordinates" }
        val coords = request.coordinates.map { it.toJtsCoordinate() }.toTypedArray()
        return geometryFactory.createLineString(coords)
    }

    private fun createMultiPoint(request: GeometryRequest): MultiPoint {
        require(request.coordinates.isNotEmpty()) { "MultiPoint requires at least one coordinate" }
        val coords = request.coordinates.map { it.toJtsCoordinate() }.toTypedArray()
        return geometryFactory.createMultiPoint(coords)
    }

    private fun createMultiLineString(request: GeometryRequest): MultiLineString {
        require(request.lines != null && request.lines.isNotEmpty()) { "MultiLineString requires at least one line" }
        val lines =
            request.lines.map { coords ->
                createLineString(GeometryRequest(type = "linestring", coordinates = coords))
            }
        return geometryFactory.createMultiLineString(lines.toTypedArray())
    }

    private fun createPolygon(request: GeometryRequest): Polygon {
        require(request.coordinates.size >= 3) { "Polygon requires at least three coordinates" }
        val shell = createLinearRing(request.coordinates)

        val holes = request.holes?.map { createLinearRing(it) }?.toTypedArray() ?: emptyArray()
        return geometryFactory.createPolygon(shell, holes)
    }

    private fun createCircle(request: GeometryRequest): Polygon {
        require(request.radius != null) { "Circle requires a radius" }
        require(request.coordinates.size == 1) { "Circle requires exactly one coordinate for center" }

        val center = request.coordinates.first().toJtsCoordinate()
        val shapeFactory = GeometricShapeFactory(geometryFactory)
        shapeFactory.setNumPoints(32)
        shapeFactory.setCentre(center)

        // Convert radius from meters to degrees (approximate)
        val radiusDegrees = request.radius / (111320 * cos(Math.toRadians(center.y)))
        shapeFactory.setSize(radiusDegrees * 2)

        return shapeFactory.createCircle()
    }

    private fun createMultiPolygon(request: GeometryRequest): MultiPolygon {
        require(request.polygons != null && request.polygons.isNotEmpty()) { "MultiPolygon requires at least one polygon" }

        val polygons =
            request.polygons
                .map { polygonCoords ->
                    val coords = polygonCoords.map { it.toJtsCoordinate() }.toMutableList()
                    if (!coords.first().equals2D(coords.last())) {
                        coords.add(coords.first())
                    }
                    geometryFactory.createPolygon(coords.toTypedArray())
                }.toTypedArray()

        return geometryFactory.createMultiPolygon(polygons)
    }

    private fun createRectangle(request: GeometryRequest): Polygon {
        require(request.coordinates.size == 2) { "Rectangle requires exactly two coordinates (min and max points)" }
        val (min, max) = request.coordinates
        val coords =
            arrayOf(
                min.toJtsCoordinate(),
                org.locationtech.jts.geom
                    .Coordinate(max[1], min[0]),
                max.toJtsCoordinate(),
                org.locationtech.jts.geom
                    .Coordinate(min[1], max[0]),
                min.toJtsCoordinate(),
            )
        return geometryFactory.createPolygon(coords)
    }

    private fun createGeometryCollection(request: GeometryRequest): GeometryCollection {
        val geometries = mutableListOf<Geometry>()

        request.coordinates.takeIf { it.isNotEmpty() }?.let {
            geometries.add(createMultiPoint(request))
        }

        request.lines?.takeIf { it.isNotEmpty() }?.let {
            geometries.add(createMultiLineString(request))
        }

        request.polygons?.takeIf { it.isNotEmpty() }?.let {
            geometries.add(createMultiPolygon(request))
        }

        require(geometries.isNotEmpty()) { "GeometryCollection requires at least one geometry" }
        return geometryFactory.createGeometryCollection(geometries.toTypedArray())
    }

    private fun createLinearRing(coordinates: List<Array<Double>>): LinearRing {
        val coords = coordinates.map { it.toJtsCoordinate() }.toMutableList()
        if (!coords.first().equals2D(coords.last())) {
            coords.add(coords.first())
        }
        return geometryFactory.createLinearRing(coords.toTypedArray())
    }

    fun convertToGeoJson(geometry: Geometry?): GeoJsonObject? {
        if (geometry == null) return null

        return when (geometry) {
            is Point -> {
                org.geojson.Point(LngLatAlt(geometry.coordinate.y, geometry.coordinate.x))
            }
            is LineString -> {
                org.geojson.LineString().apply {
                    coordinates =
                        geometry.coordinates.map { coord ->
                            LngLatAlt(coord.y, coord.x)
                        }
                }
            }
            is Polygon -> {
                org.geojson.Polygon().apply {
                    // Add exterior ring first
                    val exterior =
                        geometry.exteriorRing.coordinates.map { coordinate ->
                            LngLatAlt(coordinate.y, coordinate.x)
                        }
                    coordinates = mutableListOf(exterior)

                    // Add interior rings
                    for (i in 0 until geometry.numInteriorRing) {
                        coordinates.add(
                            geometry.getInteriorRingN(i).coordinates.map { coordinate ->
                                LngLatAlt(coordinate.y, coordinate.x)
                            },
                        )
                    }
                }
            }
            is MultiPolygon -> {
                org.geojson.MultiPolygon().apply {
                    coordinates =
                        mutableListOf<List<List<LngLatAlt>>>().apply {
                            for (i in 0 until geometry.numGeometries) {
                                val polygon = geometry.getGeometryN(i) as Polygon
                                val polygonCoordinates = mutableListOf<List<LngLatAlt>>()

                                // Add exterior ring
                                polygonCoordinates.add(
                                    polygon.exteriorRing.coordinates.map { coordinate ->
                                        LngLatAlt(coordinate.y, coordinate.x)
                                    },
                                )

                                // Add interior rings
                                for (j in 0 until polygon.numInteriorRing) {
                                    polygonCoordinates.add(
                                        polygon.getInteriorRingN(j).coordinates.map { coordinate ->
                                            LngLatAlt(coordinate.y, coordinate.x)
                                        },
                                    )
                                }
                                add(polygonCoordinates)
                            }
                        }
                }
            }
            else -> {
                // For other geometry types, use the default conversion
                val geoJsonString = geoJsonWriter.write(geometry)
                ObjectMapper().readValue(geoJsonString, GeoJsonObject::class.java)
            }
        }
    }
}
