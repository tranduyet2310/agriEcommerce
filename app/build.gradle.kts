plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
    id("kotlinx-serialization")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.argiecommerce"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.argiecommerce"
        minSdk = 24
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    dataBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    // Design Support
    implementation("com.google.android.material:material:1.8.0")
    // Navigation Component
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // CardView
    val cardviewVersion = "1.0.0"
    implementation("androidx.cardview:cardview:$cardviewVersion")
    // RecyclerView
    val recyclerview_version = "1.2.1"
    val vectordrawableVersion = "1.0.0"
    implementation("androidx.recyclerview:recyclerview:$recyclerview_version")
    implementation("androidx.vectordrawable:vectordrawable-animated:$vectordrawableVersion")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    // Retrofit
    val retrofit_version = "2.4.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.0.0-alpha06")
    // Glide
    val glide_version = "4.16.0"
    implementation("com.github.bumptech.glide:glide:$glide_version")
    kapt("com.github.bumptech.glide:compiler:$glide_version")
    // Circle ImageView
    val CircleImgVersion = "3.1.0"
    implementation("de.hdodenhof:circleimageview:$CircleImgVersion")
    // Preference
    val preferenceVersion = "1.1.1"
    implementation("androidx.preference:preference:$preferenceVersion")
    // ViewModel
    val view_model_version = "2.0.0"
    implementation("androidx.lifecycle:lifecycle-extensions:$view_model_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel:$view_model_version")
    // lottie
    val lottie_version = "6.0.0"
    implementation("com.airbnb.android:lottie:$lottie_version")
    //stepView
    implementation("com.github.shuhart:stepview:1.5.1")
    // image slider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
    // paypal
    implementation("com.paypal.checkout:android-sdk:1.2.1")
    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:3.12.1")
    implementation("com.squareup.okhttp3:logging-interceptor:3.6.0")
    // network connection
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")
}