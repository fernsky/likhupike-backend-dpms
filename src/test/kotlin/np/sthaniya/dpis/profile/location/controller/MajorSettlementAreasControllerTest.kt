package np.sthaniya.dpis.profile.location.controller

import com.fasterxml.jackson.databind.ObjectMapper
import java.math.BigDecimal
import java.util.*
import np.sthaniya.dpis.auth.controller.base.BaseRestDocsTest
import np.sthaniya.dpis.profile.location.dto.*
import np.sthaniya.dpis.profile.location.service.MajorSettlementAreasService
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

class MajorSettlementAreasControllerTest : BaseRestDocsTest() {

    @Autowired private lateinit var settlementService: MajorSettlementAreasService

    @Autowired private lateinit var wardService: ProfileWardService

    @Autowired private lateinit var municipalityService: ProfileMunicipalityService

    @Autowired private lateinit var objectMapper: ObjectMapper

    private lateinit var testWardId: UUID

    @BeforeEach
    fun setup() {
        // Create a test municipality first
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

        // Then create a test ward
        val ward =
                wardService.createWard(
                        WardCreateRequest(
                                number = 1,
                                area = BigDecimal("10.5"),
                                formingLocalBodies = arrayOf("Former VDC 1", "Former VDC 2"),
                                formingConstituentWards = arrayOf("Ward 1", "Ward 2")
                        )
                )
        testWardId = ward.id!!
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should create a settlement area successfully`() {
        val request =
                MajorSettlementAreasCreateRequest(name = "Test Settlement", wardId = testWardId)

        mockMvc.perform(
                        post("/api/v1/profile/location/settlements")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name").value("Test Settlement"))
                .andExpect(jsonPath("$.data.wardId").value(testWardId.toString()))
                .andDo(
                        document(
                                "settlement-create",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name").description("Settlement area name"),
                                        fieldWithPath("wardId")
                                                .description(
                                                        "UUID of the ward this settlement belongs to"
                                                )
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("Settlement data"),
                                        fieldWithPath("data.id")
                                                .description(
                                                        "Unique identifier of the settlement area"
                                                ),
                                        fieldWithPath("data.name")
                                                .description("Settlement area name"),
                                        fieldWithPath("data.wardId")
                                                .description(
                                                        "UUID of the ward this settlement belongs to"
                                                ),
                                        fieldWithPath("data.wardNumber")
                                                .description(
                                                        "Ward number this settlement belongs to"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should get all settlement areas with public access`() {
        // Create a test settlement first
        createTestSettlement()

        mockMvc.perform(
                        get("/api/v1/profile/location/settlements")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[0].name").value("Test Settlement"))
                .andDo(
                        document(
                                "settlement-get-all",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data")
                                                .description("List of settlement areas"),
                                        fieldWithPath("data[].id")
                                                .description(
                                                        "Unique identifier of the settlement area"
                                                ),
                                        fieldWithPath("data[].name")
                                                .description("Settlement area name"),
                                        fieldWithPath("data[].wardId")
                                                .description(
                                                        "UUID of the ward this settlement belongs to"
                                                ),
                                        fieldWithPath("data[].wardNumber")
                                                .description(
                                                        "Ward number this settlement belongs to"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should get settlement area by id with public access`() {
        // Create a test settlement first
        val settlementId = createTestSettlement().id!!

        mockMvc.perform(
                        get("/api/v1/profile/location/settlements/{id}", settlementId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(settlementId.toString()))
                .andExpect(jsonPath("$.data.name").value("Test Settlement"))
                .andDo(
                        document(
                                "settlement-get-by-id",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id")
                                                .description(
                                                        "The ID of the settlement area to retrieve"
                                                )
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data").description("Settlement area data"),
                                        fieldWithPath("data.id")
                                                .description(
                                                        "Unique identifier of the settlement area"
                                                ),
                                        fieldWithPath("data.name")
                                                .description("Settlement area name"),
                                        fieldWithPath("data.wardId")
                                                .description(
                                                        "UUID of the ward this settlement belongs to"
                                                ),
                                        fieldWithPath("data.wardNumber")
                                                .description(
                                                        "Ward number this settlement belongs to"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should get settlement areas by ward with public access`() {
        // Create a test settlement first
        createTestSettlement()

        mockMvc.perform(
                        get("/api/v1/profile/location/settlements/ward/{wardId}", testWardId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].wardId").value(testWardId.toString()))
                .andDo(
                        document(
                                "settlement-get-by-ward",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("wardId")
                                                .description(
                                                        "The ID of the ward to retrieve settlements for"
                                                )
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data")
                                                .description(
                                                        "List of settlement areas in the specified ward"
                                                ),
                                        fieldWithPath("data[].id")
                                                .description(
                                                        "Unique identifier of the settlement area"
                                                ),
                                        fieldWithPath("data[].name")
                                                .description("Settlement area name"),
                                        fieldWithPath("data[].wardId")
                                                .description(
                                                        "UUID of the ward this settlement belongs to"
                                                ),
                                        fieldWithPath("data[].wardNumber")
                                                .description(
                                                        "Ward number this settlement belongs to"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should search settlement areas by name with public access`() {
        // Create a test settlement first
        createTestSettlement()

        mockMvc.perform(
                        get("/api/v1/profile/location/settlements/search")
                                .param("name", "Test")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Test Settlement"))
                .andDo(
                        document(
                                "settlement-search-by-name",
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("name")
                                                .description(
                                                        "Search term for settlement area names"
                                                )
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data")
                                                .description(
                                                        "List of settlement areas matching the search term"
                                                ),
                                        fieldWithPath("data[].id")
                                                .description(
                                                        "Unique identifier of the settlement area"
                                                ),
                                        fieldWithPath("data[].name")
                                                .description("Settlement area name"),
                                        fieldWithPath("data[].wardId")
                                                .description(
                                                        "UUID of the ward this settlement belongs to"
                                                ),
                                        fieldWithPath("data[].wardNumber")
                                                .description(
                                                        "Ward number this settlement belongs to"
                                                )
                                )
                        )
                )
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should update a settlement area`() {
        // Create a test settlement first
        val settlementId = createTestSettlement().id!!

        val request = MajorSettlementAreasUpdateRequest(name = "Updated Settlement")

        mockMvc.perform(
                        put("/api/v1/profile/location/settlements/{id}", settlementId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Updated Settlement"))
                .andDo(
                        document(
                                "settlement-update",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id")
                                                .description(
                                                        "The ID of the settlement area to update"
                                                )
                                ),
                                requestFields(
                                        fieldWithPath("name")
                                                .description("Updated settlement area name")
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message").description("Success message"),
                                        fieldWithPath("data")
                                                .description("Updated settlement area data"),
                                        fieldWithPath("data.id")
                                                .description(
                                                        "Unique identifier of the settlement area"
                                                ),
                                        fieldWithPath("data.name")
                                                .description("Updated settlement area name"),
                                        fieldWithPath("data.wardId")
                                                .description(
                                                        "UUID of the ward this settlement belongs to (unchanged)"
                                                ),
                                        fieldWithPath("data.wardNumber")
                                                .description(
                                                        "Ward number this settlement belongs to (unchanged)"
                                                )
                                )
                        )
                )
    }

    @Test
    @WithMockUser(authorities = ["PERMISSION_MANAGE_PROFILE"])
    fun `should delete a settlement area`() {
        // Create a test settlement first
        val settlementId = createTestSettlement().id!!

        mockMvc.perform(
                        delete("/api/v1/profile/location/settlements/{id}", settlementId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.success").value(true))
                .andDo(
                        document(
                                "settlement-delete",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id")
                                                .description(
                                                        "The ID of the settlement area to delete"
                                                )
                                ),
                                responseFields(
                                        fieldWithPath("success")
                                                .description(
                                                        "Indicates if the request was successful"
                                                ),
                                        fieldWithPath("message")
                                                .description(
                                                        "Success message indicating settlement was deleted"
                                                )
                                )
                        )
                )
    }

    @Test
    fun `should return 401 when creating settlement without authentication`() {
        val request =
                MajorSettlementAreasCreateRequest(name = "Test Settlement", wardId = testWardId)

        mockMvc.perform(
                        post("/api/v1/profile/location/settlements")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(authorities = ["WRONG_PERMISSION"])
    fun `should return 403 when creating settlement without proper permission`() {
        val request =
                MajorSettlementAreasCreateRequest(name = "Test Settlement", wardId = testWardId)

        mockMvc.perform(
                        post("/api/v1/profile/location/settlements")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden)
    }

    // Helper method to create a test settlement
    private fun createTestSettlement() =
            settlementService.createSettlement(
                    MajorSettlementAreasCreateRequest(name = "Test Settlement", wardId = testWardId)
            )
}
