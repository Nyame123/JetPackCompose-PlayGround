plugins {
    id("jpg.android.application")
    id("jpg.android.application.compose")
    id("jpg.spotless")
}

android {
    defaultConfig {
        applicationId = "com.google.samples.apps.niacatalog"
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(libs.androidx.activity.compose)
}
