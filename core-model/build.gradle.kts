@kotlin.Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("jpg.android.library")
    id("jpg.android.library.jacoco")
    id("kotlinx-serialization")
    alias(libs.plugins.ksp)
    id("jpg.spotless")
}

dependencies {
    testImplementation(project(":core-testing"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}
