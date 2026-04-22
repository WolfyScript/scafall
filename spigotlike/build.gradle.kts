import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigotlike")
    id("scafall.docker.run")
    alias(sharedLibs.plugins.shadow)
    alias(sharedLibs.plugins.resource.factory.bukkit)
}

dependencies {
    api(projects.core)
    implementation(projects.spigot.spigotWrappers)
    implementation(projects.loaderApi)
    api(sharedLibs.item.nbt.api)
    api(sharedLibs.adventure.platform.bukkit)

    paperweight.paperDevBundle(sharedLibs.versions.papermc.get())
    compileOnly(libs.bundles.spigot.external.plugins)

    implementation(project(":common"))
}

fun archiveName(): String {
    return "scafall-${project.version}-spigot-${sharedLibs.versions.minecraft.get()}"
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

tasks {
    shadowJar {
        archiveFileName.set("${archiveName()}-mojmap.jar")

        dependencies {
            include(project(project.projects.core))
            include(project(project.projects.loaderApi))
            include(project(project.projects.spigot.spigotWrappers))
            include(project(project.projects.common))
            include(dependency(sharedLibs.jackson.dataformat.hocon))
        }
        metaInf.duplicatesStrategy = DuplicatesStrategy.FAIL
    }
}

artifacts {
    archives(tasks.shadowJar)
    default(tasks.shadowJar)
    implementation(tasks.shadowJar)
}

bukkitPluginYaml {
    name = "scafall"
    version = project.version.toString()
    main = "com.wolfyscript.scafall.spigot.loader.SpigotLoaderPlugin"
    apiVersion = sharedLibs.versions.minecraft.get() // Only support the latest Minecraft version!
    authors.add("WolfyScript")
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP

    libraries.apply {
        addAll(
            sharedLibs.kotlin.stdlib.get().toString(),
            sharedLibs.kotlin.reflect.get().toString(),
            sharedLibs.jetbrains.annotations.get().toString(),
            sharedLibs.typesafe.config.get().toString(),
        )
        sharedLibs.bundles.exposed.get().forEach {
            add(it.toString())
        }

        sharedLibs.bundles.adventure.get().forEach {
            add(it.toString())
        }
        add(sharedLibs.adventure.platform.bukkit.get().toString())

        addAll(
            sharedLibs.jackson.kotlin.get().toString(),
            sharedLibs.jackson.databind.get().toString(),
            sharedLibs.jackson.annotations.get().toString(),
            sharedLibs.jackson.core.get().toString(),
        )
    }

    softDepend.addAll(
        "Magic",
        "LWC",
        "PlotSquared",
        "WorldGuard",
        "MythicMobs",
        "MMOItems",
        "BungeeChat",
        "mcMMO",
        "Oraxen",
        "ItemsAdder",
        "PlaceholderAPI",
        "eco",
        "zAuctionHouseV3",
        "SCore"
    )
}
