plugins {
    id("repgit.android-compose")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.repgit.rootscreen.api"

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.activity.ktx)
    implementation(libs.koin.core.coroutines)
    implementation(libs.bundles.decompose)
    implementation(projects.components.repodetailscreen.impl)
    implementation(projects.components.repodetailscreen.api)
    implementation(projects.components.core.decompose)
    implementation(projects.components.profilescreen.api)
}
