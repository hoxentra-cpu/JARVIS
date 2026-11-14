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
    }
}

rootProject.name = "HAIRUL-OS"

include(":app")
include(":launcher")
include(":core:design")
include(":core:common")
include(":core:device")
include(":feature:dashboard")
include(":feature:assistant")
include(":feature:settings")
