package np.sthaniya.dpis.profile.demographics.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers demographics endpoints to RouteRegistry for security filtering.
 * All endpoints require JWT authentication.
 *
 * Endpoint patterns follow RESTful API conventions:
 * - Base path: /api/v1/demographics
 * - Dynamic segments use [^/]+ regex to match UUIDs
 * - All methods require corresponding permissions
 */
@Configuration
class DemographicsRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Ward-wise religion population endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population/ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population/religion/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population/summary/by-religion", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-religion-population/summary/by-ward", HttpMethod.GET, true)

        // Ward-wise absentee educational level endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level/by-educational-level/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level/summary/by-educational-level", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-absentee-educational-level/summary/by-ward", HttpMethod.GET, true)

        // Ward-wise mother tongue population endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/by-language/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/summary/by-language", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-mother-tongue-population/summary/by-ward", HttpMethod.GET, true)

        // Ward-wise househead gender endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/by-gender/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/summary/by-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-househead-gender/summary/by-ward", HttpMethod.GET, true)

        // Ward-wise demographic summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-demographic-summary/by-ward/[^/]+", HttpMethod.GET, true)

        // Ward-wise caste population endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/by-caste/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/summary/by-caste", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-wise-caste-population/summary/by-ward", HttpMethod.GET, true)

        // Ward time series population endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population/by-year/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population/summary/latest-by-ward", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-time-series-population/summary/by-year", HttpMethod.GET, true)

        // Ward age-wise population endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/by-age-group/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/by-gender/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/summary/by-age-group", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/summary/by-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-population/summary/detailed", HttpMethod.GET, true)

        // Ward age-wise marital status endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/by-age-group/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/by-marital-status/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/summary/by-marital-status", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-wise-marital-status/summary/by-age-group", HttpMethod.GET, true)

        // Demographic summary endpoints
        routeRegistry.register("/api/v1/profile/demographics/summary", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/summary", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/summary/field", HttpMethod.PUT)

        // Ward age-gender-wise married age endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-age-group/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/by-gender/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/summary/by-age-group-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-married-age/summary/by-ward", HttpMethod.GET, true)

        // Ward age-gender-wise absentee endpoints
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee", HttpMethod.POST)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/[^/]+", HttpMethod.PUT)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/[^/]+", HttpMethod.DELETE)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-ward/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-age-group/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/by-gender/[^/]+", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/summary/by-age-gender", HttpMethod.GET, true)
        routeRegistry.register("/api/v1/profile/demographics/ward-age-gender-wise-absentee/summary/by-ward", HttpMethod.GET, true)
    }
}
