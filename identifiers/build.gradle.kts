plugins {
    `java-library`
    id("scafall.common")
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
            groupId = "com.wolfyscript.scafall"
            artifactId = "scheduler"
            artifact(tasks.kotlinSourcesJar) {
                classifier = "sources"
            }
        }
    }
}
