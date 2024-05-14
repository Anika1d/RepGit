plugins {
    id("repgit.android-compose")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.profilescreen.api"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.decompose)
    implementation(libs.decompose.jetpack)
    implementation(libs.coil.compose)
    implementation(projects.components.core.decompose)
    implementation(projects.components.core.ui.data)
}
