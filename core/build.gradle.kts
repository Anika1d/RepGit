val ktorVersion = rootProject.extra["ktor_version"] as String
val koinVersion = rootProject.extra["koin_version"] as String

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version ("1.9.22")

}

android {
    namespace = "com.mir.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        this.forEach {
            it.buildConfigField( "String", "BASE_URL",  "\"https://api.github.com/\"")
            it.buildConfigField("String","HOST_NAME","\"api.github.com\"")
            it.buildConfigField("String","GITHUB_AUTH_TOKEN","\"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\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures{
        buildConfig=true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {
    /**
     * Ktor implementation
     */
    implementation(platform("io.ktor:ktor-bom:$ktorVersion"))
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation ("io.ktor:ktor-client-android")

    implementation("io.ktor:ktor-client-logging-jvm")
    implementation("ch.qos.logback:logback-classic:1.3.14")

    /**
     * Koin implementation
     */
    implementation(platform("io.insert-koin:koin-bom:$koinVersion"))
    implementation("io.insert-koin:koin-android")
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-core-coroutines")
    implementation("io.insert-koin:koin-ktor")

}
