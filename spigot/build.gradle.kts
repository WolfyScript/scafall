import org.gradle.accessors.dm.RootProjectAccessor
import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigot")
    id("scafall.docker.run")
    alias(libs.plugins.shadow)
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
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

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks {
    shadowJar {
        archiveBaseName = "scafall-spigot"
        archiveClassifier = ""
        archiveAppendix = ""

        finalizedBy(reobfJar)

        dependencies {
            include(project(project.projects.api))
            include(project(project.projects.loaderApi))
            include(project(project.projects.spigot.spigotApi))
            include(project(project.projects.common))
            include(dependency(libs.jackson.dataformat.hocon))
        }
        metaInf.duplicatesStrategy = DuplicatesStrategy.FAIL
    }
    assemble {
        dependsOn(reobfJar)
    }
    reobfJar {
        finalizedBy("spigot_1_21_copy")
    }
}

artifacts {
    archives(tasks.reobfJar)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.spigot"
            artifactId = "spigot"
        }
    }
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
            libs.commons.lang3.get().toString(),
            libs.reflections.get().toString(),
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
    libName.set("spigot-${version}.jar") // Makes sure to copy the correct file (when using shaded classifier "-all.jar" this needs to be changed!)
    servers {
        // Scaffolding will only support 1.21+
        register("spigot") {
            destFileName.set("scafall.jar")
            version.set(libs.versions.minecraft.get())
            type.set("SPIGOT")
            imageVersion.set("java21-graalvm") // need jdk to build from source
            extraEnv.put("BUILD_FROM_SOURCE", "true")
            ports.add("25565:25565")
        }
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
