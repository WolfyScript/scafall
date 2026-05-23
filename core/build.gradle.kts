plugins {
    `java-library`
    id("scafall.common")
    alias(sharedLibs.plugins.shadow)
    alias(sharedLibs.plugins.fabric.loom)
}

dependencies {
    api(shadow(project(":loader-api"))!!)
    implementation(kotlin("reflect"))
    api(projects.scheduler)
    minecraft(sharedLibs.minecraft)
}

tasks {
    shadowJar {
        archiveBaseName = "scafall-api"

        // Mappings are in the runtime classpath. Not sure why they are included even though we use include for dependencies...
        // So to be sure nothing else slips in, just accept dependencies from the shadow configuration.
        configurations = listOf(project.configurations.shadow.get())

        dependencies {
            include(dependency("com.wolfyscript.scafall:.*"))
        }
    }
}

artifacts {
    archives(tasks.shadowJar)
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
