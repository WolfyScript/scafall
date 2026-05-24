plugins {
    `java-library`
    id("scafall.common")
    alias(sharedLibs.plugins.shadow)
    alias(sharedLibs.plugins.fabric.loom)
}

dependencies {
    api(projects.loaderApi)
    api(projects.identifiers)
    api(projects.wrappers)
    api(projects.scheduler)
    implementation(kotlin("reflect"))
    minecraft(sharedLibs.minecraft)
}

tasks {
    shadowJar {
        archiveBaseName = "scafall-core"

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
            groupId = "com.wolfyscript.scafall.core"
            artifactId = "core"
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
    }
}
