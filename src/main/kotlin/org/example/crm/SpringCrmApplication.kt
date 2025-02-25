package org.example.crm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.example.crm.shared.security.JwtConfig

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig::class)
class SpringCrmApplication

fun main(args: Array<String>) {
    runApplication<SpringCrmApplication>(*args)
}
