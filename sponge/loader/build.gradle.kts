import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    alias(libs.plugins.spongepowered.gradle)
}

dependencies {
    implementation(project(":loader-api"))
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
        entrypoint("com.wolfyscript.scafall.sponge.loader.SpongeLoaderPlugin")
        description("APIs and Utils to help in the creation of cross-platform plugins")
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }

}
