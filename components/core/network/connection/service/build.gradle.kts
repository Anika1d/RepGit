plugins {
    id("repgit.android-lib")
}

android.namespace = "mir.anika1d.core.network.connection.service"

dependencies {
    implementation(libs.kotlin.coroutines)
    implementation(libs.annotation.jvm)
    implementation(libs.koin.core)
}
