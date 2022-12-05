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
        versionCode = 1
        versionName = "0.0.1" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = "com.bismark.core.testing.JPGTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ".debug"
        }
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        val benchmark by creating {
            initWith(release)
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
            proguardFiles("benchmark-rules.pro")
        }
        val staging by creating {
            initWith(debug)
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("debug")
            applicationIdSuffix = ".staging"
        }
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":feature-author"))
    implementation(project(":feature-interests"))
    implementation(project(":feature-foryou"))
    implementation(project(":feature-topic"))

    implementation(project(":core-ui"))
    implementation(project(":core-navigation"))

    implementation(project(":sync"))

    androidTestImplementation(project(":core-testing"))
    androidTestImplementation(project(":core-datastore-test"))
    androidTestImplementation(project(":core-data-test"))
    androidTestImplementation(project(":core-network"))

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
