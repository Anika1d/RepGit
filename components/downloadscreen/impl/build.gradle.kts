plugins {
    id("repgit.android-compose")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.downloadscreen.impl"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.decompose)
    implementation(libs.decompose.jetpack)
    implementation(libs.compose.activity)
    implementation(projects.components.core.decompose)
    implementation(projects.components.downloadscreen.api)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.androidx.compose)
     
    implementation(projects.components.core.ui.theme)
    implementation(projects.components.core.network.connection.service)
    implementation(projects.components.core.ui.composable)
    implementation(projects.components.core.ui.data)
    implementation(projects.components.core.resources)
    implementation(projects.downloadlogic.service)
}
