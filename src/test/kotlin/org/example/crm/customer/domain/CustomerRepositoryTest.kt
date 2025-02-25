package org.example.crm.customer.domain

import org.example.crm.config.TestContainersConfig
import org.example.crm.util.TestDataFactory
import org.example.crm.user.domain.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.junit.jupiter.Testcontainers
import org.assertj.core.api.Assertions.assertThat

@JdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfig::class)
@Testcontainers
@Sql("/db/schema.sql")
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    private val testUser = TestDataFactory.createUser(
        id = 1L,
        username = "testuser",
        email = "test@example.com",
        role = UserRole.SALES_REPRESENTATIVE
    )

    @Test
    fun `should save and find customer by id`() {
        // given
        val customer = TestDataFactory.createCustomer(
            name = "Test Customer",
            company = "Test Company",
            createdBy = testUser.id!!
        )

        // when
        val savedCustomer = customerRepository.save(customer)
        val foundCustomer = customerRepository.findById(savedCustomer.id!!)

        // then
        assertThat(foundCustomer).isPresent
        assertThat(foundCustomer.get()).matches {
            it.name == customer.name &&
            it.company == customer.company &&
            it.status == CustomerStatus.ACTIVE &&
            it.createdBy == testUser.id
        }
    }

    @Test
    fun `should find customers by status`() {
        // given
        val customers = listOf(
            TestDataFactory.createCustomer(
                name = "Active Customer",
                status = CustomerStatus.ACTIVE,
                createdBy = testUser.id!!
            ),
            TestDataFactory.createCustomer(
                name = "Inactive Customer",
                status = CustomerStatus.INACTIVE,
                createdBy = testUser.id!!
            )
        )
        customers.forEach { customerRepository.save(it) }

        // when
        val activeCustomers = customerRepository.findByStatus(CustomerStatus.ACTIVE, PageRequest.of(0, 10))
        val inactiveCustomers = customerRepository.findByStatus(CustomerStatus.INACTIVE, PageRequest.of(0, 10))

        // then
        assertThat(activeCustomers.content).hasSize(1)
        assertThat(activeCustomers.content[0].name).isEqualTo("Active Customer")
        assertThat(inactiveCustomers.content).hasSize(1)
        assertThat(inactiveCustomers.content[0].name).isEqualTo("Inactive Customer")
    }

    @Test
    fun `should find customers by category`() {
        // given
        val customers = listOf(
            TestDataFactory.createCustomer(
                name = "Premium Customer",
                category = CustomerCategory.PREMIUM,
                createdBy = testUser.id!!
            ),
            TestDataFactory.createCustomer(
                name = "Enterprise Customer",
                category = CustomerCategory.ENTERPRISE,
                createdBy = testUser.id!!
            )
        )
        customers.forEach { customerRepository.save(it) }

        // when
        val premiumCustomers = customerRepository.findByCategory(CustomerCategory.PREMIUM, PageRequest.of(0, 10))
        val enterpriseCustomers = customerRepository.findByCategory(CustomerCategory.ENTERPRISE, PageRequest.of(0, 10))

        // then
        assertThat(premiumCustomers.content).hasSize(1)
        assertThat(premiumCustomers.content[0].name).isEqualTo("Premium Customer")
        assertThat(enterpriseCustomers.content).hasSize(1)
        assertThat(enterpriseCustomers.content[0].name).isEqualTo("Enterprise Customer")
    }

    @Test
    fun `should find customers by created by`() {
        // given
        val anotherUser = TestDataFactory.createUser(id = 2L)
        val customers = listOf(
            TestDataFactory.createCustomer(
                name = "Customer 1",
                createdBy = testUser.id!!
            ),
            TestDataFactory.createCustomer(
                name = "Customer 2",
                createdBy = anotherUser.id!!
            )
        )
        customers.forEach { customerRepository.save(it) }

        // when
        val userCustomers = customerRepository.findByCreatedBy(testUser.id!!, PageRequest.of(0, 10))

        // then
        assertThat(userCustomers.content).hasSize(1)
        assertThat(userCustomers.content[0].name).isEqualTo("Customer 1")
        assertThat(userCustomers.content[0].createdBy).isEqualTo(testUser.id)
    }

    @Test
    fun `should delete customer`() {
        // given
        val customer = TestDataFactory.createCustomer(
            name = "To Delete",
            createdBy = testUser.id!!
        )
        val savedCustomer = customerRepository.save(customer)

        // when
        customerRepository.deleteById(savedCustomer.id!!)

        // then
        assertThat(customerRepository.findById(savedCustomer.id!!)).isEmpty
    }
}