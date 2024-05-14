import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_AUTH_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_CALLBACK_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_CLIENT_ID
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_END_SESSION_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_ID
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_LOGOUT_CALLBACK_URL
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_SCOPE_WORKING
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_SECRET_KEY
import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_TOKEN_URL

plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.ksp) apply true
    alias(libs.plugins.kotlin.serialization) apply true
}
android {
    namespace = "mir.anika1d.repgit.network.github.repositories.impl"
    buildTypes {
        defaultConfig {
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
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.kotlin.serialization.gen)
    implementation(libs.kotlin.serialization.json)

    implementation(projects.network.github.repositories.api)
    implementation(projects.network.github.services.api)
    implementation(projects.network.github.services.impl)
    implementation(projects.network.core.data)
    implementation(libs.openid)

}
