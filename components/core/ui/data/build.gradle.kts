plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.serialization) apply true
}

android.namespace = "mir.anika1d.core.ui.data"

dependencies {
    implementation(libs.kotlin.serialization.gen)
    implementation(libs.kotlin.serialization.json)

}
