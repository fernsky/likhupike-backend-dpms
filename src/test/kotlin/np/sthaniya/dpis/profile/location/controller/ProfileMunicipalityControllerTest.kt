package np.sthaniya.dpis.profile.location.controller

import java.math.BigDecimal
import np.sthaniya.dpis.auth.controller.base.BaseRestDocsTest
import np.sthaniya.dpis.profile.location.dto.MunicipalityCreateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityGeoLocationUpdateRequest
import np.sthaniya.dpis.profile.location.dto.MunicipalityUpdateRequest
import np.sthaniya.dpis.profile.location.service.ProfileMunicipalityService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ProfileMunicipalityControllerTest : BaseRestDocsTest() {

    @Autowired private lateinit var municipalityService: ProfileMunicipalityService

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should create municipality successfully`() {
        val request =
                MunicipalityCreateRequest(
                        name = "Test Municipality",
                        province = "Test Province",
                        district = "Test District",
                        rightmostLatitude = BigDecimal("85.4"),
                        leftmostLatitude = BigDecimal("85.1"),
                        bottommostLongitude = BigDecimal("27.1"),
                        topmostLongitude = BigDecimal("27.4"),
                        lowestAltitude = BigDecimal("1000"),
                        highestAltitude = BigDecimal("2000"),
                        areaInSquareKilometers = BigDecimal("150")
                )

        mockMvc.perform(
                        post("/api/v1/profile/location/municipality")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name").value("Test Municipality"))
                .andDo(
                        document(
                                "municipality-create",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name").description("Municipality name"),
                                        fieldWithPath("province").description("Province name"),
                                        fieldWithPath("district").description("District name"),
                                        fieldWithPath("rightmostLatitude")
                                                .description("Rightmost latitude coordinate"),
                                        fieldWithPath("leftmostLatitude")
                                                .description("Leftmost latitude coordinate"),
                                        fieldWithPath("bottommostLongitude")
                                                .description("Bottommost longitude coordinate"),
                                        fieldWithPath("topmostLongitude")
                                                .description("Topmost longitude coordinate"),
                                        fieldWithPath("lowestAltitude")
                                                .optional()
                                                .description(
                                                        "Lowest altitude in meters (optional)"
                                                ),
                                        fieldWithPath("highestAltitude")
                                                .optional()
                                                .description(
                                                        "Highest altitude in meters (optional)"
                                                ),
                                        fieldWithPath("areaInSquareKilometers")
                                                .description("Total area in square kilometers")
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("Municipality data"),
                                        fieldWithPath("data.id")
                                                .description(
                                                        "Unique identifier of the municipality"
                                                ),
                                        fieldWithPath("data.name").description("Municipality name"),
                                        fieldWithPath("data.province").description("Province name"),
                                        fieldWithPath("data.district").description("District name"),
                                        fieldWithPath("data.rightmostLatitude")
                                                .description("Rightmost latitude coordinate"),
                                        fieldWithPath("data.leftmostLatitude")
                                                .description("Leftmost latitude coordinate"),
                                        fieldWithPath("data.bottommostLongitude")
                                                .description("Bottommost longitude coordinate"),
                                        fieldWithPath("data.topmostLongitude")
                                                .description("Topmost longitude coordinate"),
                                        fieldWithPath("data.lowestAltitude")
                                                .description("Lowest altitude in meters"),
                                        fieldWithPath("data.highestAltitude")
                                                .description("Highest altitude in meters"),
                                        fieldWithPath("data.areaInSquareKilometers")
                                                .description("Total area in square kilometers"),
                                        fieldWithPath("data.wardsCount")
                                                .description("Number of wards in the municipality")
                                )
                        )
                )
    }

    @Test
    fun `should get municipality with public access`() {
        // Create a municipality first
        createTestMunicipality()

        mockMvc.perform(
                        get("/api/v1/profile/location/municipality")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name").value("Test Municipality"))
                .andDo(
                        document(
                                "municipality-get",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("Municipality data"),
                                        fieldWithPath("data.id")
                                                .description(
                                                        "Unique identifier of the municipality"
                                                ),
                                        fieldWithPath("data.name").description("Municipality name"),
                                        fieldWithPath("data.province").description("Province name"),
                                        fieldWithPath("data.district").description("District name"),
                                        fieldWithPath("data.rightmostLatitude")
                                                .description("Rightmost latitude coordinate"),
                                        fieldWithPath("data.leftmostLatitude")
                                                .description("Leftmost latitude coordinate"),
                                        fieldWithPath("data.bottommostLongitude")
                                                .description("Bottommost longitude coordinate"),
                                        fieldWithPath("data.topmostLongitude")
                                                .description("Topmost longitude coordinate"),
                                        fieldWithPath("data.lowestAltitude")
                                                .description("Lowest altitude in meters"),
                                        fieldWithPath("data.highestAltitude")
                                                .description("Highest altitude in meters"),
                                        fieldWithPath("data.areaInSquareKilometers")
                                                .description("Total area in square kilometers"),
                                        fieldWithPath("data.wardsCount")
                                                .description("Number of wards in the municipality")
                                )
                        )
                )
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should update municipality info`() {
        // Create a municipality first
        createTestMunicipality()

        val request =
                MunicipalityUpdateRequest(
                        name = "Updated Municipality",
                        province = "Updated Province",
                        district = "Updated District"
                )

        mockMvc.perform(
                        put("/api/v1/profile/location/municipality/info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Updated Municipality"))
                .andExpect(jsonPath("$.data.province").value("Updated Province"))
                .andDo(
                        document(
                                "municipality-update-info",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name")
                                                .description("Updated municipality name"),
                                        fieldWithPath("province")
                                                .description("Updated province name"),
                                        fieldWithPath("district")
                                                .description("Updated district name")
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data")
                                                .description("Updated municipality data"),
                                        fieldWithPath("data.id")
                                                .description(
                                                        "Unique identifier of the municipality"
                                                ),
                                        fieldWithPath("data.name")
                                                .description("Updated municipality name"),
                                        fieldWithPath("data.province")
                                                .description("Updated province name"),
                                        fieldWithPath("data.district")
                                                .description("Updated district name"),
                                        fieldWithPath("data.rightmostLatitude")
                                                .description(
                                                        "Rightmost latitude coordinate (unchanged)"
                                                ),
                                        fieldWithPath("data.leftmostLatitude")
                                                .description(
                                                        "Leftmost latitude coordinate (unchanged)"
                                                ),
                                        fieldWithPath("data.bottommostLongitude")
                                                .description(
                                                        "Bottommost longitude coordinate (unchanged)"
                                                ),
                                        fieldWithPath("data.topmostLongitude")
                                                .description(
                                                        "Topmost longitude coordinate (unchanged)"
                                                ),
                                        fieldWithPath("data.lowestAltitude")
                                                .description(
                                                        "Lowest altitude in meters (unchanged)"
                                                ),
                                        fieldWithPath("data.highestAltitude")
                                                .description(
                                                        "Highest altitude in meters (unchanged)"
                                                ),
                                        fieldWithPath("data.areaInSquareKilometers")
                                                .description(
                                                        "Total area in square kilometers (unchanged)"
                                                ),
                                        fieldWithPath("data.wardsCount")
                                                .description("Number of wards in the municipality")
                                )
                        )
                )
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should update municipality geo-location`() {
        // Create a municipality first
        createTestMunicipality()

        val request =
                MunicipalityGeoLocationUpdateRequest(
                        rightmostLatitude = BigDecimal("85.5"),
                        leftmostLatitude = BigDecimal("85.0"),
                        bottommostLongitude = BigDecimal("27.0"),
                        topmostLongitude = BigDecimal("27.5"),
                        lowestAltitude = BigDecimal("900"),
                        highestAltitude = BigDecimal("2100"),
                        areaInSquareKilometers = BigDecimal("160")
                )

        mockMvc.perform(
                        put("/api/v1/profile/location/municipality/geo-location")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.areaInSquareKilometers").value(160))
                .andDo(
                        document(
                                "municipality-update-geo-location",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("rightmostLatitude")
                                                .description(
                                                        "Updated rightmost latitude coordinate"
                                                ),
                                        fieldWithPath("leftmostLatitude")
                                                .description(
                                                        "Updated leftmost latitude coordinate"
                                                ),
                                        fieldWithPath("bottommostLongitude")
                                                .description(
                                                        "Updated bottommost longitude coordinate"
                                                ),
                                        fieldWithPath("topmostLongitude")
                                                .description(
                                                        "Updated topmost longitude coordinate"
                                                ),
                                        fieldWithPath("lowestAltitude")
                                                .optional()
                                                .description("Updated lowest altitude in meters"),
                                        fieldWithPath("highestAltitude")
                                                .optional()
                                                .description("Updated highest altitude in meters"),
                                        fieldWithPath("areaInSquareKilometers")
                                                .description(
                                                        "Updated total area in square kilometers"
                                                )
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data")
                                                .description("Updated municipality data"),
                                        fieldWithPath("data.id")
                                                .description(
                                                        "Unique identifier of the municipality"
                                                ),
                                        fieldWithPath("data.name")
                                                .description("Municipality name (unchanged)"),
                                        fieldWithPath("data.province")
                                                .description("Province name (unchanged)"),
                                        fieldWithPath("data.district")
                                                .description("District name (unchanged)"),
                                        fieldWithPath("data.rightmostLatitude")
                                                .description(
                                                        "Updated rightmost latitude coordinate"
                                                ),
                                        fieldWithPath("data.leftmostLatitude")
                                                .description(
                                                        "Updated leftmost latitude coordinate"
                                                ),
                                        fieldWithPath("data.bottommostLongitude")
                                                .description(
                                                        "Updated bottommost longitude coordinate"
                                                ),
                                        fieldWithPath("data.topmostLongitude")
                                                .description(
                                                        "Updated topmost longitude coordinate"
                                                ),
                                        fieldWithPath("data.lowestAltitude")
                                                .description("Updated lowest altitude in meters"),
                                        fieldWithPath("data.highestAltitude")
                                                .description("Updated highest altitude in meters"),
                                        fieldWithPath("data.areaInSquareKilometers")
                                                .description(
                                                        "Updated total area in square kilometers"
                                                ),
                                        fieldWithPath("data.wardsCount")
                                                .description("Number of wards in the municipality")
                                )
                        )
                )
    }

    @Test
    fun `should return 401 when updating municipality info without authentication`() {
        val request =
                MunicipalityUpdateRequest(
                        name = "Updated Municipality",
                        province = "Updated Province",
                        district = "Updated District"
                )

        mockMvc.perform(
                        put("/api/v1/profile/location/municipality/info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(authorities = ["WRONG_PERMISSION"])
    fun `should return 403 when updating municipality info without proper permission`() {
        val request =
                MunicipalityUpdateRequest(
                        name = "Updated Municipality",
                        province = "Updated Province",
                        district = "Updated District"
                )

        mockMvc.perform(
                        put("/api/v1/profile/location/municipality/info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden)
    }

    // Helper method to create a test municipality
    private fun createTestMunicipality() =
            municipalityService.getOrCreateMunicipality(
                    MunicipalityCreateRequest(
                            name = "Test Municipality",
                            province = "Test Province",
                            district = "Test District",
                            rightmostLatitude = BigDecimal("85.4"),
                            leftmostLatitude = BigDecimal("85.1"),
                            bottommostLongitude = BigDecimal("27.1"),
                            topmostLongitude = BigDecimal("27.4"),
                            lowestAltitude = BigDecimal("1000"),
                            highestAltitude = BigDecimal("2000"),
                            areaInSquareKilometers = BigDecimal("150")
                    )
            )
}
