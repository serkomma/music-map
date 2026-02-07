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
//        id("build-kmp") apply false
        id("multiplatform") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":music-map-tmp")
include(":music-map-api")
include(":music-map-common")
include(":music-map-stubs")