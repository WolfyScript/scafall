import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    `java-library`
    id("scafall.common")
    alias(libs.plugins.goooler.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":loader-api"))
    api(project(":api"))
    compileOnly(project(":spigot"))
    compileOnly(project(":common"))

    compileOnly(libs.papermc.paper)
}

archivesName = "scafall-spigot-platform"

tasks {
    processResources {
        expand(project.properties)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    shadowJar {
        dependsOn(project(":spigot").tasks.shadowJar.get())
        mustRunAfter("jar")

        configurations = listOf(
            project.configurations.runtimeClasspath.get(),
            project.configurations.compileClasspath.get() // Include compileOnly dependencies to hide implementation from
        )

        archiveBaseName = "scafall-spigot-platform"
        archiveClassifier = ""
        archiveAppendix = ""

        include("**")

        dependencies {
            include(project(":common"))
            include(project(":spigot"))
            include(dependency("com.wolfyscript.scafall:.*"))
        }

        mergeServiceFiles()
    }
}

artifacts {
    archives(tasks.shadowJar)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.spigot"
            artifactId = "spigot-platform"
        }
    }
}
