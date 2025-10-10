pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

rootProject.name = "music-map"

includeBuild("lessons")
includeBuild("music-map-be")