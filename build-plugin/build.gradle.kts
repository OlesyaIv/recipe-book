plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
            implementationClass = "BuildPluginJvm"
        }
        register("build-kmp") {
            id = "build-kmp"
            implementationClass = "BuildPluginMultiplatform"
        }
        register("build-pgContainer") {
            id = "build-pgContainer"
            implementationClass = "BuildPluginPgContainer"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.binaryCompatibilityValidator)
    implementation(libs.testcontainers.postgres)
    implementation(libs.testcontainers.core)
    implementation(libs.db.postgres)
}
