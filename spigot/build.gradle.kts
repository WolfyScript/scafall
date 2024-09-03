plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigot")
    alias(libs.plugins.goooler.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")

    maven(url = "https://maven.enginehub.org/repo/")
    maven(url = "https://repo.citizensnpcs.co")
    maven(url = "https://repo.codemc.io/repository/maven-public/")
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven(url = "https://nexus.phoenixdevt.fr/repository/maven-public/")
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
    maven(url = "https://www.iani.de/nexus/content/repositories/public/")
}

dependencies {
    compileOnly(project(":loader-api"))
    compileOnly(libs.papermc.paper)
    api(libs.tr7zw.item.nbt.api)
    api(libs.adventure.platform.bukkit)
    compileOnly(libs.bundles.spigot.external.plugins)

    compileOnly("com.ssomar:SCore:4.0.1")
    compileOnly("com.ssomar.executableblocks:ExecutableBlocks:4.0.1")
    compileOnly("com.denizenscript:denizen:1.2.5-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.1")
    compileOnly("com.willfp:eco:6.13.0")
    compileOnly("com.github.LoneDev6:api-itemsadder:3.1.5")
    compileOnly("com.elmakers.mine.bukkit:MagicAPI:10.2")
    compileOnly("com.github.AlessioGr:FancyBags:2.7.0")
    compileOnly("com.github.oraxen:oraxen:1.152.0")
    compileOnly("io.lumine:MythicLib:1.1.5")
    compileOnly("net.Indyuce:MMOItems-API:6.9.2-SNAPSHOT")
    compileOnly("io.lumine:Mythic-Dist:5.6.1")

    implementation(project(":common"))
}

tasks {
    shadowJar {
        archiveFileName = "scafall-spigot.innerjar"

        dependencies {
            include(dependency("com.wolfyscript.scafall:.*"))
            include(project(":common"))
        }
    }
}

artifacts {
    archives(tasks.shadowJar)
}



publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.spigot"
            artifactId = "spigot-implementation"
        }
    }
}
