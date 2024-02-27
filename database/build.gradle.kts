val koinVersion = rootProject.extra["koin_version"] as String
val roomVersion = rootProject.extra["room_version"] as String


plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version ("1.9.22")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.mir.database"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
     * Koin implementation
     */
    implementation(platform("io.insert-koin:koin-bom:$koinVersion"))
    implementation("io.insert-koin:koin-android")
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-core-coroutines")

    /**
     * Room implementation
     */
    implementation ("androidx.room:room-runtime:$roomVersion")
    annotationProcessor( "androidx.room:room-compiler:$roomVersion")
    implementation ("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

}