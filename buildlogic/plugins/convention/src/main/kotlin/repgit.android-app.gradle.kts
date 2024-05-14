import com.android.build.gradle.BaseExtension
import mir.anika1d.repgit.buildlogic.ApkConfig

plugins {
    id("com.android.application")
    id("kotlin-android")
}

configure<BaseExtension> {
    commonAndroid(project)

    defaultConfig {
        applicationId = ApkConfig.APPLICATION_ID
        manifestPlaceholders["appAuthRedirectScheme"] = "mir.anika1d.repgit"
    }

    buildTypes {
        internal {
            isShrinkResources = true
            isMinifyEnabled = true
            consumerProguardFile(
                "proguard-rules.pro"
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            consumerProguardFile(
                "proguard-rules.pro"
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}