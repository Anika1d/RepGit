plugins {
    id("repgit.android-compose")
}

android.namespace = "mir.anika1d.repgit.singleactivity.impl"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.annotation.jvm)
    implementation(libs.core.ktx)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.bundles.decompose)
    implementation(libs.compose.activity)
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.androidx.compose)
    implementation(projects.components.core.decompose)
    implementation(projects.components.rootscreen.api)
    implementation(projects.components.rootscreen.impl)
    implementation(projects.components.searchscreen.api)
    implementation(projects.components.repodetailscreen.api)
    implementation(projects.components.core.ui.theme)
    implementation(projects.components.core.ui.composable)
    implementation(projects.components.core.ui.data)
}
