plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.spigotlike")
    alias(libs.plugins.shadow)
}

dependencies {
    api(project(":api"))
    implementation(project(":common"))
    api(libs.tr7zw.item.nbt.api)
    api(libs.adventure.platform.bukkit)
    paperweight.paperDevBundle(libs.versions.papermc.get())
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
