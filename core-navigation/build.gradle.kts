@kotlin.Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("jpg.android.library")
    kotlin("kapt")
    id("jpg.android.library.jacoco")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
    id("jpg.spotless")
}

dependencies {
    //navigation
    api(libs.androidx.hilt.navigation.compose)
    api(libs.androidx.navigation.compose)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
