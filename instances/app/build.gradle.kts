plugins {
    id("repgit.android-app")
}

android.namespace = "mir.anika1d.repgit.app"

dependencies {
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core.coroutines)
    implementation(projects.components.core.decompose)
    implementation(projects.components.core.resources)
    implementation(projects.components.core.network.connection.service)
    implementation(projects.datastore.api)
    implementation(projects.datastore.impl)
    implementation(projects.downloadlogic.service)
    implementation(projects.downloadlogic.receiver)
    implementation(projects.components.rootscreen.api)
    implementation(projects.components.rootscreen.impl)
    implementation(projects.components.singleactivity.impl)
    implementation(libs.openid)
}
