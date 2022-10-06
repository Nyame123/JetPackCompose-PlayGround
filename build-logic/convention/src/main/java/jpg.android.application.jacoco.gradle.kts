import com.bismark.convention.configureJacoco
import gradle.kotlin.dsl.accessors._52d5903b27a2445df1221261cf9ce911.android
import org.gradle.kotlin.dsl.jacoco

plugins {
    id("com.android.application")
    jacoco
}

android{
    androidComponents{
        configureJacoco(this)
    }
}
