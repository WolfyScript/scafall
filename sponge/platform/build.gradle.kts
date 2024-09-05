import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    `java-library`
    id("scafall.common")

    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly(project(":loader-api"))
    api(project(":api"))
    compileOnly(project(":sponge"))
    compileOnly(project(":common"))

    compileOnly(libs.spongepowered.api)
}

archivesName = "scafall-sponge-platform"

tasks {
    processResources {
        expand(project.properties)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    shadowJar {
        dependsOn(project(":sponge").tasks.shadowJar.get())
        mustRunAfter("jar")

        configurations = listOf(
            project.configurations.runtimeClasspath.get(),
            project.configurations.compileClasspath.get() // Include compileOnly dependencies to hide implementation from
        )

        archiveBaseName = "scafall-sponge-platform"
        archiveClassifier = ""
        archiveAppendix = ""

        include("**")

        dependencies {
            include(project(":common"))
            include(project(":sponge"))
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
            groupId = "com.wolfyscript.scafall.sponge"
            artifactId = "sponge-platform"
        }
    }
}
