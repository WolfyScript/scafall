plugins {
    `java-library`
    `maven-publish`
    id("scafall.common")
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

        archiveBaseName = "scaffolding-spigot"
        archiveClassifier = ""
        archiveAppendix = ""

        dependencies {
            include(dependency("com.wolfyscript.scaffolding:.*"))
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
