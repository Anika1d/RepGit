plugins {
    id("repgit.android-compose")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.searchscreen.impl"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.bundles.decompose)
     
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.androidx.compose)

    implementation(projects.components.core.decompose)
    implementation(projects.components.core.resources)
    implementation(projects.components.searchscreen.api)
    implementation(projects.components.core.ui.theme)
    implementation(projects.components.core.ui.composable)
    implementation(projects.components.core.ui.data)
    implementation(projects.components.core.network.connection.service)
    implementation(projects.network.usecases.search)
    implementation(projects.network.core.data)
    implementation(projects.database.impl)
    implementation(projects.database.api)
    implementation(projects.datastore.api)
}
