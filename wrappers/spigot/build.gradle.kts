plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigotlike")
    id("scafall.publishing")
    alias(sharedLibs.plugins.shadow)
}

dependencies {
    api(projects.wrappers)
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
            groupId = "com.wolfyscript.scafall.wrappers"
            artifactId = "wrappers-spigot"
            artifact(tasks.kotlinSourcesJar)
        }
    }
}
