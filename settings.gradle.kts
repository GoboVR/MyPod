pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") } // needed for NewPipeExtractor (used by innertube)
    }
}

rootProject.name = "MyPod"
include(":app")
include(":innertube")
