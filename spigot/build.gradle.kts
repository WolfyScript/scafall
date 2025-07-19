plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigot")
    alias(libs.plugins.shadow)
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
}

dependencies {
    compileOnly(project(":spigot:spigot-api"))
    compileOnly(project(":loader-api"))
    api(libs.tr7zw.item.nbt.api)
    api(libs.adventure.platform.bukkit)

    paperweight.paperDevBundle(libs.versions.papermc.get())
//    compileOnly(libs.bundles.spigot.external.plugins)

//    compileOnly("com.ssomar.score:SCore:4.24.4.15")
//    compileOnly("com.ssomar.executableblocks:ExecutableBlocks:4.24.4.15")
//    compileOnly("com.denizenscript:denizen:1.2.5-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.willfp:eco:6.13.0")
    compileOnly("com.github.LoneDev6:api-itemsadder:3.1.5")
//    compileOnly("com.elmakers.mine.bukkit:MagicAPI:10.2")
//    compileOnly("com.github.AlessioGr:FancyBags:2.7.0")
    compileOnly("com.github.oraxen:oraxen:1.152.0")
//    compileOnly("io.lumine:MythicLib:1.1.5")
    compileOnly("net.Indyuce:MMOItems-API:6.9.2-SNAPSHOT")
//    compileOnly("io.lumine:Mythic-Dist:5.6.1")

    api(project(":common"))
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

tasks {
    shadowJar {
        archiveFileName = "scafall-spigot-dev.jar"

        dependencies {
            include(project(":common"))
        }
    }
    assemble {
        dependsOn(reobfJar)
    }
    reobfJar {
        outputJar.set(layout.buildDirectory.file("libs/scafall-spigot-reobf.jar"))
    }
    register<Copy>("createInnerJar") {
        mustRunAfter(reobfJar)
        dependsOn(reobfJar)
        from(reobfJar)
        into(layout.buildDirectory.file("inner"))
        rename { "scafall-spigot.innerjar" }
    }
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
