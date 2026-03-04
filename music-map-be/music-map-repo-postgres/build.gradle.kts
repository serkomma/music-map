import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerInspectContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStopContainer
import com.bmuschko.gradle.docker.tasks.container.DockerWaitContainer
import com.bmuschko.gradle.docker.tasks.image.DockerPullImage
import com.github.dockerjava.api.command.InspectContainerResponse
import com.github.dockerjava.api.model.ExposedPort
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

plugins {
    id("multiplatform-plugin")
    alias(libs.plugins.ksp)
    alias(libs.plugins.liquibase)
    alias(libs.plugins.muschko.remote)
}

buildscript {
    dependencies {
        classpath(libs.liquibase.core)
    }
}

group = "com.serkomma.musicmap.repo.postgres"
version = "0.1"

repositories {
    google()
    mavenCentral()
}

kotlin {
    compilerOptions {
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.coroutines.core)
                implementation(project(":music-map-common"))
                implementation(project(":music-map-repo-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":music-map-repo-tests"))
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.db.postgres)
                implementation(libs.bundles.exposed)
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        nativeMain {
            dependencies {
                implementation(libs.sqlx4k.postgres)
            }
            dependsOn(commonMain.get())
        }

        nativeTest {
            dependsOn(commonTest.get())
        }
        linuxX64Main {
            kotlin.srcDir("build/generated/ksp/linuxX64/linuxX64Main/kotlin/com")
            dependsOn(nativeMain.get())
        }

        mingwX64Main {
            kotlin.srcDir("build/generated/ksp/linuxX64/mingwX64Main/kotlin/com")
            dependsOn(nativeMain.get())
        }
        linuxX64Test {
            dependsOn(nativeTest.get())
        }
        mingwX64Test {
            dependsOn(nativeTest.get())
        }
    }
}

dependencies {
    val a = ksp(libs.sqlx4k.codegen)
    add("kspCommonMainMetadata", libs.sqlx4k.codegen)
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.picocli)
    liquibaseRuntime(libs.liquibase.snakeyml)
    liquibaseRuntime(libs.db.postgres)
}

ksp {
    arg("output-package", "com.serkomma.musicmap.repo.postgres")
}

tasks {
    var pgPort = 5440
    val taskGroup = "pgContainerTest"
    val pgDbName = "musicmaptest"
    val pgUsername = "test"
    val pgPassword = "test"
    // Здесь в тасках запускаем PotgreSQL в контейнере
    // Накатываем liquibase миграцию
    // Передаем настройки в среду тестирования
    val postgresImage = "postgres:latest"
    val pullImage by registering(DockerPullImage::class) {
        group = taskGroup
        image.set(postgresImage)
    }
    val dbContainer by registering(DockerCreateContainer::class) {
        group = taskGroup
        dependsOn(pullImage)
        targetImageId(pullImage.get().image)
        withEnvVar("POSTGRES_PASSWORD", pgPassword)
        withEnvVar("POSTGRES_USER", pgUsername)
        withEnvVar("POSTGRES_DB", pgDbName)
        healthCheck.cmd("pg_isready")
        hostConfig.portBindings.set(listOf(":5432"))
        exposePorts("tcp", listOf(5432))
        hostConfig.autoRemove.set(true)
    }
    val stopPg by registering(DockerStopContainer::class) {
        ->
        group = taskGroup
        targetContainerId(dbContainer.get().containerId)
    }
    val startPg by registering(DockerStartContainer::class) {
        ->
        group = taskGroup
        dependsOn(dbContainer)
        targetContainerId(dbContainer.get().containerId)
        finalizedBy(stopPg)
    }
    val inspectPg by registering(DockerInspectContainer::class) {
        ->
        group = taskGroup
        dependsOn(startPg)
        finalizedBy(stopPg)
        targetContainerId(dbContainer.get().containerId)
        onNext(
            object : Action<InspectContainerResponse> {
                override fun execute(container: InspectContainerResponse) {
                    pgPort = container.networkSettings.ports.bindings[ExposedPort.tcp(5432)]
                        ?.first()
                        ?.hostPortSpec
                        ?.toIntOrNull()
                        ?: throw Exception("Postgres port is not found in container")
                }
            }
        )
    }
    val liquibaseUpdate = getByName("update") {
        group = taskGroup
        dependsOn(inspectPg)
        finalizedBy(stopPg)
        doFirst {
            println("waiting for a while ${System.currentTimeMillis()/1000000}")
            Thread.sleep(30000)
            println("LQB: \"jdbc:postgresql://localhost:$pgPort/$pgDbName\" ${System.currentTimeMillis()/1000000}")
            liquibase {
                activities {
                    register("test") {
                        arguments = mapOf(
                            "logLevel" to "info",
                            "searchPath" to layout.projectDirectory.dir("migrations/changelog").asFile.toString(),
                            "changelogFile" to "changelog-v0.0.1.sql",
                            "url" to "jdbc:postgresql://localhost:$pgPort/$pgDbName",
                            "username" to pgUsername,
                            "password" to pgPassword,
                            "driver" to "org.postgresql.Driver"
                        )
                    }
                }
            }
        }
    }
    val waitPg by creating(DockerWaitContainer::class) {
        group = taskGroup
        dependsOn(inspectPg)
        dependsOn(liquibaseUpdate)
        containerId.set(startPg.get().containerId)
        finalizedBy(stopPg)
        doFirst {
            println("PORT: $pgPort")
        }
    }
    withType(KotlinNativeTest::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
    withType(Test::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
}
