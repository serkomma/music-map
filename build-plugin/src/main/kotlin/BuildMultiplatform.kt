package com.serkomma.musicmap.plugin

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.text.set

class BuildMultiplatform : Plugin<Project> {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        group = rootProject.group
        version = rootProject.version

        plugins.withId("org.jetbrains.kotlin.multiplatform") {
            extensions.configure<KotlinMultiplatformExtension> {
                val libs = project.the<LibrariesForLibs>()
//                val hostOs = System.getProperty("os.name")
//                val isMingwX64 = hostOs.startsWith("Windows")
//                val nativeTarget = when {
//                    hostOs == "Mac OS X" -> macosX64("native")
//                    hostOs == "Linux" -> linuxX64("native")
//                    isMingwX64 -> mingwX64("native")
//                    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//                }
//                nativeTarget.apply {
//                    compilations.getByName("main") {
//                        if(file("src/nativeInterop/cinterop/version.def").isFile) {
//                            cinterops {
//                                with(create("version")) {
//                                    definitionFile.set(file("src/nativeInterop/cinterop/version.def"))
//                                    includeDirs("${projectDir}/src/nativeInterop/cinterop")
//                                    compilerOpts.add("${projectDir}/src/nativeInterop/cinterop")
//                                }
//                            }
//                        }
//                    }
//                    binaries {
//                        executable {
//                            entryPoint = "com.serkomma.musicmap.app.ktor.main"
//                        }
//                    }
//                }

                jvmToolchain {
                    languageVersion.set(JavaLanguageVersion.of(libs.versions.jvm.language.get()))
                }

                jvm {
                    compilations.configureEach {
                        compilerOptions.configure {
                            jvmTarget.set(JvmTarget.valueOf("JVM_${libs.versions.jvm.compiler.get()}"))
                        }
                    }
                    mainRun {
                        mainClass.set("com.serkomma.musicmap.app.ktor.ApplicationKt")
                    }
                }
                linuxX64 {
                    binaries {
                        executable {
                            entryPoint = "com.serkomma.musicmap.app.ktor.main"
                        }
                    }
                }
                mingwX64 {
                    binaries {
                        executable {
                            entryPoint = "com.serkomma.musicmap.app.ktor.main"
                        }
                    }
                }
            }
        }
    }

}