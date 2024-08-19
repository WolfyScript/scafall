import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    `java-library`
    id("scaffolding.common")
    alias(libs.plugins.goooler.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":loader-api"))
    api(project(":api"))
    implementation(project(":spigot"))

    compileOnly(libs.papermc.paper)
}

archivesName = "scaffolding-spigot-platform"

tasks {
    processResources {
        expand(project.properties)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    shadowJar {
        dependsOn(project(":spigot").tasks.shadowJar.get())
        mustRunAfter("jar")

        archiveBaseName = "scaffolding-spigot-platform"
        archiveClassifier = ""
        archiveAppendix = ""

        dependencies {
            include(dependency("com.wolfyscript.scaffolding:.*"))
        }

        mergeServiceFiles()

        // Include the inner jar files for api and internal implementation
//        from(project(":spigot").tasks.shadowJar.get().archiveFile)
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
