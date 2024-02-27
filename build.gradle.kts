buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    }
    extra.apply {
        set("ktor_version", "2.3.8")
        set("koin_version", "3.6.0-wasm-alpha2")
        set("moko_version","0.16.1")
        set("room_version","2.6.1")

    }
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.22" apply false
    id("com.android.library") version "8.2.2" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}
