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
                api("com.serkomma.musicmap.libs:music-map-lib-cor")
                api(libs.coroutines.core)
            }
        }
        commonTest {
            dependencies {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                    api(libs.coroutines.test)
                }
            }
        }
    }
}