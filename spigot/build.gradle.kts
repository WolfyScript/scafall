plugins {
    `java-library`
    `maven-publish`
    id("scaffolding.common")
    id("scaffolding.docker.run")
    alias(libs.plugins.goooler.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.papermc.paper)
}

tasks {
    shadowJar {
        archiveFileName = "scaffolding-spigot.innerjar"

        dependencies {
            include(dependency("com.wolfyscript.scaffolding:.*"))
        }
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
