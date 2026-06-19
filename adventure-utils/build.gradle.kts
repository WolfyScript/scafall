plugins {
    `java-library`
    id("scafall.common")
    alias(sharedLibs.plugins.fabric.loom)
    id("scafall.publishing")
}

dependencies {
    minecraft(sharedLibs.minecraft)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components.getByName("java"))
            groupId = "com.wolfyscript.scafall.adventure-utils"
            artifactId = "adventure-utils"
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
    }
}
