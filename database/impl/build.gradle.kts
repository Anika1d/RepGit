plugins {
    id("repgit.android-lib")
    alias(libs.plugins.kotlin.ksp) apply true
}

android{
    namespace = "mir.anika1d.repgit.database.impl"
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}
dependencies {
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.ktor)
    implementation(libs.koin.androidx.compose)

    implementation(projects.database.api)
}
