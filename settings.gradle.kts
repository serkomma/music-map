pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("multiplatform") version kotlinVersion
    }
}

rootProject.name = "music-map"

includeBuild("lessons")
includeBuild("music-map-be")