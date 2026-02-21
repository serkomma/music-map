package com.serkomma.musicmap.repo.postgres

actual fun SqlProperties.url(): String {
    return "jdbc:postgresql://${host}:${port}/${database}"
}