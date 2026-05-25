rootProject.name = "scafall"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
//        mavenLocal()
        maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    }

    versionCatalogs {
        create("sharedLibs") {
            from("com.wolfyscript.scafall:scafall-versions:1.3.0")
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

// Main Modules
sequenceOf(
    "identifiers",
    "wrappers",
    "scheduler",
    "loader-api",
    "core",
).forEach {
    include(":$it")
    project(":$it").projectDir = file(it.replace(":", "/"))
}

// Wrappers
sequenceOf(
    "spigot",
).forEach {
    include(":wrappers:${it}")
    project(":wrappers:${it}").apply {
        projectDir = file("wrappers/${it.replace(":", "/")}")
        name = "wrappers-${it.replace(":", "-")}"
    }
}

// Core
sequenceOf(
    "spigotlike",
    "spigot",
    "paper",
    "fabric",
    "sponge",
    "sponge:loader",
).forEach {
    include(":core:${it}")
    project(":core:${it}").apply {
        projectDir = file("core/${it.replace(":", "/")}")
        name = "core-${it.replace(":", "-")}"
    }
}
