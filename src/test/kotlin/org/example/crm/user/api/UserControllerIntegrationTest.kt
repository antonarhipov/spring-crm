package org.example.crm.user.api

import org.example.crm.BaseIntegrationTest
import org.example.crm.shared.config.SecurityConfig
import org.example.crm.shared.error.EntityNotFoundException
import org.example.crm.user.api.dto.CreateUserRequest
import org.example.crm.user.api.dto.UpdateUserRequest
import org.example.crm.user.api.dto.UserResponse
import org.example.crm.user.domain.UserRole
import org.example.crm.user.domain.UserStatus
import org.example.crm.user.service.UserService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.springframework.context.annotation.Import
import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper

@WebMvcTest(UserController::class)
@Import(SecurityConfig::class)
class UserControllerIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userService: UserService

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ["ADMIN"])
    fun `should create new user`() {
        val request = CreateUserRequest(
            username = "newuser",
            email = "newuser@example.com",
            password = "password123",
            role = UserRole.SALES_REPRESENTATIVE
        )

        val response = UserResponse(
            id = 5L,
            username = request.username,
            email = request.email,
            role = request.role,
            status = UserStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        given(userService.createUser(any())).willReturn(response)

        mockMvc.perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.username").value(request.username))
            .andExpect(jsonPath("$.email").value(request.email))
            .andExpect(jsonPath("$.role").value(request.role.name))
            .andExpect(jsonPath("$.status").value(UserStatus.ACTIVE.name))
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ["ADMIN"])
    fun `should get existing user`() {
        val response = UserResponse(
            id = MANAGER_ID,
            username = MANAGER_USERNAME,
            email = "manager@example.com",
            role = UserRole.SALES_MANAGER,
            status = UserStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        given(userService.getUser(MANAGER_ID)).willReturn(response)

        mockMvc.perform(get("/api/v1/users/$MANAGER_ID"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(MANAGER_ID))
            .andExpect(jsonPath("$.username").value(MANAGER_USERNAME))
            .andExpect(jsonPath("$.role").value(UserRole.SALES_MANAGER.name))
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ["ADMIN"])
    fun `should update existing user`() {
        val request = UpdateUserRequest(
            email = "updated.manager@example.com",
            status = UserStatus.INACTIVE
        )

        val response = UserResponse(
            id = MANAGER_ID,
            username = MANAGER_USERNAME,
            email = request.email!!,
            role = UserRole.SALES_MANAGER,
            status = request.status!!,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        given(userService.updateUser(eq(MANAGER_ID), any())).willReturn(response)

        mockMvc.perform(
            put("/api/v1/users/$MANAGER_ID")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(MANAGER_ID))
            .andExpect(jsonPath("$.email").value(request.email))
            .andExpect(jsonPath("$.status").value(request.status?.name))
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ["ADMIN"])
    fun `should delete existing user`() {
        doNothing().`when`(userService).deleteUser(READ_ONLY_ID)

        mockMvc.perform(delete("/api/v1/users/$READ_ONLY_ID").with(csrf()))
            .andExpect(status().isNoContent)
    }

    @Test
    @WithMockUser(username = MANAGER_USERNAME, roles = ["SALES_MANAGER"])
    fun `should not allow non-admin to create user`() {
        val request = CreateUserRequest(
            username = "newuser",
            email = "newuser@example.com",
            password = "password123",
            role = UserRole.SALES_REPRESENTATIVE
        )

        mockMvc.perform(
            post("/api/v1/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ["ADMIN"])
    fun `should list all users`() {
        val users = listOf(
            UserResponse(
                id = ADMIN_ID,
                username = ADMIN_USERNAME,
                email = "admin@example.com",
                role = UserRole.ADMIN,
                status = UserStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            UserResponse(
                id = MANAGER_ID,
                username = MANAGER_USERNAME,
                email = "manager@example.com",
                role = UserRole.SALES_MANAGER,
                status = UserStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        given(userService.listUsers(any())).willReturn(
            PageImpl(users, PageRequest.of(0, 20), users.size.toLong())
        )

        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].username").value(ADMIN_USERNAME))
            .andExpect(jsonPath("$.content[1].username").value(MANAGER_USERNAME))
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ["ADMIN"])
    fun `should handle user not found`() {
        given(userService.getUser(999L)).willThrow(EntityNotFoundException("User not found with id: 999"))

        mockMvc.perform(get("/api/v1/users/999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("User not found with id: 999"))
    }
}
