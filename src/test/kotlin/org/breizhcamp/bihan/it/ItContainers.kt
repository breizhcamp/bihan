package org.breizhcamp.bihan.it

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.postgresql.PostgreSQLContainer

@TestConfiguration(proxyBeanMethods = false)
class ItContainers {

    @Bean
    @ServiceConnection
    fun postgresqlContainer(): PostgreSQLContainer =
        PostgreSQLContainer("postgres:15.2")

}