package org.example.crm.shared.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
data class JwtConfig(
    var secret: String = "default_secret_key_which_should_be_replaced_in_production",
    var expirationMs: Long = 86400000 // 24 hours
)