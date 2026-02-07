plugins {
    id("multiplatform")
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
    commonMainImplementation(project(":music-map-common"))
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-card-v1.yaml").toString().replace("\\", "/"))
}