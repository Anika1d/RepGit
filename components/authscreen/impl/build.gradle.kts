plugins {
    id("repgit.android-compose")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.authscreen.impl"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.decompose)
    implementation(libs.decompose.jetpack)
    implementation(libs.coil.compose)
    implementation(libs.openid)
    implementation(libs.compose.activity)
    implementation(projects.components.core.decompose)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.androidx.compose)
    implementation(libs.browser)
    implementation(projects.network.usecases.auth)
    implementation(projects.components.authscreen.api)
    implementation(projects.components.core.ui.theme)
    implementation(projects.components.core.ui.composable)
    implementation(projects.components.core.ui.data)
    implementation(projects.components.core.resources)
    implementation(projects.components.core.network.connection.service)

}
