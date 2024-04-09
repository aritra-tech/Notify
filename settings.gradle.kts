pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

val snapshotVersion : String = "11670047"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://androidx.dev/snapshots/builds/$snapshotVersion/artifacts/repository/") }
        google()
        mavenCentral()

    }
}
rootProject.name = "Notify"
include ("app")
