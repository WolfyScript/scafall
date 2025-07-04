plugins {
    `java-library`
    id("scafall.common")
    alias(libs.plugins.shadow)
    alias(libs.plugins.fabric.loom)
}

dependencies {
    compileOnly(project(":loader-api"))
    implementation(kotlin("reflect"))
    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")

    mappings(
        loom.layered {
            officialMojangMappings()
//            parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
        }
    )
}

tasks {
    shadowJar {
        archiveFileName = "scafall-api"

        dependencies {
            include(dependency("com.wolfyscript.scafall:.*"))
        }
    }
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

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall"
            artifactId = "api"
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
    }
}
