import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigot")
    id("scafall.docker.run")
    alias(libs.plugins.shadow)
    alias(libs.plugins.resource.factory.bukkit)

    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":api"))
    api(project(":spigot:spigot-api"))
    implementation(project(":loader-api"))

    paperweight.paperDevBundle(libs.versions.papermc.get())
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

tasks {
    processResources {
        expand(project.properties)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    shadowJar {
        dependsOn(project(":spigot").tasks.getByName<Copy>("createInnerJar"))
        mustRunAfter(jar)
        finalizedBy(reobfJar)

        archiveBaseName = "scafall-spigot"
        archiveClassifier = ""
        archiveAppendix = ""

        dependencies {
            include(project(":api"))
            include(project(":loader-api"))
            include(project(":spigot:spigot-api"))
            include(dependency(libs.jackson.dataformat.hocon))
        }
        metaInf.duplicatesStrategy = DuplicatesStrategy.FAIL

        // Include the inner jar files of the internal implementation
        from(project(":spigot").tasks.getByName("createInnerJar"))
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }

    reobfJar {
        finalizedBy("spigot_1_21_copy")
    }
}

artifacts {
    archives(tasks.shadowJar)
}

minecraftServers {
    libName.set("loader-${version}-reobf.jar") // Makes sure to copy the correct file (when using shaded classifier "-all.jar" this needs to be changed!)
    serversDir.set(file("${System.getProperty("user.home")}${File.separator}minecraft${File.separator}test_servers_v5"))
    val debugPort = System.getProperty("debugPort") ?: "5006"
    val debugPortMapping = "${debugPort}:${debugPort}"
    servers {
        // Scaffolding will only support 1.21+
        register("spigot_1_21") {
            destFileName.set("scafall.jar")
            version.set("1.21.7")
            type.set("SPIGOT")
            imageVersion.set("java21")
            ports.set(setOf(debugPortMapping, "25565:25565"))
        }
        // Paper test servers
        register("paper_1_21") {
            destFileName.set("scafall.jar")
            version.set("1.21.7")
            type.set("PAPER")
            imageVersion.set("java21")
            ports.set(setOf(debugPortMapping, "25566:25565"))
        }
    }
}
