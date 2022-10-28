plugins {
    id("jpg.android.library")
    id("jpg.android.library.jacoco")
    kotlin("kapt")
    id("jpg.spotless")
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(project(":core-testing"))
}
