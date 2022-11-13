plugins {
    id("jpg.android.library")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("jpg.spotless")
}

dependencies {
    api(project(":core-datastore"))
    implementation(project(":core-testing"))

    api(libs.androidx.dataStore.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)

    configurations.configureEach {
        resolutionStrategy {
            // Temporary workaround for https://issuetracker.google.com/174733673
            force("org.objenesis:objenesis:2.6")
        }
    }
}
