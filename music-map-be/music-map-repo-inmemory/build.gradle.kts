plugins {
    id("multiplatform-plugin")
}

group = "com.serkomma.musicmap.repo.inmemory"
version = "0.1"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.coroutines.core)
                implementation(libs.db.cache4k)
                implementation(project(":music-map-common"))
                implementation(project(":music-map-stubs"))
                implementation(project(":music-map-repo-common"))
            }
        }
    }
}