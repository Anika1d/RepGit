plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.ksp) apply true
}

android.namespace = "mir.anika1d.repgit.database.api"



dependencies {
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
