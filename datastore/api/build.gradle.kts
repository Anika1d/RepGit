
plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.serialization)
}
android.namespace = "mir.anika1d.repgit.datastore.api"

dependencies {
    implementation(libs.datastore)
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)
    implementation(projects.components.core.ui.data)
}