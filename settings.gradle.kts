rootProject.name = "scafall"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
//        mavenLocal()
        maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    }

    versionCatalogs {
        create("sharedLibs") {
            from("com.wolfyscript.scafall:scafall-versions:1.1-SNAPSHOT")
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net")
            content {
                includeGroupByRegex("net.fabricmc.*")
                includeGroup("fabric-loom")
            }
        }
        maven {
            name = "Sponge"
            url = uri("https://repo.spongepowered.org/repository/maven-public")
            content {
                includeGroupAndSubgroups("org.spongepowered")
            }
        }
        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net")
            content {
                includeGroupAndSubgroups("net.minecraftforge")
            }
        }
        maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
        maven("https://maven.neoforged.net/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Platforms
sequenceOf(
    "core",
    "common",
    "loader-api",
    "spigotlike",
    "spigot",
    "spigot:spigot-wrappers",
    "paper",
    "fabric",
    "sponge",
    "sponge:loader"
).forEach {
    include(":$it")
    project(":$it").projectDir = file(it.replace(":", "/"))
}

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
