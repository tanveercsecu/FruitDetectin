plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")


}

android {
    namespace = "com.surendramaran.fruitdetection"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.surendramaran.fruitdetection"
        minSdk = 31
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

    buildFeatures {
        viewBinding = true
    }
}


    dependencies {
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.activity:activity:1.9.0")

        // Firebase
        implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-firestore-ktx")
        implementation("com.google.firebase:firebase-database-ktx")

        implementation("com.google.android.gms:play-services-auth:21.2.0")
        implementation ("com.google.android.material:material:1.12.0") // Check for the latest version


        // CameraX
        val cameraxVersion = "1.4.0-alpha03"
        implementation("androidx.camera:camera-camera2:$cameraxVersion")
        implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
        implementation("androidx.camera:camera-view:$cameraxVersion")
        implementation ("com.google.guava:guava:32.0.1-android")

        // TensorFlow Lite
        implementation("org.tensorflow:tensorflow-lite:2.14.0")
        implementation("org.tensorflow:tensorflow-lite-support:0.4.4")

        // ImagePicker
        implementation("com.github.dhaval2404:imagepicker:2.1")



        // RecyclerView
        implementation("androidx.recyclerview:recyclerview:1.3.2")

        // for api design

        implementation("com.android.volley:volley:1.2.1")
        implementation("org.jsoup:jsoup:1.18.1")
        implementation ("com.squareup.picasso:picasso:2.8")
        implementation("com.google.android.material:material:1.12.0")

3
        // Retrofit for network requests
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        // OkHttp for logging (optional, useful for debugging)
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")


        // Testing dependencies
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    }

