package org.example.crm

import org.example.crm.config.TestConfig
import org.junit.jupiter.api.Test
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@ContextConfiguration(classes = [TestConfig::class])
@TestPropertySource(
    properties = [
        "spring.main.allow-bean-definition-overriding=true",
        "jwt.secret=test_secret_key_for_testing_purposes_only",
        "jwt.expiration-ms=3600000"
    ]
)
class SpringCrmApplicationTests {

    @Test
    fun contextLoads() {

    }




}
