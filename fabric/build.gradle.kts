plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.docker.run")
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.shadow)
}

loom {
    serverOnlyMinecraftJar()

    mods {
        create("scafall") {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    api(shadow(projects.api)!!)
    api(shadow(projects.common)!!)

    implementation(projects.loaderApi)

    include(libs.adventure.platform.fabric)
    include(libs.jackson.kotlin)

    shadow(libs.hocon)
    shadow(libs.bundles.jackson)
    shadow(libs.bundles.exposed)

    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
    // TODO: To be removed next MC release
    mappings(
        loom.layered {
            officialMojangMappings()
//            parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
        }
    )
    // TODO: Change next MC release
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.adventure.text.minimessage)
    modCompileOnly(libs.adventure.platform.shared)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.fabric"
            artifactId = "scafall-fabric"
        }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
    shadowJar {
        // Mappings are in the runtime classpath. Not sure why they are included even though we use include for dependencies...
        // So to be sure nothing else slips in, just accept dependencies from the shadow configuration.
        configurations = listOf(project.configurations.shadow.get())

        dependencies {
            include(project(project.projects.api))
            include(project(project.projects.common))

            include(dependency(libs.hocon))
            libs.bundles.exposed.get().forEach {
                include(dependency(it))
            }
            include(dependency(libs.jackson.databind))
            libs.bundles.jackson.get().forEach {
                include(dependency(it))
            }
        }

        metaInf.duplicatesStrategy = DuplicatesStrategy.FAIL
    }
    // TODO: To be removed next MC release
    remapJar {
        dependsOn(shadowJar)
        finalizedBy("fabric_copy")
        inputFile.set(shadowJar.get().archiveFile)
        archiveBaseName.set("scafall")
        archiveClassifier.set("fabric-${libs.versions.minecraft.get()}")
    }
}

artifacts {
    // TODO: Change next MC release
    archives(tasks.remapJar)
}

minecraftServers {
    libName.set("scafall-${version}-fabric-${libs.versions.minecraft.get()}.jar")
    servers {
        register("fabric") {
            destPath.set("mods")
            destFileName.set("scafall.jar")
            version.set(libs.versions.minecraft.get())
            type.set("FABRIC")
            imageVersion.set("java21")
            ports.add("25569:25565")
            extraEnv.put("MODRINTH_PROJECTS", "fabric-api, fabric-language-kotlin")
            extraEnv.put("FABRIC_LOADER_VERSION", libs.versions.fabric.loader.get())
        }
    }
}
