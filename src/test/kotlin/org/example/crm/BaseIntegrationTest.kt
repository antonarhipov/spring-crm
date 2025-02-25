package org.example.crm

import org.example.crm.config.TestConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig::class)
@ActiveProfiles("test")
@Testcontainers
abstract class BaseIntegrationTest {
    companion object {
        const val ADMIN_ID = 1L
        const val MANAGER_ID = 2L
        const val SALES_REP_ID = 3L
        const val READ_ONLY_ID = 4L

        const val ADMIN_USERNAME = "admin"
        const val MANAGER_USERNAME = "manager"
        const val SALES_REP_USERNAME = "sales_rep"
        const val READ_ONLY_USERNAME = "read_only"
    }
}