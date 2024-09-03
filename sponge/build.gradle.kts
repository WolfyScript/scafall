import org.spongepowered.gradle.plugin.config.PluginLoaders

plugins {
    `java-library`
    `maven-publish`
//    id("scafall.common")
    id("scafall.docker.run")
    alias(libs.plugins.spongepowered.gradle)
    alias(libs.plugins.goooler.shadow)
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

minecraftServers {
    val debugPort = System.getProperty("debugPort") ?: "5006"
    val debugPortMapping = "${debugPort}:${debugPort}"
    servers {
        register("spongevanilla_11") {
            val spongeVersion = "1.20.6-11.0.0"
            type.set("CUSTOM")
            extraEnv.put("SPONGEVERSION", spongeVersion)
            extraEnv.put("CUSTOM_SERVER", "https://repo.spongepowered.org/repository/maven-public/org/spongepowered/spongevanilla/${spongeVersion}/spongevanilla-${spongeVersion}-universal.jar")
            ports.set(setOf(debugPortMapping, "25595:25565"))
        }
    }
}
