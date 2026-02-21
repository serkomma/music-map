package com.serkomma.musicmap.repo.postgres

actual fun SqlProperties.url(): String {
    return "postgresql://${host}:${port}/${database}"
}