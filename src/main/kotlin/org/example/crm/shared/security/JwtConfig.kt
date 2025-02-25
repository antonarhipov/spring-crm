package org.example.crm.shared.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class JwtConfig(
    val secret: String = "default_secret_key_which_should_be_replaced_in_production",
    val expirationMs: Long = 86400000 // 24 hours
)
