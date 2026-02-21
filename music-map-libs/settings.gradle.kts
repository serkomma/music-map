rootProject.name = "music-map-libs"

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

include(":music-map-lib-logging-common")
include(":music-map-lib-logging-kermit")
include(":music-map-lib-cor")