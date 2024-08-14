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

include(":spigot:platform")
project(":spigot:platform").projectDir = file("spigot/platform")

// Sample Plugin Modules
val samplesDir: String = "samples"

fun samplePlugin(root: String, vararg modules: String) {
    println("Samples dir: $samplesDir")
    include(":$samplesDir:$root")
    project(":$samplesDir:$root").projectDir = file("$samplesDir/$root")

    modules.forEach {
        // platform loader project
        include(":$samplesDir:$root:$it")
        project(":$samplesDir:$root:$it").projectDir = file("$samplesDir/$root/${it.replace(":", "/")}")
    }
}

samplePlugin("multi-platform-plugin", "common", "spigot", "spigot:loader", "sponge", "sponge:loader")
samplePlugin("single-platform-plugin", "spigot")
