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
    implementation(projects.spigotlike)
    api(projects.api)
    implementation(projects.spigot.spigotApi)
    implementation(projects.loaderApi)
    api(sharedLibs.item.nbt.api)
    api(sharedLibs.adventure.platform.bukkit)

    paperweight.paperDevBundle(sharedLibs.versions.papermc.get())
    compileOnly(libs.bundles.spigot.external.plugins)

    implementation(projects.common)
}

fun archiveName(): String {
    return "${rootProject.name}-${project.version}-${project.name}-${sharedLibs.versions.minecraft.get()}"
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks {
    shadowJar {
        archiveFileName.set("${archiveName()}-mojmap.jar")

        finalizedBy(reobfJar)

        dependencies {
            include(project(project.projects.spigotlike))
        }
        metaInf.duplicatesStrategy = DuplicatesStrategy.FAIL
    }
    assemble {
        dependsOn(reobfJar)
    }
    reobfJar {
        finalizedBy(jar)
        outputJar.set(layout.buildDirectory.file("libs/${archiveName()}.jar"))
    }
}

artifacts {
    archives(tasks.reobfJar)
}

bukkitPluginYaml {
    name = "scafall"
    version = project.version.toString()
    main = "com.wolfyscript.scafall.spigot.SpigotLoaderPlugin"
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

minecraftServers {
    libName.set("${archiveName()}.jar") // Makes sure to copy the correct file
    servers {
        // Scaffolding will only support 1.21+
        register("spigot") {
            destFileName.set("scafall.jar")
            version.set(sharedLibs.versions.minecraft.get())
            type.set("SPIGOT")
            imageVersion.set("java21-graalvm") // need jdk to build from source
            extraEnv.put("BUILD_FROM_SOURCE", "true")
            ports.add("25565:25565")
        }
    }
}
