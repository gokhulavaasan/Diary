plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android") version "2.51" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
//    id("com.google.devtools.ksp") version "1.9.21-1.0.14" apply false
//    id("io.realm.kotlin") version "1.11.0"
}

android {
    namespace = "com.example.diary"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.diary"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.6.0")

    // Firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")

    // Room components
    implementation("androidx.room:room-runtime:2.5.2")
//    ksp("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    // Runtime Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // MongoDB Realm
//    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.3.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//    implementation("io.realm.kotlin:library-sync:10.17.0")
//    api("io.realm.kotlin:library-base:1.11.0")
//    implementation("io.realm.kotlin:library-base:1.11.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48")
//    ksp("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Date-Time Picker
    implementation("androidx.core:core-ktx:1.12.0")

    // Calendar
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:1.0.0") // Replace with the actual dependency

    // Clock
    implementation("com.maxkeppeler.sheets-compose-dialogs:clock:1.0.0") // Replace with the actual dependency

    // Message Bar Compose
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.8")

    // One-Tap Compose
    implementation("com.github.stevdza-san:OneTapCompose:1.0.0")

    // Desugar JDK for newer Java APIs on older devices
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

}