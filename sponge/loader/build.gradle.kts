import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency.LoadOrder

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.spongepowered.gradle)
    alias(libs.plugins.spongepowered.repository)

    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven(url = "https://artifacts.wolfyscript.com/artifactory/gradle-dev")
    sponge.snapshots()
    sponge.releases()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":loader-api"))
    implementation(libs.slf4j.api)
    compileOnly(libs.reflections)
    compileOnly(libs.fastutil)
    compileOnly(libs.jetbrains.annotations)
}

sponge {
    apiVersion("11.0.0-SNAPSHOT")
    license("GNU GPL 3.0")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("scafall") {
        displayName("scafall")
        entrypoint("com.wolfyscript.scafall.sponge.")
        dependency("spongeapi") {
            loadOrder(LoadOrder.AFTER)
            optional(false)
        }
    }
}
