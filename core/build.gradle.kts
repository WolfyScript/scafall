plugins {
    `java-library`
    id("scafall.common")
    alias(sharedLibs.plugins.fabric.loom)
    id("scafall.publishing")
}

dependencies {
    api(projects.loaderApi)
    api(projects.identifiers)
    api(projects.wrappers)
    api(projects.scheduler)
    implementation(kotlin("reflect"))
    minecraft(sharedLibs.minecraft)
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
