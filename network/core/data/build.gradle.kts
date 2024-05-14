
plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.ksp) apply true
    alias(libs.plugins.kotlin.serialization) apply true
}

android . namespace = "mir.anika1d.repgit.network.core.data"
dependencies {
    implementation(libs.kotlin.serialization.gen)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.ktor.client.android)
}

