@file:Suppress("Filename")

import com.android.build.gradle.BaseExtension
import mir.anika1d.repgit.buildlogic.ApkConfig
import mir.anika1d.repgit.buildlogic.ApkConfig.VERSION_CODE
import mir.anika1d.repgit.buildlogic.ApkConfig.VERSION_NAME
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun BaseExtension.commonAndroid(target: Project) {
    configureDefaultConfig(target)
    configureBuildTypes()
    configureBuildFeatures()
    configureCompileOptions()
    target.suppressOptIn()
}

private fun BaseExtension.configureDefaultConfig(project: Project) {
    compileSdkVersion(ApkConfig.COMPILE_SDK_VERSION)
    defaultConfig {
        minSdk = ApkConfig.MIN_SDK_VERSION
        targetSdk = ApkConfig.TARGET_SDK_VERSION
        versionCode = project.VERSION_CODE
        versionName = project.VERSION_NAME

        consumerProguardFiles(
            "consumer-rules.pro"
        )
        packagingOptions {
            resources.excludes += "META-INF/LICENSE-LGPL-2.1.txt"
            resources.excludes += "META-INF/LICENSE-LGPL-3.txt"
            resources.excludes += "META-INF/LICENSE-W3C-TEST"
            resources.excludes += "META-INF/DEPENDENCIES"
            resources.excludes += "META-INF/{AL2.0,LGPL2.1}"
            resources.excludes += "META-INF/INDEX.LIST"
            resources.excludes += "*.proto"
        }
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }
    }
}

private fun BaseExtension.configureBuildTypes() {
    buildTypes {
        maybeCreate("debug").apply {
            buildConfigField("boolean", "INTERNAL", "true")
            multiDexEnabled = true
            isDebuggable = true
        }
        maybeCreate("internal").apply {
            setMatchingFallbacks("debug")
            sourceSets.getByName(this.name).setRoot("src/debug")

            buildConfigField("boolean", "INTERNAL", "true")
        }
        maybeCreate("release").apply {
            buildConfigField("boolean", "INTERNAL", "true")
        }
    }
}

private fun BaseExtension.configureBuildFeatures() {
    buildFeatures.buildConfig = true
    buildFeatures.viewBinding = false
    buildFeatures.aidl = false
    buildFeatures.compose = false
    buildFeatures.prefab = false
    buildFeatures.renderScript = false
    buildFeatures.resValues = false
    buildFeatures.shaders = false
}

private fun BaseExtension.configureCompileOptions() {
    compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    compileOptions.targetCompatibility = JavaVersion.VERSION_17
}

private fun Project.suppressOptIn() {
    tasks.withType<KotlinCompile>()
        .configureEach {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
}

fun <BuildTypeT> NamedDomainObjectContainer<BuildTypeT>.debug(
    action: BuildTypeT.() -> Unit
) {
    maybeCreate("debug").action()
}

fun <BuildTypeT> NamedDomainObjectContainer<BuildTypeT>.internal(
    action: BuildTypeT.() -> Unit
) {
    maybeCreate("internal").action()
}

fun <BuildTypeT> NamedDomainObjectContainer<BuildTypeT>.release(
    action: BuildTypeT.() -> Unit
) {
    maybeCreate("release").action()
}
