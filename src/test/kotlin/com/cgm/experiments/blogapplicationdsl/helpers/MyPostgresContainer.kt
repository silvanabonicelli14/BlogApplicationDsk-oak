package com.cgm.experiments.blogapplicationdsl.helpers

import org.testcontainers.containers.PostgreSQLContainer

object MyPostgresContainer {
    val container by lazy {
        PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("oak")
            withUsername("silvana")
            withPassword("pass")
            withExposedPorts(5432)
        }
    }
}
