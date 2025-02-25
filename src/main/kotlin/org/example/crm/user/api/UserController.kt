package org.example.crm.user.api

import jakarta.validation.Valid
import org.example.crm.user.api.dto.CreateUserRequest
import org.example.crm.user.api.dto.UpdateUserRequest
import org.example.crm.user.api.dto.UserResponse
import org.example.crm.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    fun createUser(@Valid @RequestBody request: CreateUserRequest): UserResponse =
        userService.createUser(request)

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUser(@PathVariable id: Long): UserResponse =
        userService.getUser(id)

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): UserResponse = userService.updateUser(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable id: Long) =
        userService.deleteUser(id)

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun listUsers(
        @PageableDefault(size = 20, sort = ["username"]) pageable: Pageable
    ): Page<UserResponse> = userService.listUsers(pageable)

    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    fun findByUsername(@PathVariable username: String): UserResponse =
        userService.findByUsername(username)

    @GetMapping("/by-email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    fun findByEmail(@PathVariable email: String): UserResponse =
        userService.findByEmail(email)
}