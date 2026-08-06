plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "com.pairlix.dating"
    compileSdk = 36

    bundle {
        language {
            enableSplit = false
        }
    }
    packagingOptions {
        jniLibs {
            useLegacyPackaging =false
        }
    }



    defaultConfig {
        applicationId = "com.pairlix.dating"
        minSdk = 25
        targetSdk = 35
        versionCode = 9
        versionName = "1.1"
        resourceConfigurations += setOf("en", "hi", "ar")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
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
        isCoreLibraryDesugaringEnabled=true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    packaging {
        jniLibs {
            excludes += "lib/x86/*.so"
            excludes += "lib/x86_64/*.so"
            useLegacyPackaging = false  // ← ADD THIS
        }
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
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.benchmark.traceprocessor.android)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.simplecountrypicker)
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("com.github.Kaaveh:sdp-compose:1.1.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("io.coil-kt:coil-gif:2.6.0")
    implementation("com.airbnb.android:lottie-compose:4.0.0")
    implementation("androidx.compose.material:material-icons-core:1.6.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // ============= Hilt =============
    implementation("com.google.dagger:hilt-android:2.57.2")
    ksp("com.google.dagger:hilt-android-compiler:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // ============= Retrofit + OkHttp =============
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.retrofit2:converter-moshi:3.0.0")
    implementation("com.squareup.retrofit2:converter-scalars:3.0.0")

    // Gson (optional)
    implementation("com.google.code.gson:gson:2.13.2")

    // OkHttp BOM + Dependencies - FIXED SECTION
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")  // ← ADD THIS LINE!
    implementation("com.squareup.okhttp3:logging-interceptor")  // ← This replaces libs.logging.interceptor

    // ============= Coroutines + Lifecycle =============
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")

    // ============= UI / Other =============
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    // CameraX
    implementation("androidx.camera:camera-core:1.3.2")
    implementation("androidx.camera:camera-camera2:1.3.2")
    implementation("androidx.camera:camera-lifecycle:1.3.2")
    implementation("androidx.camera:camera-view:1.3.0")

    // ML Kit
    implementation("com.google.mlkit:face-detection:16.1.7")
    implementation("com.google.mlkit:text-recognition:16.0.1")

    // RxJava 3
    implementation("io.reactivex.rxjava3:rxjava:3.1.7")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    implementation("io.github.lucksiege:pictureselector:v3.11.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.google.android.gms:play-services-location:21.2.0")

    // Subsampling Scale Image View
    implementation("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")

    implementation("io.socket:socket.io-client:2.1.2")

    implementation("com.google.android.gms:play-services-maps:18.1.0")

    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")

    //exoplayer
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")

    //crop image
    implementation("com.github.yalantis:uCrop:2.2.10")

    // FaceLivenessDetector dependency
    implementation("com.amplifyframework.ui:liveness:1.5.0")

    // Amplify Auth dependency
    implementation("com.amplifyframework:aws-auth-cognito:2.29.0")
    implementation("androidx.compose.material3:material3:1.2.0")

    // Support for Java 8 features
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation ("io.agora.rtc:full-sdk:4.5.0")

    implementation ("androidx.lifecycle:lifecycle-process:2.7.0")


    //firebase
    implementation(platform("com.google.firebase:firebase-bom:26.3.0"))
    implementation ("com.google.firebase:firebase-messaging-ktx:23.1.1")
    implementation ("com.google.firebase:firebase-auth:22.3.1")


    //google login
    implementation ("androidx.credentials:credentials:1.3.0")
    implementation ("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    //video compress

    implementation ("com.github.AbedElazizShe:LightCompressor:1.3.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("io.coil-kt:coil-video:2.5.0")


//crashlytics
    implementation ("com.google.firebase:firebase-crashlytics-ktx:18.6.0")
    implementation ("com.google.firebase:firebase-analytics-ktx:21.5.0")

    implementation ("com.valentinilk.shimmer:compose-shimmer:1.0.3")
    implementation("com.android.billingclient:billing:6.2.1")
}









