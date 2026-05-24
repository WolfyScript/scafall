plugins {
    `java-library`
    id("scafall.common")
    alias(sharedLibs.plugins.fabric.loom)
}

dependencies {
    implementation(kotlin("reflect"))
    api(projects.identifiers)
    minecraft(sharedLibs.minecraft)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.wrappers"
            artifactId = "wrappers"
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
    }
}
