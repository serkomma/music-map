package com.serkomma.musicmap.repo.postgres

data class SqlProperties(
    val host: String = getEnv("DB_HOST") ?: "localhost",
    val port: Int = getEnv("DB_PORT")?.toIntOrNull() ?: 5433,
    val user: String = "postgres",
    val password: String = "postgres",
    val database: String = "musicmap",
    val schema: String = "public",
)

expect fun SqlProperties.url(): String
