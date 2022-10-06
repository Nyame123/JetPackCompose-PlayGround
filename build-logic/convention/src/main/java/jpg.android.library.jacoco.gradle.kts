import com.bismark.convention.configureJacoco
import gradle.kotlin.dsl.accessors._40eb15a6b217a3d3ee2337af6f50774c.androidComponents
import org.gradle.kotlin.dsl.jacoco

plugins{
    id("com.android.library")
    jacoco
}

android{
    androidComponents{
        configureJacoco(this)
    }
}
