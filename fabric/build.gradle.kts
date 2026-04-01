plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.docker.run")
    alias(sharedLibs.plugins.fabric.loom)
    alias(sharedLibs.plugins.shadow)
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

//    include(sharedLibs.adventure.platform.fabric)
    include(sharedLibs.jackson.kotlin)

    shadow(sharedLibs.typesafe.config)
    shadow(sharedLibs.bundles.jackson)
    shadow(sharedLibs.bundles.exposed)

    minecraft(sharedLibs.minecraft)
    implementation(sharedLibs.fabric.loader)
    implementation(sharedLibs.fabric.api)
    implementation(sharedLibs.adventure.minimessage)
    compileOnly(sharedLibs.adventure.platform.shared)
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

            include(dependency(sharedLibs.typesafe.config))
            sharedLibs.bundles.exposed.get().forEach {
                include(dependency(it))
            }
            include(dependency(sharedLibs.jackson.databind))
            sharedLibs.bundles.jackson.get().forEach {
                include(dependency(it))
            }
        }

        finalizedBy("fabric_copy")
        metaInf.duplicatesStrategy = DuplicatesStrategy.FAIL
    }
}

artifacts {
    archives(tasks.shadowJar)
}

minecraftServers {
    libName.set("scafall-${version}-fabric-${sharedLibs.versions.minecraft.get()}.jar")
    servers {
        register("fabric") {
            destPath.set("mods")
            destFileName.set("scafall.jar")
            version.set(sharedLibs.versions.minecraft.get())
            type.set("FABRIC")
            imageVersion.set("java21")
            ports.add("25569:25565")
            extraEnv.put("MODRINTH_PROJECTS", "fabric-api, fabric-language-kotlin")
            extraEnv.put("FABRIC_LOADER_VERSION", sharedLibs.versions.fabric.loader.get())
        }
    }
}
