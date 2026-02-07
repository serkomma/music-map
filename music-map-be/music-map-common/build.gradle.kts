plugins {
    id("multiplatform-plugin")
}

group = "com.serkomma"
version = "0.1"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                api("com.serkomma.musicmap.libs:music-map-lib-logging-common")
            }
        }
    }
}