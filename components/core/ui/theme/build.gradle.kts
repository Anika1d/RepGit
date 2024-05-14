plugins {
    id("repgit.android-compose")
}

android.namespace = "mir.anika1d.core.ui.theme"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.core.ktx)

}
