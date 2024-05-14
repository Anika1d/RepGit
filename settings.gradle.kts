enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    includeBuild("buildlogic")

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
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "RepGit"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":instances:app",

    ":network:core:client",
    ":network:core:data",

    ":network:github:services:api",
    ":network:github:services:impl",

    ":network:github:repositories:api",
    ":network:github:repositories:impl",

    ":network:usecases:issues",
    ":network:usecases:search",
    ":network:usecases:auth",
    ":network:usecases:users",
    ":network:usecases:repository",

    ":database:api",
    ":database:impl",
    ":datastore:api",
    ":datastore:impl",

    ":components:core:decompose",
    ":components:core:network:connection:service",
    ":components:core:ui:theme",
    ":components:core:resources",
    ":components:core:ui:composable",
    ":components:core:ui:data",

    ":components:singleactivity:impl",

    ":components:rootscreen:api",
    ":components:rootscreen:impl",

    ":components:authscreen:api",
    ":components:authscreen:impl",

    ":components:profilescreen:api",
    ":components:profilescreen:impl",

    ":components:searchscreen:api",
    ":components:searchscreen:impl",

    ":components:repodetailscreen:api",
    ":components:repodetailscreen:impl",

    ":components:downloadscreen:api",
    ":components:downloadscreen:impl",


    ":downloadlogic:service",
    ":downloadlogic:receiver"
    )
