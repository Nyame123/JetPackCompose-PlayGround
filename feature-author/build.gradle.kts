plugins {
    id("jpg.android.library")
    id("jpg.android.library.compose")
    id("jpg.android.library.jacoco")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("jpg.spotless")
}

android{
    defaultConfig {
        testInstrumentationRunner = "com.bismark.core.testing.JPGTestRunner"
    }
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-data"))
    implementation(project(":core-common"))
    implementation(project(":core-navigation"))

    testImplementation(project(":core-testing"))
    androidTestImplementation(project(":core-testing"))

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // TODO : Remove this dependency once we upgrade to Android Studio Dolphin b/228889042
    // These dependencies are currently necessary to render Compose previews
    debugImplementation(libs.androidx.customview.poolingcontainer)

    // androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
    configurations.configureEach {
        resolutionStrategy {
            force(libs.junit4)
            // Temporary workaround for https://issuetracker.google.com/174733673
            force("org.objenesis:objenesis:2.6")
        }
    }
}
