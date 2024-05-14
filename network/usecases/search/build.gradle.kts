plugins {
    id("repgit.android-lib")
}

android.namespace = "mir.anika1d.repgit.network.usecases.search"

dependencies {
    implementation(libs.kotlin.coroutines)
    implementation(libs.koin.core)
    implementation(projects.network.core.data)
    implementation(projects.network.github.repositories.api)
    implementation(projects.network.github.repositories.impl)
}

