plugins {
    id("multiplatform-plugin")
}

group = "com.serkomma.musicmap.app.common"
version = "0.1"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":music-map-common"))
                implementation(project(":music-map-api-log"))
                implementation(project(":music-map-biz"))
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}