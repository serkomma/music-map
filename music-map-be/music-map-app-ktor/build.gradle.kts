plugins {
    id("multiplatform-plugin")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.serkomma.musicmap.app.ktor"
version = "0.1"


kotlin {
    tasks.withType<Jar> {
        doFirst {
            manifest.attributes["Main-Class"] = "com.serkomma.musicmap.app.ktor.ApplicationKt"
            val dependencies = configurations
                .jvmRuntimeClasspath
                .get()
                .map(::zipTree)
            from(dependencies)
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.cors)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.server.default.headers)
                implementation(libs.ktor.server.caching.headers)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.server.content.negotiation)
                implementation(libs.kotlinx.serialization.json)
                implementation(project(":music-map-api"))
                implementation(project(":music-map-common"))
                implementation(project(":music-map-app-common"))
                implementation(project(":music-map-stubs"))
                implementation(project(":music-map-biz"))
                api("com.serkomma.musicmap.libs:music-map-lib-logging-kermit")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.ktor.server.test)
                implementation(libs.ktor.client.negotiation)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.server.call.logging)
                implementation(libs.h2)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

