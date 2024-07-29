import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
    id("scaffolding.docker.run")
    alias(libs.plugins.spongepowered.gradle)
    alias(libs.plugins.goooler.shadow)
}

dependencies {
    implementation(project(":common"))
}

sponge {
    apiVersion("8.2.0")
    license("GNU GPL 3.0")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("scaffolding") {
        displayName("scaffolding")
        entrypoint("")
        description("APIs and Utils to help in the creation of cross-platform plugins")
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }

}

minecraftServers {
    val debugPort = System.getProperty("debugPort") ?: "5006"
    val debugPortMapping = "${debugPort}:${debugPort}"
    servers {
        register("spongevanilla_1_16") {
            val spongeVersion = "1.16.5-8.2.0"
            type.set("CUSTOM")
            extraEnv.put("SPONGEVERSION", spongeVersion)
            extraEnv.put("CUSTOM_SERVER", "https://repo.spongepowered.org/repository/maven-public/org/spongepowered/spongevanilla/${spongeVersion}/spongevanilla-${spongeVersion}-universal.jar")
            ports.set(setOf(debugPortMapping, "25595:25565"))
        }
    }
}
