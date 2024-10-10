import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.example.e_store"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.e_store"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }
        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"${localProperties.getProperty("GOOGLE_MAPS_API_KEY")}\"")
        manifestPlaceholders["google_maps_api_key"] = localProperties.getProperty("GOOGLE_MAPS_API_KEY")


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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {



    //Exopplayer for GIF
    implementation("com.google.accompanist:accompanist-drawablepainter:0.35.0-alpha")


    //Coil for images
    implementation("io.coil-kt:coil-compose:2.0.0")
    implementation("io.coil-kt:coil-gif:2.4.0")

    //Lottie
    implementation(libs.lottie)

    //Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Google Maps
    implementation ("com.google.accompanist:accompanist-permissions:0.34.0")


    // Google Maps Compose library
    implementation ("com.google.android.gms:play-services-maps:19.0.0")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.libraries.places:places:3.5.0")
    implementation(libs.firebase.crashlytics.buildtools)


    val mapsComposeVersion = "6.1.0"
    implementation("com.google.maps.android:maps-compose:$mapsComposeVersion")
    // Google Maps Compose utility library
    implementation("com.google.maps.android:maps-compose-utils:$mapsComposeVersion")
    // Google Maps Compose widgets library
    implementation("com.google.maps.android:maps-compose-widgets:$mapsComposeVersion")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("androidx.compose.ui:ui:1.7.3")
    implementation ("androidx.compose.material3:material3:1.3.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.7.3")
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.exoplayer)

    //UI helpers
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")

    // Retrofit for API requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")


    // ViewModel and LiveData for MVVM architecture
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.3.1")

    //Pager for images
    implementation ("com.google.accompanist:accompanist-pager:0.32.0")

    implementation ("com.google.accompanist:accompanist-pager-indicators:0.32.0")



}