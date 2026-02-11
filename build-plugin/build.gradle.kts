plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
//        register("build-jvm") {
//            id = "build-jvm"
//            implementationClass = "com.serkomma.musicmap.plugin.BuildPluginJvm"
//        }
//        register("build-kmp") {
//            id = "build-kmp"
//            implementationClass = "com.serkomma.musicmap.plugin.BuildPluginMultiplatform"
//        }
        register("multiplatform-plugin") {
            id = "multiplatform-plugin"
            implementationClass = "com.serkomma.musicmap.plugin.BuildMultiplatform"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // enable Ktlint formatting
//    add("detektPlugins", libs.plugin.detektFormatting)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.plugin.kotlin)
//    implementation(libs.plugin.dokka)
    implementation(libs.plugin.binaryCompatibilityValidator)
//    implementation(libs.plugin.mavenPublish)
}
