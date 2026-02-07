rootProject.name = "music-map-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("multiplatform-plugin") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":music-map-tmp")
include(":music-map-api")
include(":music-map-api-log")
include(":music-map-common")
include(":music-map-stubs")
include(":music-map-app-ktor")
include(":music-map-app-common")
include(":music-map-biz")
include(":music-map-app-kafka")