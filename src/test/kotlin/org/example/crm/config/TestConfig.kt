package org.example.crm.config

import org.example.crm.shared.security.JwtConfig
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfig {

    @Bean
    @Primary
    fun jwtConfig(): JwtConfig = JwtConfig(
        secret = "test_secret_key_for_testing_purposes_only",
        expirationMs = 3600000
    )
}
