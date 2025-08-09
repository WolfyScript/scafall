plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    alias(libs.plugins.fabric.loom)
}

dependencies {
    api(project(":api"))
    implementation(project(":loader-api"))
    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
    modCompileOnly(libs.adventure.platform.shared)
    mappings(
        loom.layered {
            officialMojangMappings()
//            parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
        }
    )
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.common"
            artifactId = "common"
        }
    }
}

tasks {
    // Disable remapping without having to disable the tasks
    // This will get shaded into other platforms that then use their specific remapper instead.
    // Additionally, this will be a public api, which should work across all platforms.
    remapJar {
        targetNamespace = "named"
    }
    remapSourcesJar {
        targetNamespace = "named"
    }
}

artifacts {
    archives(tasks.remapJar)
}
