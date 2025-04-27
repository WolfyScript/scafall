import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency
import utils.convertToEpochVer

plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.docker.run")
    alias(libs.plugins.shadow)
    alias(libs.plugins.spongepowered.gradle)
}

repositories {
    maven("https://artifacts.wolfyscript.com/artifactory/gradle-dev")
}

dependencies {
    implementation(project(":api"))
    implementation(project(":loader-api"))
    implementation(libs.slf4j.api)
    implementation(libs.reflections)
}

sponge {
    apiVersion(libs.versions.sponge.api.get())
    license("GNU GPL 3.0")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("scafall") {
        version(project.version.toString().convertToEpochVer())
        displayName("scafall")
        description("")
        entrypoint("com.wolfyscript.scafall.sponge.loader.SpongeLoaderPlugin")
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

tasks {
    processResources {
        expand(project.properties)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    shadowJar {
        dependsOn(project(":sponge").tasks.shadowJar.get())
        mustRunAfter("jar")

        archiveBaseName = "scafall-sponge"
        archiveClassifier = ""
        archiveAppendix = ""

        include("**")

        dependencies {
            include(dependency("${libs.reflections.get().group}:.*"))
            include(dependency("org.javassist:.*"))
            include(dependency("com.wolfyscript.scafall:.*"))
            include(dependency("org.jetbrains:.*"))
            include(dependency("org.jetbrains.kotlin:.*"))
        }

        mergeServiceFiles()

        // Include the inner jar files for internal implementation
        from(project(":sponge").tasks.shadowJar.get().archiveFile)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }
}

artifacts {
    archives(tasks.shadowJar)
}

minecraftServers {
    serversDir.set(file("${System.getProperty("user.home")}${File.separator}minecraft${File.separator}test_servers_v5"))
    libName.set("scafall-sponge-${version}.jar") // Makes sure to copy the correct file (when using shaded classifier "-all.jar" this needs to be changed!)
    val debugPort = System.getProperty("debugPort") ?: "5006"
    val debugPortMapping = "${debugPort}:${debugPort}"

    servers {
        register("spongevanilla_14") {
            val spongeVersion = "1.21.4-14.0.0-RC2113"
            destFileName.set("scafall-loader.jar")
            imageVersion.set("java21")
            type.set("CUSTOM")
            extraEnv.put("SPONGEVERSION", spongeVersion)
            extraEnv.put(
                "CUSTOM_SERVER",
                "https://repo.spongepowered.org/repository/maven-public/org/spongepowered/spongevanilla/${spongeVersion}/spongevanilla-${spongeVersion}-universal.jar"
            )
            ports.set(setOf(debugPortMapping, "25595:25565"))
        }
    }
}
