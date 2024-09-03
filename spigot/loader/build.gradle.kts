plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
    id("scafall.docker.run")
    alias(libs.plugins.goooler.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":loader-api"))

    compileOnly(libs.papermc.paper)
}

tasks {
    processResources {
        expand(project.properties)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    shadowJar {
        dependsOn(project(":spigot").tasks.shadowJar.get())
        mustRunAfter("jar")

        archiveBaseName = "scafall-spigot"
        archiveClassifier = ""
        archiveAppendix = ""

        dependencies {
            include(dependency("com.wolfyscript.scafall:.*"))
        }

        mergeServiceFiles()

        // Include the inner jar files for api and internal implementation
        from(project(":api").tasks.shadowJar.get().archiveFile)
        from(project(":spigot").tasks.shadowJar.get().archiveFile)
    }
}

artifacts {
    archives(tasks.shadowJar)
}

minecraftServers {
    val debugPort = System.getProperty("debugPort") ?: "5006"
    val debugPortMapping = "${debugPort}:${debugPort}"
    servers {
        // Scaffolding will only support 1.21+
        register("spigot_1_21") {
            version.set("1.21")
            type.set("SPIGOT")
            imageVersion.set("java21")
            ports.set(setOf(debugPortMapping, "25565:25565"))
        }
        // Paper test servers
        register("paper_1_21") {
            version.set("1.21")
            type.set("PAPER")
            imageVersion.set("java21")
            ports.set(setOf(debugPortMapping, "25566:25565"))
        }
    }
}
