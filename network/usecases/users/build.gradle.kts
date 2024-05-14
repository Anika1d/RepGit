plugins {
    id("repgit.android-lib")
}

android.namespace = "mir.anika1d.repgit.network.usecases.users"

dependencies {
    implementation(libs.kotlin.coroutines)
    implementation(projects.network.core.data)
    implementation(projects.network.github.repositories.api)
    implementation(projects.network.github.repositories.impl)
    implementation(libs.koin.core)
}

