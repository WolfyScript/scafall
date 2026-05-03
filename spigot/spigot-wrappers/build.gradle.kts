plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigotlike")
    alias(sharedLibs.plugins.shadow)
}

dependencies {
    api(projects.core)
    api(sharedLibs.item.nbt.api)
    api(sharedLibs.adventure.platform.bukkit)
    paperweight.paperDevBundle(sharedLibs.versions.papermc.get())
}

tasks {
    kotlinSourcesJar {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allJava)
    }
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.spigot"
            artifactId = "spigot-api"
            artifact(tasks.kotlinSourcesJar)
        }
    }
}
