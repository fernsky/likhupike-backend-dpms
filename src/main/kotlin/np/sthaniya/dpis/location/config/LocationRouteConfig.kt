package np.sthaniya.dpis.location.config

import np.sthaniya.dpis.common.config.RouteRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

/**
 * Registers location-related endpoints in RouteRegistry for security filtering.
 * 
 * Implementation details:
 * - Read-only endpoints (GET) are public to allow location information access
 * - Write operations (POST, PUT) require administrative privileges
 * - Search endpoints support public access with pagination
 * - Using regex patterns instead of path variables for proper Spring security matching
 * 
 * Base paths:
 * - /api/v1/provinces/
 * - /api/v1/districts/
 * - /api/v1/municipalities/
 * - /api/v1/wards/
 */
@Configuration
class LocationRouteConfig(
    routeRegistry: RouteRegistry,
) {
    init {
        // Province routes
        routeRegistry.register("/api/v1/provinces", HttpMethod.GET, isPublic = true) // getAllProvinces
        routeRegistry.register("/api/v1/provinces/[^/]+", HttpMethod.GET, isPublic = true) // getProvinceDetail
        routeRegistry.register("/api/v1/provinces/search", HttpMethod.GET, isPublic = true) // searchProvinces
        routeRegistry.register("/api/v1/provinces/large", HttpMethod.GET, isPublic = true) // findLargeProvinces
        routeRegistry.register("/api/v1/provinces", HttpMethod.POST, isPublic = false) // createProvince
        routeRegistry.register("/api/v1/provinces/[^/]+", HttpMethod.PUT, isPublic = false) // updateProvince

        // District routes
        // routeRegistry.register("/api/v1/districts", HttpMethod.GET, isPublic = true) // getAllDistricts
        routeRegistry.register("/api/v1/districts/[^/]+", HttpMethod.GET, isPublic = true) // getDistrictDetail
        routeRegistry.register("/api/v1/districts/search", HttpMethod.GET, isPublic = true) // searchDistricts
        routeRegistry.register("/api/v1/districts/by-province/[^/]+", HttpMethod.GET, isPublic = true) // getDistrictsByProvince
        routeRegistry.register("/api/v1/districts", HttpMethod.POST, isPublic = false) // createDistrict
        routeRegistry.register("/api/v1/districts/[^/]+", HttpMethod.PUT, isPublic = false) // updateDistrict

        // Municipality routes
        // routeRegistry.register("/api/v1/municipalities", HttpMethod.GET, isPublic = true) // getAllMunicipalities
        routeRegistry.register("/api/v1/municipalities/[^/]+", HttpMethod.GET, isPublic = true) // getMunicipalityDetail
        routeRegistry.register("/api/v1/municipalities/search", HttpMethod.GET, isPublic = true) // searchMunicipalities
        routeRegistry.register("/api/v1/municipalities/by-district/[^/]+", HttpMethod.GET, isPublic = true) // getMunicipalitiesByDistrict
        routeRegistry.register("/api/v1/municipalities/by-type/[^/]+", HttpMethod.GET, isPublic = true) // getMunicipalitiesByType
        routeRegistry.register("/api/v1/municipalities/nearby", HttpMethod.GET, isPublic = true) // findNearbyMunicipalities
        routeRegistry.register("/api/v1/municipalities", HttpMethod.POST, isPublic = false) // createMunicipality
        routeRegistry.register("/api/v1/municipalities/[^/]+", HttpMethod.PUT, isPublic = false) // updateMunicipality

        // Ward routes
        routeRegistry.register("/api/v1/wards/by-municipality/[^/]+", HttpMethod.GET, isPublic = true) // getWardsByMunicipality
        routeRegistry.register("/api/v1/wards/[^/]+/\\d+", HttpMethod.GET, isPublic = true) // getWardDetail (municipality/wardNumber)
        routeRegistry.register("/api/v1/wards/search", HttpMethod.GET, isPublic = true) // searchWards
        routeRegistry.register("/api/v1/wards/nearby", HttpMethod.GET, isPublic = true) // findNearbyWards
        routeRegistry.register("/api/v1/wards", HttpMethod.POST, isPublic = false) // createWard
        routeRegistry.register("/api/v1/wards/[^/]+/\\d+", HttpMethod.PUT, isPublic = false) // updateWard (municipality/wardNumber)
    }
}
