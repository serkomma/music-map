import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("multiplatform-plugin")
    kotlin("plugin.serialization") version "2.2.0"
    alias(libs.plugins.openapi.generator)
}

group = "com.serkomma"
version = "0.1"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/src/commonMain/kotlin"))
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(project(":music-map-common"))
                implementation(project(":music-map-stubs"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

openApiGenerate {
    generatorName = "kotlin"
    packageName = group.toString()
    inputSpec.set(rootProject.ext["spec-v1"] as String)
//    outputDir.set("$rootDir/src/openAPIStubs")
    typeMappings.put("string+date-time", "Instant")
    importMappings.putAll(
        mapOf(
            "Instant" to "kotlinx.datetime.Instant",
            "File" to "OctetByteArray",
        )
    )
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }
    validateSpec = false
    additionalProperties.set(
        mapOf(
            "library" to "multiplatform",
            "dateLibrary" to "kotlinx-datetime",
            "enumPropertyNaming" to "UPPERCASE",
            "useCoroutines" to "true",
        )
    )
}

tasks {
    val openApiGenerateTask: GenerateTask = getByName("openApiGenerate", GenerateTask::class) {
        outputDir.set(layout.buildDirectory.file("generate-resources").get().toString())
        finalizedBy("compileCommonMainKotlinMetadata")
    }
    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerateTask)
    }
}