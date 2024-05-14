import mir.anika1d.repgit.buildlogic.ApkConfig.GITHUB_APP_SECRET_KEY

plugins {
    id("repgit.android-compose")
}

android {
    namespace = "mir.anika1d.repgit.rootscreen.impl"
    defaultConfig {
        buildConfigField(
            "String",
            "GITHUB_APP_SECRET_KEY",
            "\"${GITHUB_APP_SECRET_KEY}\""
        )
    }
}

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)

    implementation(libs.bundles.decompose)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.androidx.compose)
    implementation(libs.core.ktx)
    implementation(libs.openid)

    implementation(projects.components.core.decompose)
    implementation(projects.components.core.ui.theme)
    implementation(projects.components.core.ui.composable)
    implementation(projects.components.core.ui.data)
    implementation(projects.components.rootscreen.api)
    implementation(projects.components.searchscreen.api)
    implementation(projects.components.searchscreen.impl)
    implementation(projects.components.repodetailscreen.api)
    implementation(projects.components.repodetailscreen.impl)
    implementation(projects.components.downloadscreen.api)
    implementation(projects.components.downloadscreen.impl)
    implementation(projects.components.authscreen.api)
    implementation(projects.components.authscreen.impl)
    implementation(projects.network.core.data)
    implementation(projects.datastore.impl)
    implementation(projects.datastore.api)
    implementation(projects.network.usecases.auth)
    implementation(projects.network.usecases.users)
    implementation(projects.components.profilescreen.api)
    implementation(projects.components.profilescreen.impl)
    implementation(projects.components.core.resources)
}
