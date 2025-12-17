import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigotlike")
    id("scafall.docker.run")
    alias(libs.plugins.shadow)
    alias(libs.plugins.resource.factory.bukkit)
}

dependencies {
    implementation(projects.spigotlike)
    api(projects.api)
    implementation(projects.spigot.spigotApi)
    implementation(projects.loaderApi)
    api(libs.tr7zw.item.nbt.api)
    api(libs.adventure.platform.bukkit)

    paperweight.paperDevBundle(libs.versions.papermc.get())
    compileOnly(libs.bundles.spigot.external.plugins)

    implementation(project(":common"))
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

fun archiveName(): String {
    return "${rootProject.name}-${project.version}-${project.name}-${libs.versions.minecraft.get()}"
}

tasks {
    shadowJar {
        archiveFileName.set("${archiveName()}-mojmap.jar")
        dependencies {
            include(project(project.projects.spigotlike))
//            include(dependency(libs.jackson.core))
//            include(dependency(libs.jackson.databind))
//            include(dependency(libs.jackson.annotations))
//            include(dependency(libs.jackson.kotlin))
        }
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
        metaInf.duplicatesStrategy = DuplicatesStrategy.FAIL
//        relocate("com.fasterxml.jackson", "com.wolfyscript.scafall.lib.jackson")
    }
    assemble {
        dependsOn(shadowJar)
    }
}

artifacts {
    archives(tasks.shadowJar)
}

bukkitPluginYaml {
    name = "scafall"
    version = project.version.toString()
    main = "com.wolfyscript.scafall.paper.PaperLoaderPlugin"
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

minecraftServers {
    libName.set("${archiveName()}-mojmap.jar") // Makes sure to copy the correct file
    servers {
        // Paper test servers
        register("paper") {
            destFileName.set("scafall.jar")
            version.set(libs.versions.minecraft.get())
            type.set("PAPER")
            imageVersion.set("java21")
            ports.add("25566:25565")
        }
    }
}
