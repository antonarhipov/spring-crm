package org.example.crm.contact.domain

import org.example.crm.config.TestContainersConfig
import org.example.crm.util.TestDataFactory
import org.example.crm.user.domain.UserRole
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
class ContactRepositoryTest {

    @Autowired
    private lateinit var contactRepository: ContactRepository

    private val testUser = TestDataFactory.createUser(
        id = 1L,
        username = "testuser",
        email = "test@example.com",
        role = UserRole.SALES_REPRESENTATIVE
    )

    private val testCustomer = TestDataFactory.createCustomer(
        id = 1L,
        name = "Test Customer",
        createdBy = testUser.id!!
    )

    @Test
    fun `should save and find contact by id`() {
        // given
        val contact = TestDataFactory.createContact(
            name = "Test Contact",
            customerId = testCustomer.id!!,
            createdBy = testUser.id!!
        )

        // when
        val savedContact = contactRepository.save(contact)
        val foundContact = contactRepository.findById(savedContact.id!!)

        // then
        assertThat(foundContact).isPresent
        assertThat(foundContact.get()).matches {
            it.name == contact.name &&
            it.customerId == testCustomer.id &&
            it.createdBy == testUser.id
        }
    }

    @Test
    fun `should find contacts by customer id`() {
        // given
        val contacts = listOf(
            TestDataFactory.createContact(
                name = "Contact 1",
                customerId = testCustomer.id!!,
                createdBy = testUser.id!!
            ),
            TestDataFactory.createContact(
                name = "Contact 2",
                customerId = testCustomer.id!!,
                createdBy = testUser.id!!
            )
        )
        contacts.forEach { contactRepository.save(it) }

        // when
        val customerContacts = contactRepository.findByCustomerId(testCustomer.id!!, PageRequest.of(0, 10))

        // then
        assertThat(customerContacts.content).hasSize(2)
        assertThat(customerContacts.content.map { it.name }).containsExactlyInAnyOrder("Contact 1", "Contact 2")
    }

    @Test
    fun `should find contacts by communication preference`() {
        // given
        val contacts = listOf(
            TestDataFactory.createContact(
                name = "Email Contact",
                customerId = testCustomer.id!!,
                communicationPreference = CommunicationPreference.EMAIL,
                createdBy = testUser.id!!
            ),
            TestDataFactory.createContact(
                name = "Phone Contact",
                customerId = testCustomer.id!!,
                communicationPreference = CommunicationPreference.PHONE,
                createdBy = testUser.id!!
            )
        )
        contacts.forEach { contactRepository.save(it) }

        // when
        val emailContacts = contactRepository.findByCustomerIdAndCommunicationPreference(
            testCustomer.id!!,
            CommunicationPreference.EMAIL,
            PageRequest.of(0, 10)
        )
        val phoneContacts = contactRepository.findByCustomerIdAndCommunicationPreference(
            testCustomer.id!!,
            CommunicationPreference.PHONE,
            PageRequest.of(0, 10)
        )

        // then
        assertThat(emailContacts.content).hasSize(1)
        assertThat(emailContacts.content[0].name).isEqualTo("Email Contact")
        assertThat(phoneContacts.content).hasSize(1)
        assertThat(phoneContacts.content[0].name).isEqualTo("Phone Contact")
    }

    @Test
    fun `should delete contact`() {
        // given
        val contact = TestDataFactory.createContact(
            name = "To Delete",
            customerId = testCustomer.id!!,
            createdBy = testUser.id!!
        )
        val savedContact = contactRepository.save(contact)

        // when
        contactRepository.deleteById(savedContact.id!!)

        // then
        assertThat(contactRepository.findById(savedContact.id!!)).isEmpty
    }

    @Test
    fun `should delete contacts by customer id`() {
        // given
        val contacts = listOf(
            TestDataFactory.createContact(
                name = "Contact 1",
                customerId = testCustomer.id!!,
                createdBy = testUser.id!!
            ),
            TestDataFactory.createContact(
                name = "Contact 2",
                customerId = testCustomer.id!!,
                createdBy = testUser.id!!
            )
        )
        contacts.forEach { contactRepository.save(it) }

        // when
        contactRepository.deleteByCustomerId(testCustomer.id!!)

        // then
        val remainingContacts = contactRepository.findByCustomerId(testCustomer.id!!, PageRequest.of(0, 10))
        assertThat(remainingContacts.content).isEmpty()
    }
}