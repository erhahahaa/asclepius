plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  kotlin("plugin.serialization") version "2.0.20"
  id("kotlin-parcelize")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.dicoding.asclepius"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.dicoding.asclepius"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions { jvmTarget = "17" }

  buildFeatures {
    viewBinding = true
    mlModelBinding = true
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.11.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.navigation:navigation-fragment-ktx:2.8.2")
  implementation("androidx.navigation:navigation-ui-ktx:2.8.2")
  implementation("androidx.activity:activity:1.9.2")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  // TensorFlow Lite dependencies
  val tfliteExt = "0.4.4"
  implementation("org.tensorflow:tensorflow-lite:2.16.1")
  implementation("org.tensorflow:tensorflow-lite-support:$tfliteExt")
  implementation("org.tensorflow:tensorflow-lite-metadata:$tfliteExt")
  implementation("org.tensorflow:tensorflow-lite-task-vision:$tfliteExt")

  //  uCrop library
  implementation("com.github.yalantis:ucrop:2.2.9")

  //  room database
  val room = "2.6.1"
  implementation("androidx.room:room-runtime:$room")
  annotationProcessor("androidx.room:room-compiler:$room")
  ksp("androidx.room:room-compiler:$room")
  implementation("androidx.room:room-ktx:$room")
}
