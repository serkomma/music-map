plugins {
    id("multiplatform-plugin")
}

group = "com.serkomma.musicmap.repo.common"
version = "0.1"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":music-map-common"))
            }
        }
    }
}