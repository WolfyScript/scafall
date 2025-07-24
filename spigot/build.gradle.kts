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
    compileOnly(libs.bundles.spigot.external.plugins)

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
