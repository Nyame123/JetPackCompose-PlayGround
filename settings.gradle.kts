pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "JetPackComposePlayGround"
include (
    ":app",
    ":core-navigation",
    ":core-data",
    ":core-datastore",
    ":core-network",
    ":feature-foryou",
    ":core-model",
    ":core-ui",
    ":core-testing",
    ":feature-interests",
    ":core-common"
)
