plugins {
    `java-library`
    id("scafall.common")
    id("scafall.publishing")
    id("build.docs.changelog")
    alias(sharedLibs.plugins.fabric.loom)
}

dependencies {
    implementation(kotlin("reflect"))
    minecraft(sharedLibs.minecraft)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.identifiers"
            artifactId = "identifiers"
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
    }
}
