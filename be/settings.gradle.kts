rootProject.name = "be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":common")
include(":biz")
include(":stubs")

include(":api-v1-jackson")
include(":api-v1-mappers")
include(":api-v2-kmp")

include(":app-common")
include(":app-spring")

include(":repo-common")
include(":repo-inmemory")
include(":repo-tests")
include(":repo-stubs")
include(":repo-postgres")
