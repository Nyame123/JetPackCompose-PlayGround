plugins {
    id("jpg.android.library")
    id("jpg.android.library.jacoco")
    kotlin("kapt")
    id("kotlinx-serialization")
    //id("dagger.hilt.android.plugin")
    id("jpg.spotless")
}

dependencies {
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-database"))
    implementation(project(":core-datastore"))
    implementation(project(":core-network"))

    testImplementation(project(":core-testing"))
    testImplementation(project(":core-datastore-test"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
