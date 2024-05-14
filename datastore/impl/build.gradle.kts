plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.serialization)
}
android.namespace = "mir.anika1d.repgit.datastore.impl"

dependencies {
    implementation(libs.datastore)
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)
    implementation(projects.datastore.api)
    implementation(project(":components:core:ui:data"))
}