package org.example.crm.user.domain

import org.example.crm.util.TestDataFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat

@JdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(org.example.crm.config.TestContainersConfig::class)
@Testcontainers
@Sql("/db/schema.sql")
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save and find user by id`() {
        // given
        val user = TestDataFactory.createUser(
            username = "testuser",
            email = "test@example.com",
            role = UserRole.SALES_REPRESENTATIVE
        )

        // when
        val savedUser = userRepository.save(user)
        val foundUser = userRepository.findById(savedUser.id!!)

        // then
        assertThat(foundUser).isPresent
        assertThat(foundUser.get()).matches {
            it.username == user.username &&
            it.email == user.email &&
            it.role == user.role &&
            it.status == UserStatus.ACTIVE
        }
    }

    @Test
    fun `should find user by username`() {
        // given
        val user = TestDataFactory.createUser(
            username = "findme",
            email = "findme@example.com"
        )
        userRepository.save(user)

        // when
        val foundUser = userRepository.findByUsername("findme")

        // then
        assertThat(foundUser).isPresent
        assertThat(foundUser.get().username).isEqualTo("findme")
    }

    @Test
    fun `should find user by email`() {
        // given
        val user = TestDataFactory.createUser(
            username = "emailtest",
            email = "find@example.com"
        )
        userRepository.save(user)

        // when
        val foundUser = userRepository.findByEmail("find@example.com")

        // then
        assertThat(foundUser).isPresent
        assertThat(foundUser.get().email).isEqualTo("find@example.com")
    }

    @Test
    fun `should check if username exists`() {
        // given
        val user = TestDataFactory.createUser(
            username = "exists",
            email = "exists@example.com"
        )
        userRepository.save(user)

        // when/then
        assertThat(userRepository.existsByUsername("exists")).isTrue
        assertThat(userRepository.existsByUsername("notexists")).isFalse
    }

    @Test
    fun `should check if email exists`() {
        // given
        val user = TestDataFactory.createUser(
            username = "emailcheck",
            email = "exists@test.com"
        )
        userRepository.save(user)

        // when/then
        assertThat(userRepository.existsByEmail("exists@test.com")).isTrue
        assertThat(userRepository.existsByEmail("notexists@test.com")).isFalse
    }

    @Test
    fun `should find users with pagination`() {
        // given
        val users = listOf(
            TestDataFactory.createUser(username = "user1", email = "user1@example.com"),
            TestDataFactory.createUser(username = "user2", email = "user2@example.com"),
            TestDataFactory.createUser(username = "user3", email = "user3@example.com")
        )
        users.forEach { userRepository.save(it) }

        // when
        val page = userRepository.findAll(PageRequest.of(0, 2))

        // then
        assertThat(page.content).hasSize(2)
        assertThat(page.totalElements).isEqualTo(3)
        assertThat(page.totalPages).isEqualTo(2)
    }

    @Test
    fun `should delete user`() {
        // given
        val user = TestDataFactory.createUser(
            username = "todelete",
            email = "delete@example.com"
        )
        val savedUser = userRepository.save(user)

        // when
        userRepository.deleteById(savedUser.id!!)

        // then
        assertThat(userRepository.findById(savedUser.id!!)).isEmpty
    }
}
