plugins {
    id("repgit.android-compose")
}

android.namespace = "mir.anika1d.core.decompose"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.decompose)
    implementation(libs.decompose.jetpack)
}
