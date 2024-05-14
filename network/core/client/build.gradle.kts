import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_AUTH_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_CALLBACK_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_CLIENT_ID
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_END_SESSION_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_ID
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_LOGOUT_CALLBACK_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_SCOPE_WORKING
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_SECRET_KEY
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_TOKEN_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_BASE_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_HOSTNAME
plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.ksp) apply true
    alias(libs.plugins.kotlin.serialization) apply true
}

android {
    namespace = "mir.anika1d.repgit.network.core.client"
    buildTypes {
        defaultConfig {
            buildConfigField(
                "String",
                "GITHUB_BASE_URL",
                "\"${GITHUB_BASE_URL}\""
            )
            buildConfigField(
                "String",
                "GITHUB_HOSTNAME",
                "\"${GITHUB_HOSTNAME}\""
            )
            buildConfigField(
                "String",
                "GITHUB_APP_AUTH_URL",
                "\"${GITHUB_APP_AUTH_URL}\""
            )
            buildConfigField(
                "String",
                "GITHUB_APP_TOKEN_URL",
                "\"${GITHUB_APP_TOKEN_URL}\""
            )
            buildConfigField(
                "String",
                "GITHUB_APP_END_SESSION_URL",
                "\"${GITHUB_APP_END_SESSION_URL}\""
            )

            buildConfigField(
                "String",
                "GITHUB_APP_SCOPE_WORKING",
                "\"${GITHUB_APP_SCOPE_WORKING}\""
            )
            buildConfigField(
                "String",
                "GITHUB_APP_ID",
                "\"${GITHUB_APP_ID}\""
            )
            buildConfigField(
                "String",
                "GITHUB_APP_CLIENT_ID",
                "\"${GITHUB_APP_CLIENT_ID}\""
            )
            buildConfigField(
                "String",
                "GITHUB_APP_SECRET_KEY",
                "\"${GITHUB_APP_SECRET_KEY}\""
            )
            buildConfigField(
                "String",
                "GITHUB_APP_CALLBACK_URL",
                "\"${GITHUB_APP_CALLBACK_URL}\""
            )
            buildConfigField(
                "String",
                "GITHUB_APP_LOGOUT_CALLBACK_URL",
                "\"${GITHUB_APP_LOGOUT_CALLBACK_URL}\""
            )
        }
    }
}
dependencies {
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.ktor)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging.jvm)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.auth)
    implementation(libs.logback.classic)
    implementation(libs.openid)
    implementation(libs.kotlin.serialization.gen)
    implementation(libs.kotlin.serialization.json)
    implementation(projects.datastore.api)
    implementation(projects.datastore.impl)

}

