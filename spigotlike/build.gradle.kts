import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigotlike")
    id("scafall.docker.run")
    alias(libs.plugins.shadow)
    alias(libs.plugins.resource.factory.bukkit)
}

dependencies {
    api(projects.api)
    implementation(projects.spigot.spigotApi)
    implementation(projects.loaderApi)
    api(libs.tr7zw.item.nbt.api)
    api(libs.adventure.platform.bukkit)

    paperweight.paperDevBundle(libs.versions.papermc.get())
    compileOnly(libs.bundles.spigot.external.plugins)

    implementation(project(":common"))
}

fun archiveName(): String {
    return "scafall-${project.version}-spigot-${libs.versions.minecraft.get()}"
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

tasks {
    shadowJar {
        archiveFileName.set("${archiveName()}-mojmap.jar")

        dependencies {
            include(project(project.projects.api))
            include(project(project.projects.loaderApi))
            include(project(project.projects.spigot.spigotApi))
            include(project(project.projects.common))
            include(dependency(libs.jackson.dataformat.hocon))
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
    apiVersion = libs.versions.minecraft.get() // Only support the latest Minecraft version!
    authors.add("WolfyScript")
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP

    libraries.apply {
        addAll(
            libs.kotlin.stdlib.get().toString(),
            libs.kotlin.reflect.get().toString(),
            libs.jetbrains.annotations.get().toString(),
            libs.hocon.get().toString(),
        )
        libs.bundles.exposed.get().forEach {
            add(it.toString())
        }

        libs.bundles.adventure.get().forEach {
            add(it.toString())
        }
        add(libs.adventure.platform.bukkit.get().toString())

        addAll(
            libs.jackson.kotlin.get().toString(),
            libs.jackson.databind.get().toString(),
            libs.jackson.annotations.get().toString(),
            libs.jackson.core.get().toString(),
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
