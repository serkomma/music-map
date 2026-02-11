plugins {
    id("multiplatform-plugin")
}

//group = "com.serkomma.musicmap.libs.logging.kermit"
//version = "0.1"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":music-map-lib-logging-common"))
                implementation(libs.kermit)
            }
        }
    }
}