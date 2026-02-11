plugins {
    id("multiplatform-plugin")
}

group = "com.serkomma.musicmap.biz"
version = "0.1"


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":music-map-common"))
                implementation(project(":music-map-stubs"))
            }
        }
    }
}