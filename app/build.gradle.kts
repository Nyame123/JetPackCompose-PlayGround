plugins {
    id("jpg.android.application")
    id("jpg.android.application.compose")
    id("jpg.android.application.jacoco")
    kotlin("kapt")
    id("jacoco")
    id("dagger.hilt.android.plugin")
    id("jpg.spotless")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.bismark.jetpackcomposeplayground"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled =  false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    implementation(project(":core-navigation"))
    implementation(project(":core-ui"))
    implementation(project(":feature-foryou"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.window.manager)
    implementation(libs.androidx.profileinstaller)

    //Coil
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)

    //compose material design 3
    implementation(libs.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)

    configurations.configureEach {
        resolutionStrategy {
            force(libs.junit4)
            // Temporary workaround for https://issuetracker.google.com/174733673
            force("org.objenesis:objenesis:2.6")
        }
    }
}
