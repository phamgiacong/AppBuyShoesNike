plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hn_2452.shoes_nike"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hn_2452.shoes_nike"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.3")
    implementation("com.google.firebase:firebase-messaging-ktx:23.3.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.5.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.hbb20:ccp:2.6.0")

    val lifecycleVersion = "2.6.2"

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    // Swipe layout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // Epoxy RecycleView
    implementation ("com.airbnb.android:epoxy:5.1.3")
    // Retrofit HTTP
    val retrofit = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")
    implementation("com.google.code.gson:gson:2.9.0")
    // Coil load image
    implementation("io.coil-kt:coil:2.4.0")
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // add placeholder
    implementation("com.facebook.shimmer:shimmer:0.1.0@aar")

    // loading
    implementation("com.github.ybq:Android-SpinKit:1.2.0")

    // room database
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // flex box layout
    implementation("com.google.android:flexbox:2.0.1")

    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))

    // Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:[4,5)")

    // Google Play Service
    implementation("com.google.android.gms:play-services-auth:20.7.0")

}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}