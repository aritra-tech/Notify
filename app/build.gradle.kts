plugins {
    id ("com.android.application")
    kotlin ("android")
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id ("com.google.devtools.ksp")
}

android {
    namespace = "com.aritra.notify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aritra.notify"
        minSdk = 24
        targetSdk = 34
        versionCode = 6
        versionName = "1.3.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility =  JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    
    implementation (libs.androidx.core.ktx)
    implementation (platform(libs.android.kotlin.bom))
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.activity.compose)
    implementation (platform(libs.android.compose.bom))
    implementation (libs.compose.ui)
    implementation (libs.compose.ui.graphics)
    implementation (libs.compose.ui.tooling.preview)
    implementation (libs.compose.material3)

    // Test Dependencies
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.androidx.espresso.core)
    androidTestImplementation (platform(libs.android.compose.bom))
    androidTestImplementation (libs.compose.ui.test.junit4)
    debugImplementation (libs.compose.ui.tooling)
    debugImplementation (libs.compose.ui.test.manifest)

    // Navigation
    implementation (libs.androidx.navigation.compose)

    // Accompanist
    implementation (libs.accompanist.systemuicontroller)
    implementation (libs.accompanist.permissions)

    // Material 3
    implementation (libs.androidx.material3)
    implementation (libs.androidx.material3.window.size)
    implementation (libs.androidx.material.icons.extended)

    // Room
    implementation (libs.androidx.room.runtime)
    implementation (libs.androidx.lifecycle.extensions)
    implementation (libs.androidx.room.ktx)
    implementation (libs.androidx.runtime.livedata)
    annotationProcessor (libs.androidx.room.compiler)
    ksp (libs.androidx.room.compiler)

    // Lifecycle
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)

    // Hilt
    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)

    // DataStore
    implementation (libs.androidx.datastore.preferences)

    // Splash API
    implementation (libs.androidx.core.splashscreen)

    //Coil
    implementation(libs.coil.compose)

    // Biometric
    implementation(libs.androidx.biometric)

    // In-App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
}