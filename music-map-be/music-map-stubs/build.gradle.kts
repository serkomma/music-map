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
                implementation(project(":music-map-common"))
            }
        }
    }
}