rootProject.name = "scaffolding"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

sequenceOf(
    "api",
    "common",
    "loader-api"
).forEach {
    include(":$it")
    project(":$it").projectDir = file(it)
}

// Platforms
sequenceOf(
    "spigot",
    "sponge"
).forEach {
    // platform implementation project
    include(":$it")
    project(":$it").projectDir = file(it)
    // platform loader project
    include(":$it:loader")
    project(":$it:loader").projectDir = file("$it/loader")
}
