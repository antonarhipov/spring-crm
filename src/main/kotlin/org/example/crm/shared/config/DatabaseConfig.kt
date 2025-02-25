package org.example.crm.shared.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJdbcRepositories(basePackages = ["org.example.crm"])
@EnableTransactionManagement
class DatabaseConfig