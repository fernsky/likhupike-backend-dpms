package np.sthaniya.dpis.profile.location.controller

import java.math.BigDecimal
import java.util.*
import np.sthaniya.dpis.auth.controller.base.BaseRestDocsTest
import np.sthaniya.dpis.profile.location.dto.MunicipalityCreateRequest
import np.sthaniya.dpis.profile.location.dto.WardCreateRequest
import np.sthaniya.dpis.profile.location.dto.WardUpdateRequest
import np.sthaniya.dpis.profile.location.service.ProfileMunicipalityService
import np.sthaniya.dpis.profile.location.service.ProfileWardService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ProfileWardControllerTest : BaseRestDocsTest() {

    @Autowired private lateinit var wardService: ProfileWardService

    @Autowired private lateinit var municipalityService: ProfileMunicipalityService

    private lateinit var municipalityId: UUID

    @BeforeEach
    fun setup() {
        // Create a test municipality if it doesn't exist
        val municipality =
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
        municipalityId = municipality.id!!
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should create a ward successfully`() {
        val request =
                WardCreateRequest(
                        number = 1,
                        area = BigDecimal("10.5"),
                        formingLocalBodies = arrayOf("Former VDC 1", "Former VDC 2"),
                        formingConstituentWards = arrayOf("Ward 1", "Ward 2")
                )

        mockMvc.perform(
                        post("/api/v1/profile/location/wards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.number").value(1))
                .andDo(
                        document(
                                "ward-create",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("number")
                                                .description("Ward number (must be positive)"),
                                        fieldWithPath("area")
                                                .description("Ward area in square kilometers"),
                                        fieldWithPath("formingLocalBodies")
                                                .description(
                                                        "Array of former local bodies that formed this ward"
                                                ),
                                        fieldWithPath("formingConstituentWards")
                                                .description(
                                                        "Array of constituent wards that formed this ward"
                                                )
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("Response data object"),
                                        fieldWithPath("data.id")
                                                .description("Unique identifier of the ward"),
                                        fieldWithPath("data.number").description("Ward number"),
                                        fieldWithPath("data.area")
                                                .description("Ward area in square kilometers"),
                                        fieldWithPath("data.formingLocalBodies")
                                                .description(
                                                        "Former local bodies that formed this ward"
                                                ),
                                        fieldWithPath("data.formingConstituentWards")
                                                .description(
                                                        "Constituent wards that formed this ward"
                                                ),
                                        fieldWithPath("data.settlementCount")
                                                .description(
                                                        "Number of settlement areas in this ward"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should get all wards with public access`() {
        // Create test ward first
        createTestWard()

        mockMvc.perform(
                        get("/api/v1/profile/location/wards")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").exists())
                .andDo(
                        document(
                                "ward-get-all",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("List of wards"),
                                        fieldWithPath("data[].id")
                                                .description("Unique identifier of the ward"),
                                        fieldWithPath("data[].number").description("Ward number"),
                                        fieldWithPath("data[].area")
                                                .description("Ward area in square kilometers"),
                                        fieldWithPath("data[].formingLocalBodies")
                                                .description(
                                                        "Former local bodies that formed this ward"
                                                ),
                                        fieldWithPath("data[].formingConstituentWards")
                                                .description(
                                                        "Constituent wards that formed this ward"
                                                ),
                                        fieldWithPath("data[].settlementCount")
                                                .description(
                                                        "Number of settlement areas in this ward"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should get ward by id with public access`() {
        // Create test ward first
        val wardId = createTestWard().id!!

        mockMvc.perform(
                        get("/api/v1/profile/location/wards/{wardId}", wardId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(wardId.toString()))
                .andDo(
                        document(
                                "ward-get-by-id",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("wardId")
                                                .description("The ID of the ward to retrieve")
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("Ward data"),
                                        fieldWithPath("data.id")
                                                .description("Unique identifier of the ward"),
                                        fieldWithPath("data.number").description("Ward number"),
                                        fieldWithPath("data.area")
                                                .description("Ward area in square kilometers"),
                                        fieldWithPath("data.formingLocalBodies")
                                                .description(
                                                        "Former local bodies that formed this ward"
                                                ),
                                        fieldWithPath("data.formingConstituentWards")
                                                .description(
                                                        "Constituent wards that formed this ward"
                                                ),
                                        fieldWithPath("data.settlementCount")
                                                .description(
                                                        "Number of settlement areas in this ward"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should get ward by number with public access`() {
        // Create test ward first
        createTestWard()

        mockMvc.perform(
                        get("/api/v1/profile/location/wards/number/{number}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.number").value(1))
                .andDo(
                        document(
                                "ward-get-by-number",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("number")
                                                .description("The ward number to retrieve")
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("Ward data"),
                                        fieldWithPath("data.id")
                                                .description("Unique identifier of the ward"),
                                        fieldWithPath("data.number").description("Ward number"),
                                        fieldWithPath("data.area")
                                                .description("Ward area in square kilometers"),
                                        fieldWithPath("data.formingLocalBodies")
                                                .description(
                                                        "Former local bodies that formed this ward"
                                                ),
                                        fieldWithPath("data.formingConstituentWards")
                                                .description(
                                                        "Constituent wards that formed this ward"
                                                ),
                                        fieldWithPath("data.settlementCount")
                                                .description(
                                                        "Number of settlement areas in this ward"
                                                )
                                )
                        )
                )
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should update a ward`() {
        // Create test ward first
        val wardId = createTestWard().id!!

        val request =
                WardUpdateRequest(
                        area = BigDecimal("15.0"),
                        formingLocalBodies = arrayOf("Updated VDC 1", "Updated VDC 2"),
                        formingConstituentWards = arrayOf("Updated Ward 1", "Updated Ward 2")
                )

        mockMvc.perform(
                        put("/api/v1/profile/location/wards/{wardId}", wardId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.area").value(15.0))
                .andDo(
                        document(
                                "ward-update",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("wardId")
                                                .description("The ID of the ward to update")
                                ),
                                requestFields(
                                        fieldWithPath("area")
                                                .description(
                                                        "Updated ward area in square kilometers"
                                                ),
                                        fieldWithPath("formingLocalBodies")
                                                .description(
                                                        "Updated array of former local bodies"
                                                ),
                                        fieldWithPath("formingConstituentWards")
                                                .description("Updated array of constituent wards")
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("Updated ward data"),
                                        fieldWithPath("data.id")
                                                .description("Unique identifier of the ward"),
                                        fieldWithPath("data.number")
                                                .description("Ward number (unchanged)"),
                                        fieldWithPath("data.area")
                                                .description(
                                                        "Updated ward area in square kilometers"
                                                ),
                                        fieldWithPath("data.formingLocalBodies")
                                                .description("Updated former local bodies"),
                                        fieldWithPath("data.formingConstituentWards")
                                                .description("Updated constituent wards"),
                                        fieldWithPath("data.settlementCount")
                                                .description(
                                                        "Number of settlement areas in this ward"
                                                )
                                )
                        )
                )
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should delete a ward`() {
        // Create test ward first
        val wardId = createTestWard().id!!

        mockMvc.perform(
                        delete("/api/v1/profile/location/wards/{wardId}", wardId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "ward-delete",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("wardId")
                                                .description("The ID of the ward to delete")
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message")
                                                .description(
                                                        "Success message indicating ward was deleted"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should return 401 when creating ward without authentication`() {
        val request =
                WardCreateRequest(
                        number = 1,
                        area = BigDecimal("10.5"),
                        formingLocalBodies = arrayOf("Former VDC 1", "Former VDC 2"),
                        formingConstituentWards = arrayOf("Ward 1", "Ward 2")
                )

        mockMvc.perform(
                        post("/api/v1/profile/location/wards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(authorities = ["WRONG_PERMISSION"])
    fun `should return 403 when creating ward without proper permission`() {
        val request =
                WardCreateRequest(
                        number = 1,
                        area = BigDecimal("10.5"),
                        formingLocalBodies = arrayOf("Former VDC 1", "Former VDC 2"),
                        formingConstituentWards = arrayOf("Ward 1", "Ward 2")
                )

        mockMvc.perform(
                        post("/api/v1/profile/location/wards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden)
    }

    // Helper method to create a test ward
    private fun createTestWard() =
            wardService.createWard(
                    WardCreateRequest(
                            number = 1,
                            area = BigDecimal("10.5"),
                            formingLocalBodies = arrayOf("Former VDC 1", "Former VDC 2"),
                            formingConstituentWards = arrayOf("Ward 1", "Ward 2")
                    )
            )
}
