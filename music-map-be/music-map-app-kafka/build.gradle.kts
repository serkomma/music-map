plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("com.serkomma.musicmap.app.kafka.MainKt")
}

group = "com.serkomma.musicmap.app.kafka"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(project(":music-map-api"))
    implementation(project(":music-map-common"))
    implementation(project(":music-map-app-common"))
    implementation(project(":music-map-biz"))
    api("com.serkomma.musicmap.libs:music-map-lib-logging-kermit")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}