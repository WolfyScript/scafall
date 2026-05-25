import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigotlike")
    id("scafall.docker.run")
    alias(sharedLibs.plugins.shadow)
    alias(sharedLibs.plugins.resource.factory.bukkit)
    alias(sharedLibs.plugins.modrinth.minotaur)
}

dependencies {
    implementation(projects.core)
    implementation(projects.core.coreSpigotlike)
    implementation(projects.identifiers)
    implementation(projects.wrappers)
    implementation(projects.wrappers.wrappersSpigot)
    implementation(projects.scheduler)
    implementation(projects.loaderApi)

    api(sharedLibs.item.nbt.api)
    api(sharedLibs.adventure.platform.bukkit)

    paperweight.paperDevBundle(sharedLibs.versions.papermc.get())
    compileOnly(libs.bundles.spigot.external.plugins)
}

fun archiveName(): String {
    return "${rootProject.name}-${project.version}-${project.name}-${sharedLibs.versions.minecraft.get()}"
}

tasks {
    shadowJar {
        archiveFileName.set("${archiveName()}.jar")
        dependencies {
            include(project(project.projects.core.coreSpigotlike))
        }
        metaInf.duplicatesStrategy = DuplicatesStrategy.FAIL
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
        // Paper test servers
        register("paper") {
            destFileName.set("scafall.jar")
            version.set(sharedLibs.versions.minecraft.get())
            type.set("PAPER")
            imageVersion.set("java${sharedLibs.versions.jdk.get()}")
            ports.add("25566:25565")
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("scafall")
    versionNumber.set(project.version.toString())
    versionType.set("alpha")
    uploadFile.set(tasks.shadowJar)
    gameVersions.set(listOf(sharedLibs.versions.minecraft.get()))
    loaders.set(listOf("paper", "purpur"))
    changelog.set(System.getenv("CHANGELOG") ?: "")
}
