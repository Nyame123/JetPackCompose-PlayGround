import com.bismark.convention.configureJacoco

plugins{
    id("com.android.library")
    jacoco
}

android{
    androidComponents{
        configureJacoco(this)
    }
}
