plugins {
    id("repgit.android-compose")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.profilescreen.impl"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.decompose)
    implementation(libs.decompose.jetpack)
    implementation(libs.coil.compose)
    implementation(libs.compose.preview)
    implementation(libs.compose.ui.tooling)
    implementation(projects.components.core.decompose)
    implementation(projects.components.profilescreen.api)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.androidx.compose)
    implementation(projects.components.core.ui.theme)
    implementation(projects.components.core.ui.composable)
    implementation(projects.components.core.ui.data)
    implementation(projects.components.core.resources)
    implementation(projects.components.core.network.connection.service)
    implementation(projects.network.usecases.users)
    implementation(projects.network.usecases.search)
    implementation(projects.network.core.data)
    implementation(projects.datastore.api)
    implementation(projects.datastore.impl)
}
