// Top-level build file where you can add configuration options common to all sub-projects/modules.

//buildscript {
//    dependencies {
//        // Right now this classpath is needed for compatibility of the
//        // MongoDB Realm with Kotlin 2.0
//        classpath(libs.realm.kotlin.gradle.plugin)
//    }
//}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.android.library") version "8.2.0" apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
//    id("com.google.devtools.ksp") version "1.9.21-1.0.14" apply false
    id("io.realm.kotlin") version "1.11.0"
}
