plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.ksp) apply true
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.network.github.repositories.api"

dependencies {
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.ktor)

    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.kotlin.serialization.gen)
    implementation(libs.kotlin.serialization.json)
    implementation(projects.network.core.data)
    implementation(libs.openid)
}
