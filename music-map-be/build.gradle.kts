plugins {
    id("multiplatform-plugin")
    alias(libs.plugins.muschko.remote) apply false
}

group = "com.serkomma.musicmap"
version = "0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

dependencies {
    commonMainImplementation(project(":music-map-api"))
    commonMainImplementation(project(":music-map-api-log"))
    commonMainImplementation(project(":music-map-common"))
    commonMainImplementation(project(":music-map-app-ktor"))
    commonMainImplementation(project(":music-map-stubs"))
    commonMainImplementation(project(":music-map-app-common"))
    commonMainImplementation(project(":music-map-repo-common"))
    commonMainImplementation(project(":music-map-repo-inmemory"))
    commonMainImplementation(project(":music-map-repo-postgres"))
    commonMainImplementation(project(":music-map-repo-tests"))
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-card-v1.yaml").toString().replace("\\", "/"))
    set("spec-log", specDir.file("specs-card-log.yaml").toString().replace("\\", "/"))
}