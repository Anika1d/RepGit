enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "buildlogic"

include(
    ":plugins:convention"
)
