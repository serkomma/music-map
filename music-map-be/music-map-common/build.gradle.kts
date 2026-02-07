plugins {
//    id("build-kmp")
    id("multiplatform")
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
                implementation(kotlin("stdlib-jdk8"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}