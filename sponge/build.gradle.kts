import org.spongepowered.gradle.plugin.config.PluginLoaders

plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    alias(libs.plugins.shadow)
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
    compileOnly(project(":loader-api"))
    implementation(project(":common"))
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
}
