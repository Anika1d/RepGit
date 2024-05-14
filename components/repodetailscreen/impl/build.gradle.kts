plugins {
    id("repgit.android-compose")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.repodetailscreen.impl"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.decompose)
    implementation(libs.decompose.jetpack)
    implementation(libs.coil.compose)
    implementation(libs.compose.activity)
    implementation(projects.components.core.decompose)
    implementation(projects.components.repodetailscreen.api)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.androidx.compose)
     
    implementation(projects.components.core.ui.theme)
    implementation(projects.components.core.ui.composable)
    implementation(projects.components.core.ui.data)
    implementation(projects.components.core.resources)
    implementation(projects.components.core.network.connection.service)
    implementation(projects.network.usecases.repository)
    implementation(projects.network.usecases.issues)
    implementation(projects.network.core.data)
    implementation(projects.downloadlogic.service)
    implementation(projects.datastore.api)
    implementation(projects.datastore.impl)


}
