plugins {
    `java-library`
    id("scafall.common")
    alias(libs.plugins.shadow)
    alias(libs.plugins.fabric.loom)
}

dependencies {
    api(shadow(project(":loader-api"))!!)
    implementation(kotlin("reflect"))
    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")

    // TODO: To be removed next MC release
    mappings(loom.officialMojangMappings())
}

tasks {
    shadowJar {
        archiveBaseName = "scafall-api"

        // Mappings are in the runtime classpath. Not sure why they are included even though we use include for dependencies...
        // So to be sure nothing else slips in, just accept dependencies from the shadow configuration.
        configurations = listOf(project.configurations.shadow.get())
        finalizedBy(remapJar) // TODO: To be removed next MC release

        dependencies {
            include(dependency("com.wolfyscript.scafall:.*"))
        }
    }
    // TODO: To be removed next MC release
    // Disable remapping without having to disable the tasks
    // This will get shaded into other platforms that then use their specific remapper instead.
    // Additionally, this will be a public api, which should work across all platforms.
    remapJar {
        dependsOn(shadowJar)
        targetNamespace = "named"
        inputFile.set(shadowJar.get().archiveFile)
    }
    remapSourcesJar {
        targetNamespace = "named"
    }
}

// TODO: To be removed next MC release
artifacts {
    archives(tasks.remapJar)
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
