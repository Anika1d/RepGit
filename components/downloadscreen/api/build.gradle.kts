plugins {
    id("repgit.android-compose")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.downloadscreen.api"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.decompose)
    implementation(libs.decompose.jetpack)
    implementation(projects.components.core.decompose)
}
