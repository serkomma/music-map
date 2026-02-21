package com.serkomma.musicmap.repo.postgres

actual fun getEnv(name: String): String? = System.getenv(name)