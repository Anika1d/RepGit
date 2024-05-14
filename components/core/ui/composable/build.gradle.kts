plugins {
    id("repgit.android-compose")
}

android.namespace = "mir.anika1d.core.ui.composable"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    implementation(libs.coil.compose)
    implementation(libs.core.ktx)
    implementation(projects.components.core.ui.theme)
    implementation(projects.components.core.ui.data)
    implementation(projects.components.core.network.connection.service)
    implementation(libs.ui.tooling.preview.android)
    implementation(libs.compose.preview)
    implementation(libs.compose.ui.tooling)
    implementation(projects.components.core.resources)
    debugImplementation(libs.ui.tooling.preview.android)
}
