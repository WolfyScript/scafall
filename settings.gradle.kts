rootProject.name = "scafall"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    }
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

include(":sponge:platform")
project(":sponge:platform").projectDir = file("sponge/platform")

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
