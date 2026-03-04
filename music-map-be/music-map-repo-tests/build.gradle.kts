plugins {
    id("multiplatform-plugin")
}

group = "com.serkomma.musicmap.repo.tests"
version = "0.1"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("test"))
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))

                api(libs.coroutines.core)
                api(libs.coroutines.test)
                implementation(project(":music-map-common"))
                implementation(project(":music-map-repo-common"))
            }
        }
        jvmMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}