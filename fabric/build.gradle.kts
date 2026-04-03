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
    api(projects.api)
    api(projects.common)
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
        doNotTrackState("Always process resources to stay up-to-date with versions")

        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "minecraftVersion" to sharedLibs.versions.minecraft.get(),
                "fabricLoaderVersion" to sharedLibs.versions.fabric.loader.get(),
                "javaVersion" to kotlin.target.compilerOptions.jvmTarget.get().target
            )
        }
    }
    shadowJar {
        dependencies {
            include(project(project.projects.api))
            include(project(project.projects.common))
            include(project(project.projects.loaderApi))

            include(dependency(sharedLibs.typesafe.config))
            sharedLibs.bundles.exposed.get().forEach {
                include(dependency(it))
            }
            include(dependency(sharedLibs.jackson.databind))
            sharedLibs.bundles.jackson.get().forEach {
                include(dependency(it))
            }
        }
        archiveFileName.set("scafall-${version}-fabric-${sharedLibs.versions.minecraft.get()}.jar")

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
            imageVersion.set("java25")
            ports.add("25569:25565")
            extraEnv.put("MODRINTH_PROJECTS", "fabric-api, fabric-language-kotlin")
            extraEnv.put("FABRIC_LOADER_VERSION", sharedLibs.versions.fabric.loader.get())
        }
    }
}
