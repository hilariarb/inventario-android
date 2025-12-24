plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    id("com.android.library") apply false
}

android {
    namespace = "com.example.inventario_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.inventario_android"
        minSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.gms:google-services:4.4.4") //4.3.15->4.4.4 (catalog)
    implementation("com.google.android.gms:auth-api-impl:11.6.0") // catalog
    implementation("com.google.firebase:firebase-config-ktx:22.1.2") //19.2.0->22.1.2 (catalog)
    implementation(platform("com.google.firebase:firebase-bom:34.7.0")) //catalog
    implementation("com.google.firebase:firebase-firestore:26.0.2") //catalog

    implementation("androidx.appcompat:appcompat:1.7.1") // existing catalog libs.appcompat
    implementation("com.google.android.material:material:1.13.0") //1.9.0->1.13.0 (already as 'material')
    implementation("com.google.firebase:firebase-auth:24.0.1") //catalog
    implementation("com.google.firebase:firebase-messaging:25.0.1") //catalog
}


// ctrl + alt + shift + S