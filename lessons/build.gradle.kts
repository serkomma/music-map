plugins {
    kotlin("jvm") version "2.2.0"
}

group = "ru.serkomma"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}