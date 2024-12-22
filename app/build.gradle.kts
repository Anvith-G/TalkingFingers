plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.fingertalks"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fingertalks"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    implementation ("com.google.android.material:material:1.9.0") // Or latest version
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.google.mlkit:language-id:17.0.3")
    implementation ("com.opencsv:opencsv:5.5.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}