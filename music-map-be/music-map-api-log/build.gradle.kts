import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("multiplatform-plugin")
    alias(libs.plugins.openapi.generator)
}

group = "com.serkomma.musicmap.api.log"
version = "0.1"

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/src/commonMain/kotlin"))
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(project(":music-map-common"))
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
    inputSpec.set(rootProject.ext["spec-log"] as String)
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